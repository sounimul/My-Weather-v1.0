# My Weather : 나만의 날씨
### 💡현재위치의 날씨를 조회하고 자신의 체감 날씨를 기록할 수 있는 웹사이트



<br></br>
## 프로젝트 설명
#### (1) 로그인, 회원가입 페이지
#### (2) 날씨를 보여주는 페이지
- 현재 위치의 날씨와 예보를 보여주는 기능
    - 현시간, 1시간 전, 1시간 후 날씨
    - 오늘의 날씨(일 최고기온, 일 최저기온, 습도, 1시간 강수량)
    - 1-2일 후의 최고기온, 최저기온과 해당 시간의 날씨
    - 3-5일 후의 오전, 오후 날씨
- 현재의 날씨와 체감 날씨를 기록하는 기능
- 웹사이트의 리뷰를 작성하는 기능
- 튜토리얼 기능
#### (3) 자신의 체감 날씨를 기록할 수 있는 페이지
- 저장한 날씨 기록을 조회하는 기능
- 사용자 정보 수정 기능
#### (4) 관리자 페이지
- 사용자, 날씨 기록, 별점을 조회하는 기능

<br></br>
## 개발 환경
- **UI** : Figma
- **Front-end** : HTML, CSS, JavaScript, Ajax
- **Database Design**
- **Back-end** : Java 17, Spring 3.0.1, JPA, Thymeleaf, MySQL
- **IDE** : VS Code, IntelliJ IDEA (gradle 7.6)

<br></br>
## 역할
- **sounimul** : UI 설계, 웹 프론트엔드
- **songu1** : 데이터베이스 설계, 웹 백엔드

<br></br>
## 프로젝트 수행
### 데이터베이스 설계 (데이터모델링)
#### (1) 개념 데이터 모델링 : ER 다이어그램
<img src="https://github.com/sounimul/Project1/assets/75112062/8f93988d-7497-4a6e-8c01-5f5837d45a1f.jpg" width="70%" height="70%"/>

#### (2) 논리 데이터 모델링 : Relational 다이어그램
<img src="https://github.com/sounimul/Project1/assets/75112062/881ee0df-5829-4c5b-bce0-b251b108442b.jpg" width="70%" height="70%"/>

#### (3) 물리 데이터 모델링 : DDL

<br></br>
### 백엔드 구현
- Spring MVC패턴으로 동작하도록 구현하고 JPA를 사용
- HttpSession으로 로그인한 UID, 주소, 날씨정보를 처리
- JS에서 얻은 데이터를 AJAX를 통해 서버로 전송
- REST API방식으로 WebClient를 사용하여 Kakao API와 기상청 API를 호출
    - 위도와 경도를 행정구역으로 변환 (**Kakao API**)
    - 현재시간과 기상청 X,Y좌표를 사용하여 날씨를 가져옴 (**기상청 API**)
        - **초단기실황 API** : (1)현재 시간의 날씨, (2) 1시간 전의 날씨
        - **초단기예보 API** : (1)1시간 후의 날씨 (2) 당일의 최고기온, 최저기온
        - **단기예보 API** : (1) 3일치의 최고기온, 최저기온

