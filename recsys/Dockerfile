FROM python:3.9

WORKDIR /app

RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

RUN wget https://storage.googleapis.com/tensorflow/linux/cpu/tensorflow_cpu-2.15.0-cp39-cp39-manylinux_2_17_x86_64.manylinux2014_x86_64.whl
RUN pip install --no-cache-dir tensorflow_cpu-2.15.0-cp39-cp39-manylinux_2_17_x86_64.manylinux2014_x86_64.whl

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
COPY kosbert-klue-bert-base /app/kosbert-klue-bert-base

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
