# 실행 위치를 서비스 폴더로
Set-Location C:\a01_javaexp\sp_workspace\prj3\services\chatbot-api

# 가상환경
if (-not (Test-Path .\.venv)) { python -m venv .venv }
.\.venv\Scripts\Activate.ps1

# 의존성
python -m pip install --upgrade pip
pip install -r requirements.txt

# 환경변수(원하면 .env 사용 가능)
$env:BOT_SECRET = "my-secret"
$env:BOT_THRESHOLD = "0.55"
$env:ALLOW_ORIGINS = "http://localhost:8080"

# 서버 실행 (포트: 8002)
python -m uvicorn app_chatbot:app --host 0.0.0.0 --port 8002 --reload


# Set-Location C:\a01_javaexp\sp_workspace\prj3\services\chatbot-api