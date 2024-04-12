# 포팅 메뉴얼

## 1 사용도구

### 협업 도구

- 이슈 관리 : Jira
- 형상 관리 : GitLab, Git
- 커뮤니케이션 : Notion, Mattermost

### 배포 도구

- CI/CD : Jenkins, Docker, ECS

### 설계 도구

- 와이어프레임 : Figma
- ERD : ERD Cloud
- 요구사항 정의서 : Google Sheets
- 기능 명세서 : Google Sheets
- 간트차트 : Google Sheets

### 개발 도구

- Visual Studio Code
- Intellij : 2023.3.2 (Ultimate Edition)

---

## 2 개발 환경

### 2.1 Frontend

```json
  "dependencies": {
    "@tanstack/react-query": "^5.28.2",
    "axios": "^1.6.7",
    "framer-motion": "^11.0.13",
    "moment": "^2.30.1",
    "next": "14.1.3",
    "next-compose-plugins": "^2.2.1",
    "react": "^18",
    "react-calendar": "^4.8.0",
    "react-dom": "^18",
    "react-error-boundary": "^4.0.13",
    "react-spinners": "^0.13.8",
    "sharp": "^0.33.3",
    "zustand": "^4.5.2"
  },
  "devDependencies": {
    "@mswjs/http-middleware": "^0.9.2",
    "@tanstack/react-query-devtools": "^5.28.6",
    "@types/cors": "^2.8.17",
    "@types/express": "^4.17.21",
    "@types/node": "^20",
    "@types/react": "^18",
    "@types/react-dom": "^18",
    "cors": "^2.8.5",
    "eslint": "^8",
    "eslint-config-next": "14.1.3",
    "express": "^4.19.2",
    "msw": "^2.2.13",
    "sass": "^1.72.0",
    "typescript": "^5",
    "typescript-plugin-css-modules": "^5.1.0",
    "webpack": "^5.90.3"
  }
```

### 2.2 Backend

| 프로그램          | 버전                                  |
| ----------------- | ------------------------------------- |
| jvm               | openjdk version "17.0.8.1" 2023-08-24 |
| gradle            | 8.6                                   |
| spring boot       | 3.2.3                                 |
| Spring            | 6.1.4                                 |
| spring security   | 6.2.1                                 |
| jpa               | 6.4.1.FINAL                           |
| querydsl          | 5.0.0                                 |
| mysql connector-j | 8.3.0                                 |
| lettuce           | 6.3.2.RELEASE                         |
| spring batch      | 5.1.0                                 |
| spring cache      | 3.2.2                                 |
| jjwt              | 0.11.2                                |
| spring cloud aws  | 2.2.6.RELEASE                         |
| nurigo sdk        | 4.2.7                                 |

### 2.3 Server

| 프로그램         | 버전                                  |
| ---------------- | ------------------------------------- |
| Amazon Lightsail | CPU : 4vCPUs, RAM : 16GB, OS : Ubuntu |
| AWS S3           | S3 Standard                           |

### 2.4 Service

| 프로그램 | 버전                  |
| -------- | --------------------- |
| Ubuntu   | Ubuntu 20.04.6 LTS    |
| Docker   | 23.0.4                |
| Jenkins  | 2.440.1               |
| MySQL    | MySQL Community 8.3.0 |
| Redis    | Redis 7.2.4           |
| Nginx    | nginx/1.25.4          |

---

## 3 환경 변수

### 3.1 Frontend

```
NEXT_PUBLIC_MODE='local'
NEXT_PUBLIC_URL='https://letter-for.me'
NEXT_PUBLIC_API_URL='https://letter-for.me/api/v1'
```

### 3.2 Backend

- spring boot : application.yml (/src/main/resources에 위치)

