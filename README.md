# Log4j2 With MongoDB

lof4j2를 이용해 MongoDB에 어플리케이션 로그를 적재한다.

## 버전 호환성

- [자바와의 호환성 체크](https://www.mongodb.com/docs/drivers/java/sync/current/compatibility/)

자바 21을 MongoDB 자바 드라이버 4.5부터 지원하나 버추얼 스레드 문제가 있는 듯 하다.
자바 21을 사용하진 않지만 최신 지원이 4.5부터 이기 때문에 드라이버는 4.5버전 이상을 사용합니다.

자바 드라이버 4.5 이상 버전 중, 최신 몽고 DB와 호환되는 것은 4.10버전(Mongo 7버전 부터 3.6까지 지원)
이기 떄문에 Mongo Java Drvier는 4.10을 사용하기로 결정했습니다.

그리하여 최종적으로는 Mongo DB 4.4을 사용하며, Java Driver는 4.10 버전을 사용합니다.
5.0+ 이상부터는 CPU 관련 지원 문제가 있어 ... 사용하지 않았습니다.

## MongoDB 설치

도커로 진행합니다.

```bash
# Docker 이미지 다운로드
docker pull mongo:4.4

# MongoDB 구동
docker run -d --name mongodb -v /home/chiptune/database/mongodb:/data/db \
-e MONGO_INITDB_ROOT_USERNAME=root \
-e MONGO_INITDB_ROOT_PASSWORD=1234 \
-p 27017:27017 \
mongo:4.4

# MongoDB 접속
docker exec -it mongodb /bin/bash
# mongodb 6.0 이전은 `mongo` 로 접속, 이후는 `mongosh`사용.
> mongosh -u root -p 1234

# 사용자 생성
> use logging; # 데이터베이스 생성 및 선택
> db.createUser( { user: "log", pwd: "1234", roles: [ {"role": "readWrite", "db" : "logging"}, ] } )
```

### 접속 테스트

![img.png](img.png)


## 종속성 추가

log4j2 에서는 NoSQL 데이터베이스를 위한 전용 Appender를 구성할 수 있도록 
관련 태그를 지원합니다.

자세한 내용은 [여기](https://logging.apache.org/log4j/2.x/manual/appenders.html#NoSQLAppenderMongoDB4)를 참고 바랍니다.

### build.gradle

```groovy
dependencies {
    // ...
    implementation("org.springframework.boot:spring-boot-starter") {
        // log4j2 사용을 위해 기본 로깅 처리 클래스 제외
        exclude group: "org.springframework.boot", module: "spring-boot-starter-logging"
    }
    implementation 'org.apache.logging.log4j:log4j-mongodb4'
    implementation 'org.apache.logging.log4j:log4j-core'
}
```

종속성을 추가 했으면 log4j2를 위한 설정 파일을 추가합니다.

### log4j2.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="DEBUG" scan="true" scanPeriod="1 hour">
    <Appenders>
        <!-- 콘솔 Appender: 로그를 콘솔(시스템 출력)에 기록 -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <!-- MongoDB Appender: 로그 이벤트를 MongoDB 데이터베이스에 저장 -->
        <NoSql name="MongoDbAppender">
            <MongoDb4
                    connection="mongodb://log:1234@localhost:${sys:MongoDBTestPort:-27017}/logging.logs"
                    capped="true"
                    collectionSize="1073741824"/>
            <!-- capped collection 사용 -->
            <!-- 컬렉션 최대 크기 (1GB) 설정 -->
        </NoSql>
    </Appenders>
    <Loggers>
        <!-- Root Logger 설정 -->
        <Root level="INFO">
            <!-- 로거의 기본 레벨을 INFO로 설정 -->
            <AppenderRef ref="Console"/>
            <AppenderRef ref="MongoDbAppender"/>
        </Root>
    </Loggers>
</Configuration>
```

`MongoDb4` 태그에 접속 정보를 세팅합니다. 
접속 정보는 `mongodb://{계정명}:{비밀번호}@{호스트}:{포트}/{데이터베이스명}.{컬렉션명}` 입니다.

이렇게 하면 세팅은 끝이 납니다. 이 상태에서 어플리케이션을 구동합니다.

## 테스트

어플리케이션을 구동하고, 올라오는 로그에서 `Mongodb` 를 검색하면 다음과 같이 연결 시도 로그 및 성공 여부를 확인할 수 있습니다.

![img_1.png](img_1.png)

이제 실제 로그가 MongoDB에 쌓였는지 확인하겠습니다.

![img_2.png](img_2.png)

위와 같이 정상적으로 어플리케이션 로그가 쌓이고 있습니다.

## 결론

다행히도 log4j2 에서 공식적으로 No SQL에 대한 Appender를 지원해주고 있어
별다른 복잡한 설정 없이 로그를 옮길 수 있었습니다.

다만 진행하면서 어려움이 있었던 것은 MongoDB에 생성한 사용자와 컬렉션 등에서 
권한이 없다거나, 컬렉션이 생성되지 않는 등의 문제가 있어서 조금은 헤메고 말았습니다.

다행히 정리가 될 정도로 그렇게 힘들지는 않았습니다.

전체 예제 소스는 [여기](https://github.com/Chiptune93/springboot.java.example/tree/logging/log4j2-to-mongodb)서 보실 수 있습니다!
