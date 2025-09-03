import re
from datetime import datetime
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.svm import SVC

def normalize_text(t: str) -> str:
    t = (t or "").strip()
    t = re.sub(r"\s+", " ", t)
    synonyms = {"환불  정책":"환불 정책","교환 요청":"교환 신청","반품신청":"반품 신청"}
    for s,tgt in synonyms.items(): t = t.replace(s, tgt)
    return t

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

X_train_norm = [normalize_text(x) for x in X_train]
vectorizer = TfidfVectorizer(ngram_range=(1, 2))
X_vec = vectorizer.fit_transform(X_train_norm)
model = SVC(kernel="linear", probability=True)
model.fit(X_vec, y_train)

def generate_response(label: str, text: str) -> str:
    if label == "인사":
        return "안녕하세요! 무엇을 도와드릴까요?"
    if label == "시간":
        from datetime import datetime
        return f"현재 시간은 {datetime.now():%Y-%m-%d %H:%M:%S}입니다."
    if label == "감사":
        return "천만에요! 도움이 되어 기쁩니다 😊"
    routes = {
        "주문_조회":"order.lookup",
        "반품_신청":"after_sales.return.create",
        "교환_신청":"after_sales.exchange.create",
        "취소_신청":"after_sales.cancel.create",
        "수거_변경":"after_sales.pickup.reschedule",
        "사이즈":"product.size_guide",
        "환불":"policy.refund",
        "혜택":"benefit.help",
    }
    if label in routes:
        return f"<ROUTE:{routes[label]}> {text}"
    return "죄송해요, 아직 그건 잘 몰라요."

def predict_with_conf(text: str, threshold: float):
    t = normalize_text(text)
    vec = vectorizer.transform([t])
    label = model.predict(vec)[0]
    proba = float(max(model.predict_proba(vec)[0]))
    if proba < threshold:
        return "미매칭", "상담원 연결을 도와드릴게요. 잠시만 기다려주세요.", proba
    return label, generate_response(label, t), proba