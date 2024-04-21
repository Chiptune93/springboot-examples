## HttpSession

### 세션 찾기 및 생성 원리

#### 1. 세션 ID의 전송 및 수신

- 클라이언트 요청: 클라이언트는 HTTP 요청을 서버에 보낼 때 HTTP 쿠키 헤더를 통해 JSESSIONID라는 이름의 쿠키를 포함시켜 전송할 수 있습니다. 이 JSESSIONID는 이전에 서버로부터 받은 세션 식별자입니다.
- 서버 수신: 서버는 HTTP 요청 헤더에서 JSESSIONID 쿠키를 찾습니다. 이 쿠키는 세션을 식별하는 데 사용됩니다.

#### 2. 세션 검색

- 세션 저장소 조회: 서버는 세션 저장소(일반적으로 메모리 내 저장소, 또는 분산 세션 관리를 위한 외부 저장소일 수 있음)에서 JSESSIONID 값에 해당하는 세션 객체를 조회합니다.
- 세션 유효성 검사: 조회된 세션 객체는 유효성 검사를 거칩니다. 세션의 유효기간이 지나거나 다른 이유로 세션이 무효화되었다면 해당 세션은 사용할 수 없습니다.

#### 3. 세션 생성

- 기존 세션 존재 여부: 만약 유효한 세션을 찾았다면, 서버는 이 세션 객체를 요청 객체에 연결합니다. 만약 유효한 세션을 찾지 못하고 getSession(true)가 호출되었다면 새 세션을 생성합니다.
- 새 세션 생성: 새로운 세션 객체가 생성되고, 새로운 JSESSIONID가 할당됩니다. 이 ID는 응답과 함께 클라이언트에게 전송되어 다음 요청부터 사용됩니다.

#### 4. 클라이언트에게 세션 ID 전송

- 쿠키 설정: 새로 생성된 세션 ID는 HTTP 응답의 Set-Cookie 헤더를 통해 클라이언트에게 전송됩니다. 클라이언트는 이후 요청에서 이 세션 ID를 포함시켜 서버와의 상태를 유지합니다.

#### 5. 세션 유지 및 관리

- 세션 타임아웃: 세션은 설정된 시간 동안만 유효합니다. 타임아웃 시간이 지나면 세션은 서버에 의해 자동으로 만료됩니다.
- 세션 데이터 관리: 세션 동안 사용자의 데이터는 세션 객체에 저장됩니다. 이 데이터는 로그인 상태 유지, 사용자 선호 설정 저장 등 다양한 용도로 활용될 수 있습니다.

### 추가 고려 사항

- 보안: 세션 ID는 보안에 매우 중요합니다. 세션 하이재킹을 방지하기 위해 HTTPS를 사용하여 데이터를 암호화하고, 쿠키 속성에 HttpOnly와 Secure를 설정하여 클라이언트 사이드 스크립트가 세션 쿠키에 접근하지 못하도록 해야 합니다.
- 성능 및 확장성: 대규모 분산 시스템에서는 세션 정보를 효과적으로 관리하기 위해 외부 세션 저장소를 사용할 수 있습니다. 이는 세션 정보의 일관성을 유지하고, 서버 간 세션 공유를 가능하게 하여 시스템의 확장성을 높입니다.

## 예제 소스

### Login Controller에서 사용자를 체크 해 세션을 생성한다.

- Login Form 에서 아래 URL로 정보를 보내면 체크해 세션을 생성한다.
- 뷰의 Form 태그는 login.html을 참고하면 된다.

```java
@PostMapping("/loginProcess")
    public String join(@ModelAttribute("username") String username,
                       @ModelAttribute("password") String password,
                       HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // 사용자 체크
            TestUser testUser = testUserRepository.findByNameAndPassword(username, password);
            if (testUser != null) {
                // 세션을 생성하기 전에 기존의 세션 파기
                request.getSession().invalidate();
                session = request.getSession(true);  // Session이 없으면 생성
                // 세션에 userId를 넣어줌
                session.setAttribute("loginUser", testUser);
                session.setMaxInactiveInterval(1800); // Session이 30분동안 유지
            }
        }
        return "redirect:/login/page";
    }
```

### 화면 테스트

- localhost:8080/login/page 에 접속. 현재 세션이 없어 메세지가 뜨고 있음.

![img.png](img.png)

- 로그인을 시도하면 (User1,1234) 성공 후에 아래와 같이 세팅했던 세션을 가져올 수 있다.

![img_1.png](img_1.png)
