# springboot-examples

다양한 스프링 부트 예제를 기록하기 위한 프로젝트입니다.

## Springboot Version

- [x] Springboot 2.7.18
- [x] Java 11


## Branches

### **master**

기본 브랜치, 해당 브랜치 기준으로 확장되고 있습니다.

---

### **[aop](https://github.com/Chiptune93/springboot-examples/tree/aop)**

- LogAspect
    - 메소드 실행 전/후로 로그를 남기기 위한 Log AOP 클래스
- NoLogging
  - Log AOP 에서 특정 클래스/메소드에 AOP 미적용하기 위한 어노테이션 클래스
- RunningTimeAspectForSpring
  - Spring 에서 실행시간 계산하는 AOP 클래스 등록 및 사용.
- RunningTimeAspectForSpringBoot
  - SpringBoot 에서 실행시간 계산하는 AOP 클래스 사용.

---

### **[aync-config](https://github.com/Chiptune93/springboot-examples/tree/aync-config)**

- AsyncConfig
  - 비동기 처리를 위한 스레드 실행 계획 설정 클래스

---

### **[cors](https://github.com/Chiptune93/springboot-examples/tree/cors)**

- SampleController
    - 컨트롤러 레벨에서 CORS를 컨트롤하기 위한 설정 예시
- WebConfig
    - SpringBoot Config 레벨에서 CORS를 컨트롤하기 위한 설정 예시
- CorsFilter
    - Filter를 이용한 CORS 처리 예시

---

### **[dynamic-datasource](https://github.com/Chiptune93/springboot-examples/tree/dynamic-datasource)**

- SQLConfig
    - 마스터/슬레이브 데이터베이스 설정을 위한 멀티 데이터 소스 예시
- SQLRoutingDataSource
    - 어노테이션을 통한 분기 처리용 클래스

---

### **[file-upload](https://github.com/Chiptune93/springboot-examples/tree/file-upload)**
   
- 파일 업로드/다운로드 예제 구현

---

### **[h2-database](https://github.com/Chiptune93/springboot-examples/tree/h2-database)**

- h2-database를 간단히 사용하는 예제


### **[jsp-for-view](https://github.com/Chiptune93/springboot-examples/tree/jsp-for-view)**
- springboot use jsp for view

---

### **[msa-architecture](https://github.com/Chiptune93/springboot-examples/tree/msa-architecture)**
- MSA 구조를 위한 아키텍처 예시

---

### **[restapi](https://github.com/Chiptune93/springboot-examples/tree/restapi)**
- REST API 구현 예제

---

### **[spring-cloud](https://github.com/Chiptune93/springboot-examples/tree/spring-cloud)**
- web client & server for spring cloud

---

### **[swagger](https://github.com/Chiptune93/springboot-examples/tree/swagger)**
- 스웨거 사용 예제

---

### **[Thymeleaf](https://github.com/Chiptune93/springboot-examples/tree/thymeleaf)**
- 타임리프를 빠르게 사용하기 위한 예제

---

### **[spring-data-jdbc](https://github.com/Chiptune93/springboot-examples/tree/spring-data-jdbc)**
- spring-data-jdbc 예제

---

## feature

### spring-security 5.7

### **[jwt-token](https://github.com/Chiptune93/springboot.java.example/tree/feature/spring-security/jwt-token)**
- JWT Token 인증 방식 구현
- 최대한 간단하게 구현하려고 노력한 버전

---
 
### **[form-login](https://github.com/Chiptune93/springboot.java.example/tree/feature/spring-security/form-login)**
- 폼 로그인 예제를 구현
- 시큐리티 클래스 중, 구현한 것
    - UserDetails
    - UserDetailsService
- 암호화 인코더 없음
- 인가 처리 하지 않음
