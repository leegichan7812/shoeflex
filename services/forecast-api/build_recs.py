import os
import warnings
from datetime import datetime

import numpy as np
import pandas as pd
from numpy import log1p
from scipy.sparse import csr_matrix
from sklearn.metrics.pairwise import cosine_similarity

from implicit.als import AlternatingLeastSquares
from implicit.nearest_neighbours import bm25_weight

import oracledb

# ===== 0) 환경 설정 (성능/경고 완화) =====
# OpenBLAS 스레드 풀 경고 완화 (성능 저하 방지)
if os.environ.get("OPENBLAS_NUM_THREADS") is None:
    os.environ["OPENBLAS_NUM_THREADS"] = "1"

warnings.filterwarnings("ignore", category=UserWarning)

# ===== 1) DB 연결 (Thick 모드) =====
# Instant Client 경로
INSTANT_CLIENT_DIR = r"C:\a03_pythonexp\instantclient_11_2"
oracledb.init_oracle_client(lib_dir=INSTANT_CLIENT_DIR)

# 접속 정보
DB_USER = "TWO"
DB_PW   = "2222"
DB_HOST = "61.252.30.155"
DB_PORT = 1521
DB_SVC  = "xe"   # 환경에 따라 service_name="XE" 또는 sid="XE" 가 맞습니다.

conn_str = f"{DB_USER}/{DB_PW}@{DB_HOST}:{DB_PORT}/{DB_SVC}"
con = oracledb.connect(conn_str)
cur = con.cursor()
print("DB 연결 확인:", con.version)

# ===== 2) 데이터 적재 (구매 집계) =====
SQL = """
SELECT USER_ID,
       PRODUCT_ID,
       SUM(QUANTITY) AS qty,
       SUM(UNIT_PRICE*QUANTITY) AS amount
FROM TWO.PURCHASE_HISTORY_FULL
WHERE ORDER_STATUS IN ('주문완료','배송중','배송완료')
GROUP BY USER_ID, PRODUCT_ID
"""
df = pd.read_sql(SQL, con)

if df.empty:
    print("[INFO] 구매 이력이 없습니다. 스크립트를 종료합니다.")
    cur.close(); con.close()
    raise SystemExit(0)

# 컬럼 소문자 표준화
df.columns = df.columns.str.lower()

# ===== 3) 인덱싱 및 행렬 구성 =====
# 사용자/아이템 인덱스 매핑
u2idx = {u: i for i, u in enumerate(df["user_id"].unique())}
i2idx = {p: i for i, p in enumerate(df["product_id"].unique())}
idx2i = {i: p for p, i in i2idx.items()}

# 인덱스 열 추가
df["u"] = df["user_id"].map(u2idx)
df["i"] = df["product_id"].map(i2idx)

# 사용자-아이템 수량 행렬
# (주의) NumPy 2.0 이상에서는 대소문자 컬럼 접근 피하고 df['qty'] 사용
X = csr_matrix((df["qty"].astype(float), (df["u"], df["i"])),
               shape=(len(u2idx), len(i2idx)))

# ===== 4) 아이템기반 CF(코사인) 유사도 =====
# 희소행렬 그대로 코사인: S = 아이템×아이템 유사도
S = cosine_similarity(X.T, dense_output=False)

def cf_candidates(u_row, topn=30):
    """사용자 u_row(행) 기반 코사인 후보 상위 N 추출."""
    owned = set(u_row.indices.tolist())
    scores = {}
    # 사용자가 소유한 각 아이템 기준으로 이웃 아이템 점수 누적
    for i in owned:
        row = S[i]
        for j, s in zip(row.indices, row.data):
            if j in owned:         # 이미 산 아이템 제외
                continue
            if s <= 0:
                continue
            # 최고 점수만 취함
            if j not in scores or s > scores[j]:
                scores[j] = float(s)
    if not scores:
        return []
    # 점수 상위 topn
    return sorted(scores.items(), key=lambda x: x[1], reverse=True)[:topn]

# ===== 5) ALS 학습 (implicit) =====
# Confidence 행렬 구성 (log1p)
df["conf"] = log1p(df["qty"].astype(float))
Xc = csr_matrix((df["conf"], (df["u"], df["i"])), shape=X.shape)

# BM25 가중치 적용 (스파스 데이터에서 효과적)
Xc_bm25 = bm25_weight(Xc, K1=1.2, B=0.75)

# implicit ALS는 (item × user) 형태 기대 → 전치 후 학습
als = AlternatingLeastSquares(factors=64, regularization=0.01, iterations=15)
als.fit(Xc_bm25.T)

# 학습된 유저/아이템 수 (안전 가드용)
TRAINED_NUM_ITEMS  = als.item_factors.shape[0]
TRAINED_NUM_USERS  = als.user_factors.shape[0]

def als_scores(u_idx: int, item_idxs: np.ndarray) -> np.ndarray:
    """특정 사용자 벡터와 여러 아이템 벡터 내적(선호도 점수)."""
    user_vec = als.user_factors[u_idx]
    item_vecs = als.item_factors[item_idxs]
    return item_vecs @ user_vec

# ===== 6) 보조 유틸 =====
def _norm(x: np.ndarray) -> np.ndarray:
    """0~1 정규화 (NumPy 2.0 대응: np.ptp 사용)"""
    x = np.asarray(x, dtype=float)
    if x.size == 0:
        return x
    return (x - x.min()) / (np.ptp(x) + 1e-8)

