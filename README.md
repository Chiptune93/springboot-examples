## Form Login With Custom Authentication

### 절차

1. 사용자는 폼 로그인을 시도한다.
2. UsernamePasswordAuthenticationFilter 에서 사용자의 인증 요청을 
UsernamePasswordAuthenticationToken 객체로 만들어 AuthenticationManager에게 전달한다.
3. AuthenticationManager는 리스트 형태로 가지고 있는 AuthenticationProvider에게 인증을 위임한다.
4. 적절한 AuthenticationProvider 에서 실제 인증 처리를 실시한다.
5. 인증 결과(성공/실패)에 따라 AbstractAuthenticationProcessingFilter를 구현한
AuthenticationSuccessHandler 또는 AuthenticationFailureHandler를 호출한다.

- 참고 : https://beaniejoy.tistory.com/86

### 구현

#### CustomUsrDetails

- 사용자 인증을 위한 인증 정보 커스터마이징 클래스

#### CustomUserDetailsService

- 사용자 정보를 데이터베이스에서 가져오기 위한 커스텀 클래스.
- loadByUsername을 구현하여 데이터베이스에서 가져오기 위해 구성한다.

#### CustomAuthenticationProvider

- 실제 인증을 처리하는 로직을 담당하는 클래스.
- 아이디/패스워드 기반으로 인증 처리를 하도록 한다.

#### CustomAuthenticationToken

- 아이디/패스워드 외, 다른 부가적인 정보를 저장하기 위한 토큰 클래스.
- 

#### CustomAuthenticationFilter

- AuthenticationFilter를 구현하는 커스텀 필터를 생성한다.
- UsernamePasswordAuthenticationFilter는 이를 구현한 필터이며, 이를 다시 확장하여 커스터마이징 하여 사용한다.

### 구현 시, 참고한 점.

- 위 커스텀 된 클래스들은 전부 Config에서 Beans으로 등록해야 한다.
- 커스텀 클래스를 전부 구현한 대상 타입으로 리턴되도록 하여 빈을 등록한다.

