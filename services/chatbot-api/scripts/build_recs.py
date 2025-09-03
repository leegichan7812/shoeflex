# scripts/build_recs.py
import os, warnings
from dotenv import load_dotenv
load_dotenv()  # chatbot-api/.env

import oracledb
# ... (나머지: pandas, numpy, implicit, sklearn 등 기존 코드 그대로)

ORACLE_MODE = os.getenv("ORACLE_MODE", "thin").lower()
if ORACLE_MODE == "thick":
    lib_dir = os.getenv("ORACLE_CLIENT_LIB_DIR")
    if lib_dir:
        oracledb.init_oracle_client(lib_dir=lib_dir)

dsn = oracledb.makedsn(
    os.getenv("DB_HOST"), int(os.getenv("DB_PORT","1521")),
    service_name=os.getenv("DB_SERVICE","XE")
)
con = oracledb.connect(user=os.getenv("DB_USER"), password=os.getenv("DB_PASSWORD"), dsn=dsn)
# 이후 쿼리/ALS 학습/USER_RECOMMENDATIONS 적재 로직 그대로…
