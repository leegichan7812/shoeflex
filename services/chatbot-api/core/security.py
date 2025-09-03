from fastapi import Header, HTTPException, status
from .config import settings

async def require_api_key(x_api_key: str | None = Header(default=None)):
    if x_api_key != settings.BOT_SECRET:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="unauthorized")
