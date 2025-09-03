from pydantic import BaseModel
from typing import Optional

class InferReq(BaseModel):
    text: str
    sessionId: Optional[str] = "anon"

class InferRes(BaseModel):
    label: str
    reply: str
    confidence: float
    sessionId: str
