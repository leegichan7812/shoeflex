# app_chatbot.py (정리본)
import os, re, logging
from datetime import datetime
from typing import Optional
from fastapi import FastAPI, Depends, HTTPException, status, Header
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from sklearn.feature_extraction.text import TfidfVectorizer
import numpy as np
from sklearn.pipeline import FeatureUnion
from sklearn.svm import LinearSVC
from sklearn.calibration import CalibratedClassifierCV
from sklearn.linear_model import LogisticRegression
import json
try:
    import oracledb
    # Oracle 11g(XE)면 Thick 모드 권장 — Instant Client 경로를 본인 환경에 맞게 지정
    # oracledb.init_oracle_client(lib_dir=r"C:\oracle\instantclient_19_22")
except Exception:
    oracledb = None  # 배포 환경에 따라 드라이버가 없을 때를 대비한 안전장치


logging.basicConfig(level=logging.INFO)
log = logging.getLogger("chatbot")
# Set-Location C:\k01_javaexp\sp_workspace\prj3\services\chatbot-api
DB_DSN  = os.environ.get("DB_DSN", "localhost:1521/XE")
DB_USER = os.environ.get("DB_USER", "TWO")
DB_PW   = os.environ.get("DB_PW",   "2222")

def load_intent_responses(locale="ko"):
    if oracledb is None:
        log.warning("[bot] oracledb 모듈 없음 → DB 템플릿 로드 건너뜀")
        return {}
    try:
        with oracledb.connect(user=DB_USER, password=DB_PW, dsn=DB_DSN) as conn:
            cur = conn.cursor()
            cur.execute("""
                SELECT INTENT_CODE, MESSAGE, ROUTE, SUGGESTIONS_JSON
                  FROM BOT_INTENT_REPLY
                 WHERE LOCALE = :loc
            """, loc=locale)
            data = {}
            for code, msg, route, sug_json in cur:
                try:
                    suggestions = json.loads(sug_json) if sug_json else None
                except Exception:
                    suggestions = None
                data[code] = {"msg": msg, "route": route, "suggestions": suggestions}
            return data
    except Exception as e:
        log.warning(f"[bot] DB에서 답변 템플릿 로드 실패: {e}")
        return {}

try:
    from utils_route import strip_route  # 동일 폴더에서 실행될 때
except ModuleNotFoundError:
    # 부모 폴더에서 실행해도 동작하도록 경로 보정
    import os, sys
    sys.path.append(os.path.dirname(__file__))
    from utils_route import strip_route
# ---------------------
# 설정 (환경변수)
# ---------------------
BOT_SECRET = os.environ.get("BOT_SECRET", "my-secret")
BOT_THRESHOLD = float(os.environ.get("BOT_THRESHOLD", "0.30"))
ALLOW_ORIGINS = [o.strip() for o in os.environ.get("ALLOW_ORIGINS", "http://localhost:8080").split(",")]

# ---------------------
# CORS/앱
# ---------------------
app = FastAPI(title="Chatbot API")
app.add_middleware(
    CORSMiddleware,
    allow_origins=ALLOW_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ---------------------
# 보안: API Key 의존성 (Header로 받기)
# ---------------------
def require_api_key(x_api_key: Optional[str] = Header(None)):
    if x_api_key != BOT_SECRET:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="unauthorized")

# ---------------------
# 전처리/모델
# ---------------------
def normalize_text(t: str) -> str:
    t = (t or "").strip()
    t = t.lower()  # 영어 인삿말 하이/헬로 등도 캐치
    t = re.sub(r"\s+", " ", t)

    # 자주 틀리는 띄어쓰기/동의어 정규화
    synonyms = {
        "환불  정책": "환불 정책",
        "반품신청": "반품 신청",
        "교환 요청": "교환 신청",
        "운송장번호": "운송장 번호",
        "정 사이즈": "정사이즈",
        "카드취소": "카드 취소",
    }
    for src, tgt in synonyms.items():
        t = t.replace(src, tgt)
    return t

# —— (1) 데이터 원문
X_train = [
    "안녕","안녕하세요","반가워","좋은 아침",
    "현재 시간 알려줘","몇 시야?","지금 몇 시입니까","시간 좀 알려줄래?",
    "고마워","감사합니다","고맙습니다",
    "주문 조회","내 주문 상태 알려줘","배송 어디야","운송장 번호 알려줘",
    "반품 신청하고 싶어요","교환 요청","상품 취소하고 싶어요","수거 일정 변경",
    "사이즈 추천","사이즈 가이드","정사이즈인가요",
    "환불 언제 되나요","환불 정책 알려줘",
    "쿠폰 사용 방법","적립금 사용","포인트 확인",
]
y_train = [
    "인사","인사","인사","인사",
    "시간","시간","시간","시간",
    "감사","감사","감사",
    "주문_조회","주문_조회","주문_조회","주문_조회",
    "반품_신청","교환_신청","취소_신청","수거_변경",
    "사이즈","사이즈","사이즈",
    "환불","환불",
    "혜택","혜택","혜택",
]