```yml
server:
  port: 8081

spring:
  security:
    oauth2:
      client:
        registration: # 카카오 API 사용하기 위한 인증 정보
          kakao:
            client-id: ${KAKAO_LOGIN_RESTAPI_KEY}
            client-secret: ${KAKAO_LOGIN_CLIENT_SECRET}
            redirect-uri: ${KAKAO_LOGIN_REDIRECT_URI}
            scope: # 카카오 API에서 받아올 정보
              - profile_nickname
              - profile_image
              - account_email
            authorization-grant-type: authorization_code
            client-authentication-method: client_secret_post
        provider:
          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: id
    provider:
      response-uri:
        kakao: ${KAKAO_TOKEN_RESPONSE_URI}

  datasource: # MySQL 컨테이너 연결 정보
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${MYSQL_URL}
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}
  jpa:
    properties:
      hibernate:
        jdbc:
          time_zone: Asia/Seoul # 시간대 설정
        dialect=org:
          hibernate:
            dialect:
              MySQL5Dialect:
        transaction:
          jta:
            platform=org:
              hibernate:
                service:
                  jta:
                    platform:
                      internal:
                        JBossStandAloneJtaPlatform:
    hibernate:
      ddl-auto: none
    show-sql: true
  data: # Redis 컨테이너 연결 정보
    redis:
      host: ${REDIS_HOSTNAME}
      port: ${REDIS_PORT}

jwt: # JWT 암호화 Key
  secret-key: ${JWT_SECRET_KEY}
```

```
KAKAO_LOGIN_RESTAPI_KEY=''
KAKAO_LOGIN_CLIENT_SECRET=''
MYSQL_USERNAME='root'
MYSQL_PASSWORD='dltmdgjs'
MYSQL_URL='jdbc:mysql://mysql-container:3306/letterforme'
MYSQL_HOST ='j10a108.p.ssafy.io'
MYSQL_DATABASE ='letterforme'

JWT_SECRET_KEY=''
KAKAO_LOGIN_REDIRECT_URI='{baseUrl}/oauth2/callback/{registrationId}'
KAKAO_TOKEN_RESPONSE_URI='https://localhost:3001/auth/oauth-response'
KAKAO_LOCAL_TOKEN_RESPONSE_URI='https://localhost:3001/auth/oauth-response'
KAKAO_LOCAL_TOKEN=''

COOKIE_DOMAIN='localhost'
OPENAI_MODEL= 'ft:gpt-3.5-turbo-0125:personal::99mAOVqt'
OPENAI_SECRET_KEY=''
REDIS_HOSTNAME='redis-container'
REDIS_PORT='6379'
```

---

## 4 배포 및 서비스 실행

### 4.1 배포 포트

BACK-END

- spring boot application : 내부 port 8081(port 노출 없이 upstream 연결)

FRONT-END

- next.js : 내부 port 3000(port 노출 없이 upstream 연결)

DATA

- mysql : 3306:3306
- redis : 6379:6379
- fastapi : 8000:8000

DEPLOY

- Nginx : 80:443
- jenkins : 9090:8080

### 4.2 배포 과정

1. 서버 세팅

```bash
# 서버시간 변경
$ sudo timedatectl set-timezone Asia/Seoul

# 변경한 서버 시간 올바른지 확인o
$ date
Sat Jan 27 18:10:22 KST 2024

# 미러 서버 변경
$ sudo vim /etc/apt/sources.list

# 위에서 열어준 파일의 ~.ubuntu.com(서버경로)를 모두 변경
sudo sed -i 's/ap-northeast-2.ec2.archive.ubuntu.com/mirror.kakao.com/g' /etc/apt/sources.list

# 패키지 업그레이드 및 업데이트
$ sudo apt update
$ sudo apt upgrade

# 가상 메모리 할당
# 현재 메모리 용량 확인(할당전 - swap 영억이 모두 0)
$ free -h

# swap 메모리 할당
$ sudo fallocate -l 8G /swapfile

# 스왑 파일에 대한 읽기 및 쓰기 권한 업데이트
$ sudo chmod 600 /swapfile

# Linux 스왑 영역 설정
$ sudo mkswap /swapfile

# 스왑 공간에 스왑 파일을 추가하여 스왑 파일을 즉시 사용할 수 있도록 만듦
$ sudo swapon /swapfile

# 절차 성공 여부 확인
$ sudo swapon -s

# /etc/fstab 파일을 편집하여 부팅시 스왑 파일을 활성화
$ sudo vi /etc/fstab

# 편집기로 연 파일에 아래 한줄 추가
/swapfile swap swap defaults 0 0

# 할당 후 메모리 확인
$ free -h
```

2. Docker 설치