# 전역 인기상품(최근 집계에서 qty 합 기준 Top) — 폴백용
pop = (
    df.groupby("product_id", as_index=False)["qty"]
      .sum()
      .sort_values("qty", ascending=False)
)
global_top_items = [pid for pid in pop["product_id"].tolist()]

# ===== 7) 유저별 추천 생성 =====
rows = []
TOPK = 5
CAND_TOPN = 30

for user_id, u_idx in u2idx.items():
    # ✅ 유저 인덱스 범위 가드
    if u_idx < 0 or u_idx >= TRAINED_NUM_USERS:
        # 매핑-행렬 미스매치가 있을 때 스킵(또는 인기 폴백)
        continue

    u_row = X[u_idx]
    if u_row.nnz == 0:
        # 구매내역 없음 → 글로벌 인기 폴백
        picked = []
        for pid in global_top_items:
            if len(picked) >= TOPK:
                break
            picked.append(pid)
        for rank, pid in enumerate(picked, start=1):
            rows.append((user_id, pid, rank, 0.0))
        continue

    # 1) 코사인 후보
    cands = cf_candidates(u_row, topn=CAND_TOPN)

    if cands:
        # 아이템 인덱스 범위 가드 (이상치 방지)
        cands = [(it, sc) for it, sc in cands if 0 <= it < TRAINED_NUM_ITEMS]
        if cands:
            item_idxs, base = zip(*cands)
            item_idxs = np.array(item_idxs, dtype=int)
            base = _norm(np.array(base, dtype=float))

            als_sc = als_scores(u_idx, item_idxs)   # 여기서 더 이상 IndexError 안 남
            als_sc = _norm(als_sc)

            final = 0.4*base + 0.6*als_sc
            order = np.argsort(-final)[:TOPK]
            for rank, k in enumerate(order, start=1):
                pid = idx2i[item_idxs[k]]
                rows.append((user_id, pid, rank, float(final[k])))
        else:
            # 후보가 전부 범위 밖이면 ALS 단독 추천으로 폴백
            owned = set(u_row.indices.tolist())
            recs = als.recommend(u_idx, Xc_bm25, N=TOPK, filter_items=list(owned))
            if recs:
                item_idxs, als_sc = zip(*recs)
                item_idxs = np.array(item_idxs, dtype=int)
                als_sc = _norm(np.array(als_sc, dtype=float))
                for rank, (it, sc) in enumerate(zip(item_idxs, als_sc), start=1):
                    pid = idx2i[it]
                    rows.append((user_id, pid, rank, float(sc)))
            else:
                # 글로벌 폴백
                owned_pids = {idx2i[i] for i in owned}
                picked = []
                for pid in global_top_items:
                    if pid in owned_pids:
                        continue
                    picked.append(pid)
                    if len(picked) >= TOPK:
                        break
                for rank, pid in enumerate(picked, start=1):
                    rows.append((user_id, pid, rank, 0.0))
    else:
        # 2) 코사인 후보 자체가 없으면 → ALS 단독 추천
        owned = set(u_row.indices.tolist())
        recs = als.recommend(u_idx, Xc_bm25, N=TOPK, filter_items=list(owned))
        if recs:
            item_idxs, als_sc = zip(*recs)
            item_idxs = np.array(item_idxs, dtype=int)
            als_sc = _norm(np.array(als_sc, dtype=float))
            for rank, (it, sc) in enumerate(zip(item_idxs, als_sc), start=1):
                pid = idx2i[it]
                rows.append((user_id, pid, rank, float(sc)))
        else:
            # 3) 글로벌 인기 폴백
            owned_pids = {idx2i[i] for i in owned}
            picked = []
            for pid in global_top_items:
                if pid in owned_pids:
                    continue
                picked.append(pid)
                if len(picked) >= TOPK:
                    break
            for rank, pid in enumerate(picked, start=1):
                rows.append((user_id, pid, rank, 0.0))

print(f"[INFO] 추천 생성 완료: {len(rows)} row")

rows_clean = []
for user_id, pid, rank, score in rows:
    rows_clean.append((int(user_id), int(pid), int(rank), float(score)))
# ===== 8) 결과 저장 =====
# (스키마: USER_RECOMMENDATIONS(USER_ID, PRODUCT_ID, RANK_ORDER, SCORE))
cur.execute("DELETE FROM USER_RECOMMENDATIONS")

# GENERATED_AT 컬럼이 있다면 아래 executemany를 이 형태로 바꿔 써도 됩니다:
# rows_with_time = [(*r, datetime.now()) for r in rows]
# cur.executemany(
#     """INSERT INTO USER_RECOMMENDATIONS
#        (USER_ID, PRODUCT_ID, RANK_ORDER, SCORE, GENERATED_AT)
#        VALUES (:1, :2, :3, :4, :5)""",
#     rows_with_time
# )

cur.executemany(
    """INSERT INTO USER_RECOMMENDATIONS
       (USER_ID, PRODUCT_ID, RANK_ORDER, SCORE)
       VALUES (:1, :2, :3, :4)""",
    rows_clean
)

con.commit()
print("[INFO] USER_RECOMMENDATIONS 테이블 갱신 완료")

# ===== 9) 종료 =====
cur.close()
con.close()
print("[DONE]")