# —— (2) 정규화
X_train_norm = [normalize_text(x) for x in X_train]

# —— (3) 특징 하이브리드: 문자 n-gram + 단어 n-gram
char_vec  = TfidfVectorizer(analyzer="char", ngram_range=(2, 5), min_df=1)
word_vec  = TfidfVectorizer(token_pattern=r"(?u)\b\w+\b", ngram_range=(1, 2))
features  = FeatureUnion([("char", char_vec), ("word", word_vec)])

X_vec = features.fit_transform(X_train_norm)
# 라벨별 최소 샘플 수 계산
from collections import Counter
min_count = min(Counter(y_train).values())

if min_count >= 2:
    # SVM + 확률보정 (교차검증 폴드 수를 안전하게 축소)
    base_clf = LinearSVC()
    safe_cv = max(2, min(3, min_count))  # 2~3 사이에서 최소샘플 수에 맞춰 선택
    model = CalibratedClassifierCV(base_clf, method="sigmoid", cv=safe_cv)
    model.fit(X_vec, y_train)
else:
    # 클래스가 1샘플인 것이 있어서 CV 불가 → 로지스틱 회귀로 폴백
    model = LogisticRegression(max_iter=1000, multi_class="auto")
    model.fit(X_vec, y_train)


# —— (5) 키워드 룰(고신뢰 우선 매칭)
KEYWORD_RULES = [
    (re.compile(r"(안녕|반가|hello|hi|ㅎㅇ|하이)"), "인사"),
    (re.compile(r"(몇\s*시|현재\s*시(간|각)|지금\s*몇\s*시)"), "시간"),
    (re.compile(r"(주문|주문\s*조회|배송|운송장|출고)"), "주문_조회"),
    (re.compile(r"(반품|회수|수거\s*요청|수거\s*신청)"), "반품_신청"),
    (re.compile(r"(교환)"), "교환_신청"),
    (re.compile(r"(취소)"), "취소_신청"),
    (re.compile(r"(수거).*(변경|예약|일정)"), "수거_변경"),
    (re.compile(r"(사이즈|치수|크기|정사이즈|작나요|크나요)"), "사이즈"),
    (re.compile(r"(환불|카드.?취소|환급)"), "환불"),
    (re.compile(r"(쿠폰|적립금|포인트|할인)"), "혜택"),
    (re.compile(r"(고마워|감사|thanks?)"), "감사"),
]

# (선택) 라벨별 임계치 미세조정
LABEL_THRESH = {
    "인사": 0.35, "감사": 0.4, "시간": 0.5,  # 짧은 표현은 낮게
    # 나머지는 전역 BOT_THRESHOLD 사용
}
# 사람 채팅 템플릿
INTENT_RESPONSES = {
    "주문_조회": {
        "msg": "주문 조회는 마이페이지 > 주문/배송 조회에서 확인하실 수 있어요. 주문번호로 검색도 가능합니다.",
        "route": "order.lookup",
        "suggestions": ["반품 신청", "교환 신청", "환불 안내"]
    },
    "반품_신청": {
        "msg": "반품은 주문 상세 화면의 [반품 신청] 버튼으로 접수해 주세요. 수거 주소와 사유를 선택하시면 됩니다.",
        "route": "after_sales.return.create",
        "suggestions": ["수거 일정 변경", "환불 안내", "교환 신청"]
    },
    "교환_신청": {
        "msg": "교환은 주문 상세 > [교환 신청]에서 진행돼요. 사이즈/색상 선택 후 택배 수거를 예약할 수 있습니다.",
        "route": "after_sales.exchange.create",
        "suggestions": ["주문 조회", "수거 일정 변경", "반품 신청"]
    },
    "취소_신청": {
        "msg": "출고 전 주문은 주문 상세 화면의 [주문 취소]로 즉시 취소됩니다. 출고 후에는 반품 절차로 안내돼요.",
        "route": "after_sales.cancel.create",
        "suggestions": ["주문 조회", "환불 안내"]
    },
    "수거_변경": {
        "msg": "수거 일정은 마이페이지 > 취소/반품/교환 목록에서 해당 요청의 [일정 변경]으로 수정할 수 있어요.",
        "route": "after_sales.pickup.reschedule",
        "suggestions": ["반품 신청", "교환 신청"]
    },
    "사이즈": {
        "msg": "해당 상품의 사이즈 가이드는 상품 상세의 [사이즈 가이드]를 확인해 주세요. 보통 정사이즈 기준입니다.",
        "route": "product.size_guide",
        "suggestions": ["주문 조회", "교환 신청"]
    },
    "환불": {
        "msg": "환불은 반품 회수 및 검수 완료 후 2~3영업일 내 결제수단으로 자동 처리됩니다. 카드 취소는 카드사 정책에 따릅니다.",
        "route": "policy.refund",
        "suggestions": ["반품 신청", "주문 조회"]
    },
    "혜택": {
        "msg": "쿠폰/적립금은 결제 단계에서 적용할 수 있어요. 마이페이지 > 쿠폰함/적립금에서 보유 내역을 확인하세요.",
        "route": "benefit.help",
        "suggestions": ["주문 조회", "환불 안내"]
    }
}
# == [추가] 도움말(명령어) 메시지 생성기 ==
def build_help_message() -> str:
    lines = []
    lines.append("✅ 사용 가능한 명령어(키워드) 안내")
    lines.append("")
    lines.append("• 상담 전환")
    lines.append("  - '상담원' : 사람 상담으로 전환")
    lines.append("  - '봇'     : 챗봇 모드로 전환")
    lines.append("")
    lines.append("  - '시간' : 현재 시간 알려줘")
    lines.append("")
    lines.append("  - '주문 조회', '배송 어디야', '운송장 번호'")
    lines.append("")
    lines.append("  - '취소 신청', '반품 신청', '교환 신청', '수거 일정 변경'")
    lines.append("")
    lines.append("  - '사이즈', '정사이즈인가요', '환불', '환불 정책', '쿠폰', '적립금', '포인트'")
    lines.append("")
    lines.append("예) '반품 신청하고 싶어요', '교환 요청', '쿠폰 사용 방법 알려줘'")
    return "\n".join(lines)