```bash
# Docker Repository 등록 및 docker-ce 패키지 설치
$ sudo apt-get update
$ sudo apt-get install ca-certificates curl
$ sudo install -m 0755 -d /etc/apt/keyrings
$ sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
$ sudo chmod a+r /etc/apt/keyrings/docker.asc

$ echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
$ sudo apt-get update
$ sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# docker 버전 확인
$ docker --version

# docker-engine 설치 확인
$ sudo docker run hello-world

# sudo 없이 docker 명령어 사용하기(선택 사항)
# 현재 사용자(ubuntu)를 docker group 에 포함
$ adduser ubuntu
$ sudo usermod -aG docker ubuntu

# 터미널 재실행 후 확인(끝에 docker 가 있는지 확인)
$ id -nG
ubuntu adm dialout cdrom floppy sudo audio dip video plugdev netdev lxd docker

# 네트워크 생성 - 모든 컨테이너의 port를 노출시키지 않으려면 docker 내에서 생성한 네트워크로 컨테이너 간 연결 및 통신해야 한다.
$ docker network create tarot-network
```

3. DB 컨테이너 생성

1) MySQL 컨테이너

```
$ docker run --name mysql-container -p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=dltmdgjs -e MYSQL_DATABASE=mydb -d mysql
```

2. Redis 컨테이너

```
$ docker run --name redis-container -v /home/ubuntu/redis-storage:/data --memory=512m --memory-swap=1g -d --network tarot-network redis
```

4. Jenkins 설정

- jenkins 컨테이너 생성

```bash
# jenkins 이미지 받기
$ docker pull jenkins/jenkins:lts
# 확인
$ docker images

# --env JENKINS_OPTS="--prefix=/jenkins" (리버스 프록시 설정한 도메인 뒤에 주소 설정 ex. letter-for.me/jenkins)
# -v /var/run/docker.sock:/var/run/docker.sock (Docker out of docker 설정 - jenkins 컨테이너 내부에서 서버 local의 도커 소켓을 마운트하여 컨테이너 내부에서 서버 로컬 도커를 사용)
$ docker run --name jenkins-container -d -p 9090:8080 -p 50000:50000 --env JENKINS_OPTS="--prefix=/jenkins" -v /home/ubuntu/jenkins:/var/jenkins_home -v /home/ubuntu/.ssh:/root/.ssh -v /var/run/docker.sock:/var/run/docker.sock -u root --network tarot-network jenkins/jenkins:lts

```

- jenkins 세팅

1. jenkins 컨테이너 실행 로그 내 초기 비밀번호

```bash
$ docker logs jenkins-container

# Please use the following password to proceed to installation:
# 문장 하단에 초기 비밀번호
```

2. 인터넷에 j10a108.p.ssafy.io:9090/ 접속 → 초기 비밀번호 입력

3. jenkins 초기 설정

- 좌측 버튼(추천 플러그인 설치)과 우측 버튼(선택해서 설치) 중 좌측 버튼(계정명 / 암호 / 이름 / 이메일 입력)
- 추가 플러그인 설치
  - GitLab, Generic Webhook Trigger, Post build task, Docker, Docker Commons, Docker Pipeline, Docker API, SSH Agent

4. jenkins 컨테이너 내 docker 설치
   - 컨테이너를 생성할 때 도커 소켓을 마운트했기 때문에 내부에서 생성된 컨테이너가 서버 로컬에서 생성 및 실행됨

