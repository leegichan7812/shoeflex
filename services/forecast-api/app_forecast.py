from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
import warnings
import numpy as np
import pandas as pd
from numpy import log1p, expm1
from statsmodels.tsa.statespace.sarimax import SARIMAX
from statsmodels.tsa.holtwinters import ExponentialSmoothing
from statsmodels.tools.sm_exceptions import ConvergenceWarning

app = FastAPI(title="Withdrawal Forecast API")

# ---------- I/O 모델 ----------
class Point(BaseModel):
    week: str
    count: float

class SeriesIn(BaseModel):
    reason: str
    points: List[Point]

class ForecastReq(BaseModel):
    horizon: int = 4
    series: List[SeriesIn]

# ---------- 예측 유틸 ----------
def _arima_forecast_positive(y: pd.Series, h: int) -> np.ndarray | None:
    """작은 그리드 SARIMAX로 h스텝 예측. 양수 보장. 수렴 실패 시 None."""
    if len(y) < 8 or float(y.sum()) <= 0:
        return None
    y_log = log1p(y)

    best, best_aic = None, np.inf
    for p in (0, 1, 2):
        for d in (0, 1):
            for q in (0, 1, 2):
                try:
                    with warnings.catch_warnings():
                        # 🔇 수렴 + 시작값 관련 모든 경고 숨김
                        warnings.simplefilter("ignore", ConvergenceWarning)
                        warnings.simplefilter("ignore", UserWarning)

                        m = SARIMAX(
                            y_log,
                            order=(p, d, q),
                            trend="t",
                            enforce_stationarity=True,
                            enforce_invertibility=True
                        ).fit(disp=False, maxiter=300, method="lbfgs")

                    # 수렴 못 하면 스킵
                    if not m.mle_retvals.get("converged", False):
                        continue

                    if m.aic < best_aic:
                        best, best_aic = m, m.aic
                except Exception:
                    continue

    if best is None:
        return None

    fc = expm1(best.forecast(h).to_numpy())
    fc = np.where(np.isfinite(fc), fc, 0.0)
    return np.clip(fc, 0, None)

def _ets_forecast_positive(y: pd.Series, h: int) -> np.ndarray | None:
    """Holt(감쇠추세) + log 변환으로 양수 예측. 실패 시 None."""
    if len(y) < 3 or float(y.sum()) <= 0:
        return None
    y_log = log1p(y)
    try:
        with warnings.catch_warnings():
            warnings.simplefilter("ignore", ConvergenceWarning)
            m = ExponentialSmoothing(
                y_log, trend="add", damped_trend=True,
                seasonal=None, initialization_method="estimated"
            ).fit(optimized=True, use_brute=True)
        fc = expm1(m.forecast(h).to_numpy())
        fc = np.where(np.isfinite(fc), fc, 0.0)
        return np.clip(fc, 0, None)
    except Exception:
        return None

def _fallback_mean_slope(y: pd.Series, h: int) -> np.ndarray:
    nz = y[y > 0]
    if len(nz) >= 2:
        tail = nz.tail(min(8, len(nz))).to_numpy(dtype=float)
        mean = float(np.mean(tail))
        slope = float((tail[-1] - tail[0]) / max(len(tail) - 1, 1))
        return np.array([max(0.0, mean + slope*i) for i in range(1, h+1)], dtype=float)
    if len(nz) == 1:
        v = float(nz.iloc[-1])
        return np.array([v]*h, dtype=float)
    return np.zeros(h, dtype=float)


# ---------- 엔드포인트 ----------
@app.get("/health")
def health():
    return {"ok": True}

@app.post("/forecast/withdrawal")
def forecast(req: ForecastReq):
    results = []
    h = req.horizon

    for s in req.series:
        # 입력 시계열 정리
        df = pd.DataFrame([p.model_dump() for p in s.points])
        if df.empty:
            results.append({"reason": s.reason, "forecast": []})
            continue

        df["week"] = pd.to_datetime(df["week"])
        df = df.sort_values("week")
        y = df["count"].astype(float).reset_index(drop=True)

        # 1) SARIMAX → 2) ETS → 3) 평균+기울기 폴백
        fc = _arima_forecast_positive(y, h)
        if fc is None:
            fc = _ets_forecast_positive(y, h)
        if fc is None:
            fc = _fallback_mean_slope(y, h)

        # 🔧 [수정 1] 응답/반올림 전에 sanitize + 바닥값 적용
        fc = np.asarray(fc, dtype=float)
        fc[~np.isfinite(fc)] = 0.0
        fc = np.clip(fc, 0, None)

        # 모든 값이 0(또는 거의 0)인데 과거에 한 번이라도 발생했다면 바닥값 깔기
        if float(y.sum()) > 0 and (fc <= 1e-9).all():
            nz = y[y > 0]
            base = float(nz.tail(min(6, len(nz))).mean()) if len(nz) else float(y.mean())
            floor = max(0.3, 0.15 * base)   # ← 필요 시 조정 (최소 0.3 or 최근 평균의 15%)
            fc = np.full(h, floor, dtype=float)

        # 🔧 [수정 2] 바닥값 적용 '후'에 반올림(권장: 소수 2자리 유지)
        fc_vals = np.round(fc, 2).tolist()   # ← 1자리 대신 2자리(또는 반올림 제거해도 됨)

        # 미래 주(월요일) 생성
        fut_weeks = pd.date_range(df["week"].max() + pd.Timedelta(days=7),
                                  periods=h, freq="W-MON")

        # 🔧 [수정 3] 응답에 'fc_vals'(바닥값/반올림 적용 후)를 넣기
        results.append({
            "reason": s.reason,
            "forecast": [{"week": w.strftime("%Y-%m-%d"), "count": v}
                         for w, v in zip(fut_weeks, fc_vals)]
        })

        # 디버그: 최종 내려가는 값 확인(반올림 후 값)
        print(f"[DBG] reason={s.reason}, last8={y.tail(8).tolist()}, fc_out={fc_vals}")

    return {"horizon": h, "results": results}
