import os
from annoy import AnnoyIndex
from fastapi import FastAPI
from sentence_transformers import SentenceTransformer
from starlette.middleware.cors import CORSMiddleware

from data import model
from data import text_preprocessing
from data.db import session
from data.model import DiaryTable

app = FastAPI()

origins = [
    "http://letter-for.me:8081",
    "https://letter-for.me",
    "http://localhost",
    "http://localhost:8081",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

embedder = SentenceTransformer(os.getcwd() + '/kosbert-klue-bert-base')


@app.get("/health-check")
async def root():
    return {"message": "Hello World"}


@app.post("/diaries/init")
async def init(diaryRequest: model.DiaryRequest):
    content = text_preprocessing.preprocessing(diaryRequest.content)
    vector = embedder.encode(content)
    return {"embed": vector.tolist()}


@app.post("/diaries/recommend")
def get_rec(diaryRequest: model.DiaryRequest):
    # 자연어 전처리
    content = text_preprocessing.preprocessing(diaryRequest.content)
    # 벡터화
    vector = embedder.encode(content)

    # 유사한 일기 추천받기
    tree = AnnoyIndex(768, 'angular')
    tree.load('tree.ann')

    print(" 비슷한 일기 : ", tree.get_nns_by_vector(vector.tolist(), 10))

    return {"embedVector": {"embed": vector.tolist()},
            "diariesId": tree.get_nns_by_vector(vector.tolist(), 10)}


@app.post("/diaries/tree")
def build_model():
    public_lst = []

    # data 읽어서 model build
    diarys = session.query(DiaryTable).all()
    for diary in diarys:
        if diary.type == "PUBLIC" and diary.vector is not None:
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
    print("새로운 트리 저장")
    return {"message": "success tree building"}


@app.post("/tarots/init", response_model=model.TarotVectors)
def get_deck(tarots: model.TarotInput):
    card_lst = []
    for tarot in tarots.tarots:
        card = model.TarotVector(id=tarot.id, keywords=[])
        for keyword in tarot.keywords.split(','):
            keyword_vector = embedder.encode(keyword.strip()).tolist()
            card.keywords.append(model.VectorEmbed(embed=keyword_vector))
        card_lst.append(card)

    return model.TarotVectors(tarots=card_lst)