```bash
$ docker exec -it jenkins-container bash
# apt-get update
# apt-get install vim
# apt-get install wget
# apt install apt-transport-https ca-certificates curl software-properties-common
# wget -qO- https://get.docker.com/ | sh
# apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

5. gitlab과 jenkins 연결

- gitlab 내 access token 생성

- jenkins credentials 생성

  - credentials > global > add credentials
  - credentials 설정
    - Kind: GitLab API token
    - Scope: Global
    - API token: ${위 gitlab에서 생성한 access token}
    - ID: gitlabAccessToken

- 연결
  - jenkins > system 설정
  - Connection name : gitlab
  - GitLab host URL : https://lab.ssafy.com/
  - Credentials : gitlabAccessToken

6. pipeline 생성

- 개발용 FE, 개발용 BE, 배포용(BE + FE) 파이프라인

- create a job - letterforme - pipeline
- build triggers의 build when a change ✅
- 시크릿키 받기 → f015e0153ed6c54f465d01a733cb3903

- create a job - letterforme-frontdev- pipeline
- build triggers의 build when a change ✅
- 시크릿키 받기 → e4debdbcddb6fea3bc9af53bab06df76

- create a job - letterforme-backdev - pipeline
- build triggers의 build when a change ✅
- 시크릿키 받기 → 471fb18e4b58c270c320c26d5d171602

7. gitlab webhooks 설정
   > 하단 url은 jenkins pipeline 설정에 있음.

- url : https://letter-for.me/jenkins/project/letterforme

  - pipeline 시크릿키 입력
  - push event ✅
  - Regular expression - release

- url : https://letter-for.me/jenkins/project/letterforme-frontdev

  - pipeline 시크릿키 입력
  - push event ✅
  - Regular expression - dev-front

- url : https://letter-for.me/jenkins/project/letterforme-backdev

  - pipeline 시크릿키 입력
  - push event ✅
  - Regular expression - develop

- test의 push test

8. 자동 배포

- backend

  - develop branch에 push 시 실행
  - Dockerfile(devbackDockerfile 경로 최상단에 위치)

  ```docker
  # 첫 번째 스테이지: Gradle을 사용하여 Java 애플리케이션을 빌드
  FROM gradle:8.7-jdk17-alpine AS BUILD

  WORKDIR /home/gradle/src/

  COPY --chown=gradle:gradle ./back-end .

  # gradlew 스크립트에 실행 권한 부여
  RUN chmod +x ./gradlew

  # Gradle Wrapper를 사용하여 프로젝트를 clean 후 build, 데몬 사용하지 않는 옵션
  RUN ./gradlew clean build --no-daemon

  # 두 번째 스테이지: 빌드된 JAR 파일을 실행하기 위한 새로운 이미지
  FROM eclipse-temurin:17-jdk-alpine

  # 첫 번째 스테이지(BUILD)에서 빌드된 JAR 파일을 현재 스테이지의 /app.jar로 복사
  COPY --from=BUILD /home/gradle/src//build/libs/*.jar  /app.jar

  # 컨테이너 시작 시, 자바 애플리케이션 실행을 위한 ENTRYPOINT 지정 및 /app.jar 실행
  ENTRYPOINT ["java","-jar","/app.jar"]
  ```

  - letterforme-backdev pipeline

  ```
  pipeline{
      agent any
      environment {
          KAKAO_LOGIN_RESTAPI_KEY=''
          KAKAO_LOGIN_CLIENT_SECRET=''
          MYSQL_USERNAME='root'
          MYSQL_PASSWORD='dltmdgjs'
          MYSQL_URL='jdbc:mysql://mysql-container:3306/letterforme'
          MYSQL_HOST ='j10a108.p.ssafy.io'
          MYSQL_DATABASE ='letterforme'

          JWT_SECRET_KEY=''
          KAKAO_LOGIN_REDIRECT_URI='{baseUrl}/oauth2/callback/{registrationId}'
          KAKAO_TOKEN_RESPONSE_URI='https://localhost:3001/auth/oauth-response'
          KAKAO_LOCAL_TOKEN_RESPONSE_URI='https://localhost:3001/auth/oauth-response'
          KAKAO_LOCAL_TOKEN=''

          COOKIE_DOMAIN='localhost'
          OPENAI_MODEL= 'ft:gpt-3.5-turbo-0125:personal::99mAOVqt'
          OPENAI_SECRET_KEY=''
          REDIS_HOSTNAME='redis-container'
          REDIS_PORT='6379'
      }
      stages{
          stage('Cleaning up'){
              agent any
              steps{
  		        sh 'docker stop devback-container || true'
                  sh 'docker stop fastapi || true'
  		        sh 'docker rm devback-container || true'
                  sh 'docker rm fastapi || true'
                  sh 'docker rmi devback-image || true'
                  sh 'docker rmi fastapi || true'

                  sh 'rm -rf /var/jenkins_home/workspace/letterforme-backdev || true'
              }
              post{
                  failure{
                      error 'Fail Cleaning up'
                  }
              }
          }

          stage('git clone'){
              steps {
                  git branch: 'develop', credentialsId: 'gitlab_name', url: 'https://lab.ssafy.com/s10-bigdata-recom-sub2/S10P22A108.git'
              }
          }
          stage('Build recsys : Fast API'){
              steps{
                   dir('recsys/') {
                     sh '''
                      docker build -t fastapi .
                      docker run -p 8000:8000 -d --name fastapi -e MYSQL_USERNAME=${MYSQL_USERNAME} -e MYSQL_PASSWORD=${MYSQL_PASSWORD} -e MYSQL_HOST=${MYSQL_HOST} -e MYSQL_DATABASE=${MYSQL_DATABASE} fastapi
                     '''
                  }
              }
              post{
                  failure{
                      error 'Fail Docker Run'
                  }
              }
          }

          stage('Build back-end : Spring boot'){
              steps{
                  sh'''
                      docker build -f devbackDockerfile -t devback-image .
                      docker run -d -e TZ=Asia/Seoul --name devback-container -e KAKAO_LOGIN_RESTAPI_KEY=${KAKAO_LOGIN_RESTAPI_KEY} -e KAKAO_LOGIN_CLIENT_SECRET=${KAKAO_LOGIN_CLIENT_SECRET} -e MYSQL_USERNAME=${MYSQL_USERNAME} -e MYSQL_PASSWORD=${MYSQL_PASSWORD} -e JWT_SECRET_KEY=${JWT_SECRET_KEY} -e MYSQL_URL=${MYSQL_URL} -e KAKAO_LOGIN_REDIRECT_URI=${KAKAO_LOGIN_REDIRECT_URI} -e KAKAO_TOKEN_RESPONSE_URI=${KAKAO_TOKEN_RESPONSE_URI} -e KAKAO_LOCAL_TOKEN_RESPONSE_URI=${KAKAO_LOCAL_TOKEN_RESPONSE_URI} -e KAKAO_LOCAL_TOKEN=${KAKAO_LOCAL_TOKEN} -e REDIS_HOSTNAME=${REDIS_HOSTNAME} -e REDIS_PORT=${REDIS_PORT} -e OPENAI_SECRET_KEY=${OPENAI_SECRET_KEY} -e COOKIE_DOMAIN=${COOKIE_DOMAIN} -e OPENAI_MODEL=${OPENAI_MODEL} --network tarot-network devback-image
                  '''

              }
              post{
                  failure{
                      error 'Fail Docker Run'
                  }
              }
          }
      }
      post {
          always {
              dir("/var/jenkins_home/workspace/letterforme-backdev@tmp") {
                  deleteDir()
              }
              dir("/var/jenkins_home/workspace/letterforme-backdev@2") {
                  deleteDir()
              }
              dir("/var/jenkins_home/workspace/letterforme-backdev@2@tmp") {
                  deleteDir()
              }
          }
      }
  }
  ```

- frontend

  - dev-front branch에 push 시 실행
  - Dockerfile(devfrontDockerfile 경로 최상단에 위치)

  ```docker
  FROM node:latest

  WORKDIR /app

  COPY ./front-end/package*.json ./

  RUN npm install

  COPY ./front-end .

  RUN npm run build

  CMD ["npm", "start"]
  ```

  - letterforme-frontdev pipeline

  ```
  pipeline{
      agent any
      environment {
          NEXT_PUBLIC_MODE='local'
          NEXT_URL='https://dev.letter-for.me'
          NEXT_API_URL='https://dev.letter-for.me/api/v1'
      }
      stages{
          stage('Cleaning up'){
              agent any
              steps{
  		        sh 'docker stop devfront-container || true'
  		        sh 'docker rm devfront-container || true'
                  sh 'docker rmi devfront-image || true'
                  sh 'rm -rf /var/jenkins_home/workspace/letterforme-frontdev || true'
              }
              post{
                  failure{
                      error 'Fail Cleaning up'
                  }
              }
          }

          stage('git clone & build'){
              steps {
                  git branch: 'dev-front', credentialsId: 'gitlab_name', url: 'https://lab.ssafy.com/s10-bigdata-recom-sub2/S10P22A108.git'
                  sh 'docker build -f devfrontDockerfile -t devfront-image .'
              }
              post{
                  failure{
                      error 'Fail Docker Build'
                  }
              }
          }

          stage('Docker Run'){
              agent any
              steps{
                  sh 'docker run -d --name devfront-container -e NEXT_PUBLIC_MODE=${NEXT_PUBLIC_MODE} -e NEXT_URL=${NEXT_URL} -e NEXT_API_URL=${NEXT_API_URL} --network tarot-network devfront-image'
              }
              post{
                  failure{
                      error 'Fail Docker Run'
                  }
              }
          }
      }
      post {
          always {
              dir("/var/jenkins_home/workspace/letterforme-frontdev@tmp") {
                  deleteDir()
              }
              dir("/var/jenkins_home/workspace/letterforme-frontdev@2") {
                  deleteDir()
              }
              dir("/var/jenkins_home/workspace/letterforme-frontdev@2@tmp") {
                  deleteDir()
              }
          }
      }
  }
  ```

  - docker-compose.yaml에서 사용할 환경변수 .env

    - sudo vi /home/ubuntu/jenkins/env/.env

    ```
    KAKAO_LOGIN_RESTAPI_KEY=''
    KAKAO_LOGIN_CLIENT_SECRET=''
    MYSQL_USERNAME='root'
    MYSQL_PASSWORD='dltmdgjs'
    MYSQL_URL='jdbc:mysql://mysql-container:3306/letterforme'
    MYSQL_HOST ='j10a108.p.ssafy.io'
    MYSQL_DATABASE ='letterforme'

    JWT_SECRET_KEY=''
    KAKAO_LOGIN_REDIRECT_URI='{baseUrl}/oauth2/callback/{registrationId}'
    KAKAO_TOKEN_RESPONSE_URI='https://localhost:3001/auth/oauth-response'
    KAKAO_LOCAL_TOKEN_RESPONSE_URI='https://localhost:3001/auth/oauth-response'
    KAKAO_LOCAL_TOKEN=''

    COOKIE_DOMAIN='localhost'
    OPENAI_MODEL= 'ft:gpt-3.5-turbo-0125:personal::99mAOVqt'
    OPENAI_SECRET_KEY=''
    REDIS_HOSTNAME='redis-container'
    REDIS_PORT='6379'

    NEXT_PUBLIC_MODE='local'
    NEXT_PUBLIC_URL='https://letter-for.me'
    NEXT_PUBLIC_API_URL='https://letter-for.me/api/v1'
    ```

- release

  - release branch에 push 시 실행
  - Docker-compose.yaml(경로 최상단에 위치)

  ```docker
  services:
    backend:
      build:
        context: .
        dockerfile: devbackDockerfile
      environment:
        - KAKAO_LOGIN_RESTAPI_KEY
        - KAKAO_LOGIN_CLIENT_SECRET
        - MYSQL_USERNAME
        - MYSQL_PASSWORD
        - MYSQL_URL
        - MYSQL_HOST
        - MYSQL_DATABASE
        - JWT_SECRET_KEY
        - KAKAO_LOGIN_REDIRECT_URI
        - KAKAO_TOKEN_RESPONSE_URI
        - KAKAO_LOCAL_TOKEN_RESPONSE_URI
        - KAKAO_LOCAL_TOKEN
        - COOKIE_DOMAIN
        - REDIS_HOSTNAME
        - REDIS_PORT
        - OPENAI_MODEL
        - OPENAI_SECRET_KEY
        - TZ=Asia/Seoul
      networks:
        - tarot-network

    frontend:
      build:
        context: .
        dockerfile: devfrontDockerfile
      environment:
        - NEXT_PUBLIC_MODE
        - NEXT_PUBLIC_URL
        - NEXT_PUBLIC_API_URL
        - TZ=Asia/Seoul
      networks:
        - tarot-network

  networks:
    tarot-network:
      driver: bridge
      external: true
  ```

  - letterforme pipeline

  ```
  pipeline{
      agent any
      stages{
          stage('Cleaning up'){
              agent any
              steps{
  								sh 'docker stop letterforme-backend-1 || true'
  								sh 'docker stop letterforme-frontend-1 || true'
  								sh 'docker rm letterforme-backend-1 || true'
  								sh 'docker rm letterforme-frontend-1 || true'
                  sh 'docker rmi letterforme-backend || true'
                  sh 'docker rmi letterforme-frontend || true'
                  sh 'rm -rf /var/jenkins_home/workspace/letterforme|| true'
              }
              post{
                  failure{
                      error 'Fail Cleaning up'
                  }
              }
          }

          stage('git clone'){
              steps {
                  git branch: 'release', credentialsId: 'gitlab_name', url: 'https://lab.ssafy.com/s10-bigdata-recom-sub2/S10P22A108.git'

              }
              post{
                  failure{
                      error 'Fail git clone'
                  }
              }
          }

          stage('docker-compose up'){
              steps {
                  sh 'cp /var/jenkins_home/env/.env .'
                  sh 'docker compose up -d --build'
                  sh 'docker restart nginxc'
              }
              post{
                  failure{
                      error 'Fail compose up'
                  }
              }
          }
      }
      post {
          always {
              dir("/var/jenkins_home/workspace/letterforme@tmp") {
                  deleteDir()
              }
              dir("/var/jenkins_home/workspace/letterforme@2") {
                  deleteDir()
              }
              dir("/var/jenkins_home/workspace/letterforme@2@tmp") {
                  deleteDir()
              }
          }
      }
  }
  ```

5. Nginx 설정
   > 가비아에서 letter-for.me 도메인을 구매해 사용

1) 가비아 설정

   - public ip 확인 -> `$ curl ip.ojj.kr` -> 43.202.56.72
   - letter-for.me / dev.letter-for.me 설정
     > letter-for.me -> 배포용 | dev.letter-for.me -> 개발용
     - 타입 : A | 호스트 : @ | 값/위치 : 43.202.56.72 | TTL : 3306
     - 타입 : A | 호스트 : dev | 값/위치 : 43.202.56.72 | TTL : 3306

2) Nginx 컨테이너 생성

```bash
# Nginx 이미지 가져오기
$ docker pull nginx

