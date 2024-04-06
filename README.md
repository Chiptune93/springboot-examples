## Redis

### Redis 설치

docker redis 설치 및 실행

```bash
docker run redis --name redis -d -p 6379:6379 redis
```

redis password 설정

```bash
# 컨테이너 접속
dk@Chiptune ~ % docker exec -it redis bash
# redis-cli로 접속
root@478e4e66d5f8:/data# redis-cli
# 비밀번호 테스트
127.0.0.1:6379> AUTH 'test'
# 비밀번호 미설정 시 멘트
(error) ERR AUTH <password> called without any password configured for the default user. Are you sure your configuration is correct?
# 비밀번호 설정 조회
127.0.0.1:6379> config get requirepass
1) "requirepass"
2) "" # 없음.
# 비밀번호를 1q2w3e4r 로 설정
127.0.0.1:6379> config set requirepass 1q2w3e4r
OK
# 테스트 데이터 세팅
127.0.0.1:6379> set foo boo
OK
# 재접속
127.0.0.1:6379> exit
root@478e4e66d5f8:/data# redis-cli
# 테스트
127.0.0.1:6379> get foo '1q2w3e4r'
# 비번 없어서 실패
(error) ERR wrong number of arguments for 'get' command
# 비번 인증!
127.0.0.1:6379> AUTH '1q2w3e4r'
OK
# 성공
127.0.0.1:6379> get foo
"boo"
```

