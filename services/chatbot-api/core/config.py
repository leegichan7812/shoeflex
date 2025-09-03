import os
from pydantic import BaseSettings

class Settings(BaseSettings):
    BOT_SECRET: str = "dev-secret"
    BOT_THRESHOLD: float = 0.55
    ALLOW_ORIGINS: str = "http://localhost:8080"

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"

settings = Settings()