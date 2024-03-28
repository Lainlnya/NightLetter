FROM node:latest

WORKDIR /app

COPY ./front-end .

RUN npm install

RUN npm run build

CMD ["npm", "start"]
