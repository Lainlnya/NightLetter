from sqlalchemy import Column, Integer, String, JSON
from pydantic import BaseModel
from . import db
from typing import List


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


class Tarot(BaseModel):
    id: int
    keywords: str

class TarotInput(BaseModel):
    tarots: List[Tarot]


class TarotVector(BaseModel):
    id: int
    keywords: List[List[float]]


class TarotVectors(BaseModel):
    tarots: List[TarotVector]