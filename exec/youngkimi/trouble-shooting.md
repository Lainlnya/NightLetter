### 24.05.14

채팅 관련 트러블 슈팅
프론트 - 백 통신 관련
같은 서브넷 IP 활용.
그럼에도 프론트는 localhost 작업.
서버는 로컬 IP를 활용해 통신.
websocket은 web 기술. 80 , 443 포트 사용해야. -> nginx 활용 리버스 프록시.
먼저 Cors 관련 에러 해결. websocket config 에서 allow Origin 설정.
그리고 nginx 내부 header 정보 추가 (upgrade)
https://www.nginx.com/blog/websocket-nginx/

### 24.05.16

int 활용해서 쿼리. (총 세번의 쿼리)

```
Hibernate: select m1_0.member_id,m1_0.oauth2id,m1_0.created_at,m1_0.created_by,m1_0.deleted_at,m1_0.email,m1_0.nickname,m1_0.profile_img_url,m1_0.provider,m1_0.updated_at,m1_0.updated_by from member m1_0 where (m1_0.deleted_at IS NULL) and m1_0.member_id=?
Hibernate: select c1_0.member_id,s1_0.nickname,s1_0.profile_img_url,c1_0.send_time,c1_0.message,case when (c1_0.member_id=?) then abs(sign(?)) else 0 end from chat c1_0 join member s1_0 on s1_0.member_id=c1_0.member_id and (s1_0.deleted_at IS NULL) where c1_0.chatroom_id=? order by c1_0.send_time desc limit ?,?
Hibernate: select count(distinct c1_0.chat_id) from chat c1_0 where c1_0.chatroom_id=?
```

entity 활용해서 쿼리.

```
Hibernate: select m1_0.member_id,m1_0.oauth2id,m1_0.created_at,m1_0.created_by,m1_0.deleted_at,m1_0.email,m1_0.nickname,m1_0.profile_img_url,m1_0.provider,m1_0.updated_at,m1_0.updated_by from member m1_0 where (m1_0.deleted_at IS NULL) and m1_0.member_id=?
Hibernate: select c1_0.member_id,s1_0.nickname,s1_0.profile_img_url,c1_0.send_time,c1_0.message,case when (c1_0.member_id=?) then abs(sign(?)) else 0 end from chat c1_0 join member s1_0 on s1_0.member_id=c1_0.member_id and (s1_0.deleted_at IS NULL) where c1_0.chatroom_id=? order by c1_0.send_time desc limit ?,?
Hibernate: select count(distinct c1_0.chat_id) from chat c1_0 where c1_0.chatroom_id=?
```

두번 다 세 번의 쿼리 발생.
