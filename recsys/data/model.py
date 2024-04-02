from pydantic import BaseModel
from sqlalchemy import Column, Integer, String, JSON
from typing import List

from . import db


class DiaryTable(db.Base):
    __tablename__ = 'diary'
    diary_id = Column(Integer, primary_key=True, autoincrement=True)
    type = Column(String)
    vector = Column(JSON)
    content = Column(String)


class Diary(BaseModel):
    diary_id: int
    type: str
    vector: dict


class DiaryRequest(BaseModel):
    content: str


class Tarot(BaseModel):
    id: int
    keywords: str


class TarotInput(BaseModel):
    tarots: List[Tarot]


class VectorEmbed(BaseModel):
    embed: List[float]


class TarotVector(BaseModel):
    id: int
    keywords: List[VectorEmbed]


class TarotVectors(BaseModel):
    tarots: List[TarotVector]
