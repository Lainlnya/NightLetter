from sqlalchemy import Column, Integer, String, JSON
from pydantic import BaseModel
from . import db

class DiaryTable(db.Base):
    __tablename__='diary'
    diary_id = Column(Integer, primary_key=True, autoincrement=True)
    type = Column(String)
    vector = Column(JSON)
    content = Column(String)

class Diary(BaseModel):
    diary_id :int
    type :str
    vector :dict

class Tarot(BaseModel):
    id :int
    keywords :list