# Nginx 컨테이너 생성
# Nginx 내 default.conf 파일은 수시로 수정해야하므로 반드시 volume 설정
# network 옵션을 통해 기존 jenkins-container 와 연결
$ docker run -d -p 80:80 -p 443:443 -v /home/ubuntu/nginx/default.conf:/etc/nginx/conf.d/default.conf -v /home/ubuntu/nginx/etc/letsencrypt:/etc/letsencrypt --name nginxc --network tarot-network nginx
```

3. SSL 설정

```bash
# 컨테이너 접속
$ docker exec -it nginx bash
# apt-get update
# apt-get install certbot python3-certbot-nginx
# apt-get install vim
# certbot --nginx -d letter-for.me -d dev.letter-for.me
```

4. default.conf 파일 설정

- volume 설정 경로의 default.conf 편집
- upstream 설정을 사용하기 위해 dev-front, develop, release 브랜치에 push를 하거나 따로 컨테이너 생성
- 혹은 upstream 설정과 관련 부분 주석 처리

```
$ sudo vi /home/ubuntu/nginx/default.conf
```

- default.conf

```
upstream dev_front {
    server devfront-container:3000;
}

upstream dev_back {
    server devback-container:8081;
}

upstream frontend {
    server letterforme-frontend-1:3000;
}