DB_RESPONSES = load_intent_responses() or INTENT_RESPONSES

def generate_response(label: str, text: str):
    if label == "인사":
        return {"msg": "안녕하세요! 무엇을 도와드릴까요?"}
    if label == "시간":
        now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        return {"msg": f"현재 시간은 {now}입니다."}
    if label == "감사":
        return {"msg": "천만에요! 도움이 되어 기쁩니다 😊"}

    data = DB_RESPONSES.get(label) or INTENT_RESPONSES.get(label)
    if data:
        return {"msg": data["msg"], "route": data.get("route"), "suggestions": data.get("suggestions")}

    return {"msg": "죄송해요, 아직 그건 잘 몰라요."}

def predict_with_conf(text: str):
    t = normalize_text(text)
    # == [추가] 도움말/명령어 트리거 ==
    if t in ("명령어", "도움말", "/help", "help", "/명령어"):
        help_msg = build_help_message()
        return "시스템", {"msg": help_msg, "route": "system.help", "suggestions": ["주문 조회", "반품 신청", "환불 안내", "상담원"]}, 1.0
    
    # 안전망
    if t in ("봇", "bot", "챗봇"):
        return "시스템", {"msg":"봇 모드 전환 요청입니다."}, 1.0
    if t in ("상담원", "사람", "상담사", "오퍼레이터"):
        return "미매칭", {"msg":"상담원 연결을 도와드릴게요. 잠시만 기다려주세요."}, 1.0
    if t in ("시간", "현재 시간", "현재시간", "시각", "지금 시간", "time"):
        return "시간", {"msg": f"현재 시간은 {datetime.now():%Y-%m-%d %H:%M:%S}입니다."}, 0.99

    # 룰
    for pat, lab in KEYWORD_RULES:
        if pat.search(t):
            return lab, generate_response(lab, t), 0.97

    # 모델
    vec   = features.transform([t])
    lab   = model.predict(vec)[0]
    proba = float(max(model.predict_proba(vec)[0]))

    thr = LABEL_THRESH.get(lab, BOT_THRESHOLD)
    if proba < thr:
        return "미매칭", {"msg":"상담원 연결을 도와드릴게요. 잠시만 기다려주세요."}, proba

    return lab, generate_response(lab, t), proba
# ---------------------
# 스키마
# ---------------------
from typing import List, Optional
class InferReq(BaseModel):
    text: str
    sessionId: Optional[str] = "anon"

class InferRes(BaseModel):
    label: str
    reply: str
    confidence: float
    sessionId: str
    route: Optional[str] = None
    suggestions: Optional[List[str]] = None

# ---------------------
# 엔드포인트
# ---------------------
@app.get("/healthz")
def health():
    return {"ok": True}

@app.post("/infer", response_model=InferRes, dependencies=[Depends(require_api_key)])
def infer(req: InferReq):
    if not req.text or not req.text.strip():
        raise HTTPException(status_code=400, detail="text is required")

    label, payload, conf = predict_with_conf(req.text)   # payload = {"msg":..., "route":..., "suggestions":[...]}
    reply = payload.get("msg", "...")
    route = payload.get("route")
    suggestions = payload.get("suggestions")

    log.info(f"[infer] sid={req.sessionId} label={label} conf={conf:.3f} text={req.text}")
    return InferRes(
        label=label,
        reply=reply,
        confidence=conf,
        sessionId=req.sessionId,
        route=route,
        suggestions=suggestions
    )