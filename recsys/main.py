from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware
from annoy import AnnoyIndex
import os
from sentence_transformers import SentenceTransformer
from data.db import session
from data.model import DiaryTable
from data import text_preprocessing
from data import model
from typing import List

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

embedder = SentenceTransformer(os.getcwd()+'\kosbert-klue-bert-base')

@app.get("/")
async def root():
    return {"message": "Hello World"}

@app.get("/diaries/recommend")
def get_rec(content: str):
    # 자연어 전처리
    content = text_preprocessing.preprocessing(content)
    # 벡터화
    vector = embedder.encode(content)

    # 유사한 일기 추천받기
    tree = AnnoyIndex(768, 'angular')
    tree.load('tree.ann')

    return {"vector": vector.tolist(),
           "diariesId" : tree.get_nns_by_vector(vector.tolist(), 10)}

@app.post("/diaryies/tree")
def build_model():

    public_lst=[]

    # data 읽어서 model build
    diarys = session.query(DiaryTable).all()
    for diary in diarys:
        if (diary.type=="PUBLIC"):
            public_lst.append([diary.diary_id, diary.vector])

    tree = AnnoyIndex(768, 'angular')
    for id, vector in public_lst:
        tree.add_item(id, vector)

    tree.build(10)

    file_path = 'tree.ann'
    if os.path.exists(file_path):
        print("기존 트리 지우기")
        os.remove(file_path)
    tree.save('tree.ann')
    print("새로운 트리 저장하기")

    return {"message": "success tree building"}

@app.post("/tarot/init")
def get_deck(tarots: List[model.Tarot]):

    card_lst = []

    for tarot in tarots:
        card={}
        card['id'] = tarot.id
        card['keywords'] = []
        for keyword in tarot.keywords:
            # 타로 카드 키워드 벡터화
            card['keywords'].append(embedder.encode(keyword.strip()))

        card_lst.append(card)

    return {"tarot_vectors":card_lst}