upstream backend {
    server letterforme-backend-1:8081;
}

map $http_upgrade $connection_upgrade {
  default upgrade;
  '' close;
}

server {
    if ($host = dev.letter-for.me) {
        return 301 https://$host$request_uri;
    } # managed by Certbot

    if ($host = letter-for.me) {
        return 301 https://$host$request_uri;
    } # managed by Certbot

    listen 80;
    listen [::]:80;
    server_name letter-for.me dev.letter-for.me;
    return 404;
}

server {
    listen 443 ssl;
    listen [::]:443 ssl;
    server_name letter-for.me;
    ssl_certificate /etc/letsencrypt/live/letter-for.me/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/letter-for.me/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    location /jenkins/ {
        proxy_pass http://letter-for.me:9090; # letter-for.me/jenkins 로 jenkins 접속 가능
        proxy_redirect     default;
        proxy_http_version 1.1;

        proxy_set_header   Connection        $connection_upgrade;
        proxy_set_header   Upgrade           $http_upgrade;

        proxy_set_header   Host              $http_host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
        proxy_max_temp_file_size 0;

        client_max_body_size       10m;
        client_body_buffer_size    128k;

        proxy_connect_timeout      90;
        proxy_send_timeout         90;
        proxy_read_timeout         90;
        proxy_request_buffering    off;
    }

    location /oauth2/ {
				proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /api/v1 {
				proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location / {
        proxy_pass http://frontend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    add_header X-Frame-Options "SAMEORIGIN";
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";

}
server {
    listen 443 ssl;
    listen [::]:443 ssl;
    server_name dev.letter-for.me;
    ssl_certificate /etc/letsencrypt/live/letter-for.me/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/letter-for.me/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    location /oauth2/ {
				proxy_pass http://dev_back;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /api/v1 {
				proxy_pass http://dev_back;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location / {
				proxy_pass http://dev_front;
				proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
				proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

## 5 외부 서비스 연동

### 5.1 fastapi

### 5.2 gpt server

1. OpenAI 젒속 후 APIKeys 발급
2. application.yml의 OPENAI_SECRET_KEY에 알맞게 정보 입력
