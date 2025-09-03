from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
import os, oracledb

router = APIRouter()

# 커넥션 풀(간단 버전: 요청마다 연결/해제도 OK)
dsn = oracledb.makedsn(
    os.getenv("DB_HOST"), int(os.getenv("DB_PORT","1521")),
    service_name=os.getenv("DB_SERVICE","XE")
)

class RecItem(BaseModel):
    product_id: int
    rank_order: int
    score: float

@router.get("/recommendations", response_model=list[RecItem])
def get_recs(user_id: int):
    try:
        with oracledb.connect(user=os.getenv("DB_USER"),
                              password=os.getenv("DB_PASSWORD"),
                              dsn=dsn) as con:
            with con.cursor() as cur:
                cur.execute("""
                    SELECT product_id, rank_order, score
                    FROM USER_RECOMMENDATIONS
                    WHERE user_id = :1
                    ORDER BY rank_order
                """, [user_id])
                rows = cur.fetchall()
        return [RecItem(product_id=int(pid), rank_order=int(rk), score=float(sc))
                for (pid, rk, sc) in rows]
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
