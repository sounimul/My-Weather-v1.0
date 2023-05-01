# WEATHER : 날씨를 알려주는 웹사이트
💡현재위치의 날씨를 알려주고 기온별 체감 날씨를 기록할 수 있는 웹사이트
![날씨페이지](https://user-images.githubusercontent.com/75112062/235446380-b2866a32-81b6-4ac8-ae35-327c94f78143.jpg)
![마이페이지](https://user-images.githubusercontent.com/75112062/235446417-31fe3bd5-8280-43e7-b8bc-2da6abef25ea.jpg)


<br></br>
## 프로젝트 설명
#### (1) 로그인, 회원가입 페이지
#### (2) 날씨를 보여주는 페이지
- 현재 시간과 행정구역을 보여주는 기능
- 현재 시간, 1시간 전, 1시간 후의 날씨를 보여주는 기능
- 당일과 3일치의 최고기온과 최저기온을 보여주는 기능
#### (3) 기온별 자신의 체감 날씨를 기록할 수 있는 페이지 - **추후 진행 예정**
- 현재 날씨를 저장하여 자신의 체감 날씨를 기록하는 기능

<br></br>
## 개발 환경 및 역할
### sounimul
- **UI** : Figma

- **Front-end** : HTML, CSS, JavaScript
### songu1
- **Database Design**

- **Back-end** : Java, Spring, JPA, Thymeleaf, MySQL, REST API

<br></br>
## 프로젝트 수행
### 데이터베이스 설계 (데이터모델링)
#### (1) 개념 데이터 모델링 : ER 다이어그램
<img src="https://user-images.githubusercontent.com/75112062/234794778-16e38b56-d81a-4399-b689-49fd8877b26b.jpg" width="70%" height="70%"/>

#### (2) 논리 데이터 모델링 : Relational 다이어그램
<img src="https://user-images.githubusercontent.com/75112062/234794634-7df1357b-a134-4547-884c-c4e38cc85a2c.jpg" width="70%" height="70%"/>

#### (3) 물리 데이터 모델링 : DDL

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

