# chatbot-api/utils_route.py
import re
import urllib.parse

# <ROUTE:after_sales.exchange.create|req_type=교환&status=신청>
ROUTE_RE = re.compile(r"<ROUTE:([a-z0-9_.\-]+)(?:\|([^>]+))?>", re.I)

def strip_route(text: str) -> str:
    """화면 노출 전 ROUTE 토큰만 깔끔히 제거"""
    return ROUTE_RE.sub("", text or "").strip()

def parse_route(text: str):
    """
    ROUTE 토큰을 이벤트로 분리해 쓰고 싶을 때 사용.
    반환: (clean_text, {"name":..., "params":{...}}) 또는 (text, None)
    """
    m = ROUTE_RE.search(text or "")
    if not m:
        return text, None

    name = m.group(1)
    qs   = m.group(2) or ""
    params = {k: v[0] for k, v in urllib.parse.parse_qs(qs, keep_blank_values=True).items()}
    clean = ROUTE_RE.sub("", text).strip()
    return clean, {"name": name, "params": params}
