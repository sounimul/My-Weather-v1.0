# My Weather : 나만의 날씨
> 사이드 프로젝트
>
> 링크 : https://myweather.kro.kr/
## My Weather WebPage ver 1.0
- 개발기간 : 2023.02 ~ 2023.06
- 기능 구현
## My Weather WebPage ver 1.1
- 개발 기간 : 2023.07 ~
- 웹사이트 성능, 버그 개선
- 코드 리팩토링
<br></br>
## 팀 COHO
- **한상경(sounimul)** : 프론트엔드 https://github.com/sounimul
- **송유정(songu1)** : 백엔드 https://github.com/songu1
### 역할
- **sounimul** : UI 설계, 웹 프론트엔드
- **songu1** : 데이터베이스 설계, 웹 백엔드

<br></br>
## 프로젝트 소개
### 💡현재위치의 날씨를 조회하고 자신의 체감 날씨를 기록할 수 있는 웹사이트
<br></br>
**제공하는 기능**
1. 현재 위치를 기반으로 날씨와 예보를 조회 (좋아하는 날씨 알림)
2. 현재 날씨와 자신의 체감 날씨를 저장하고 기록을 조회

<br></br>
## 개발 환경
- **UI** : Figma
- **Database Design**

- **Front-end** : HTML, CSS, JavaScript, Ajax<br></br>
    <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
    <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
    <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">

- **Back-end** : Java 17, Spring, Spring Boot 3.0.1, JPA, Thymeleaf, MariaDB <br></br>
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
    <img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white"> 

- **IDE** : VS Code, IntelliJ IDEA (gradle 7.6)

- **배포** : Cloud Type

<br></br>
## 화면 구성
|로그인 화면|회원가입 화면|
|:---:|:---:|
|![image](https://github.com/sounimul/Project1/assets/75112062/9ea6a7b8-2dd3-4376-816d-f3c7a9cf4836)|![image](https://github.com/sounimul/Project1/assets/75112062/348e75ff-1c36-45ec-bdce-2633a15cc5d0)
|날씨페이지 화면|튜토리얼 화면|
|![image](https://github.com/sounimul/Project1/assets/75112062/9617094e-1239-4136-88e2-8d2fc598532e)|![image](https://github.com/sounimul/Project1/assets/75112062/35b85850-a643-4c06-b1e3-adb4fd9b828c)|
|날씨 저장 기능|리뷰 저장 기능|
|![image](https://github.com/sounimul/Project1/assets/75112062/fbd32f77-9ec3-4d92-9cfe-2507123406f9)|![image](https://github.com/sounimul/Project1/assets/75112062/64176121-a512-4c71-9f79-c7228ebc5e35)|
|마이페이지 화면|관리자 페이지 화면|
|![image](https://github.com/sounimul/Project1/assets/75112062/e315df3e-ed4d-4e55-8e99-d0c58a2bb059)|![image](https://github.com/sounimul/Project1/assets/75112062/5fd8ffc2-a80b-4818-b53f-414d4fb4722e)|

<br></br>
## 주요 기능
### (1) 로그인, 회원가입 페이지
### (2) 날씨를 보여주는 페이지
- 현재 위치의 날씨와 예보를 보여주는 기능
    - 현재 시간, 1시간 전, 1시간 후 날씨
    - 오늘의 날씨(일 최고기온, 일 최저기온, 습도, 1시간 강수량)
    - 1-2일 후의 최고기온, 최저기온과 해당 시간의 날씨
    - 3-5일 후의 오전, 오후 날씨
- 현재의 날씨와 체감 날씨를 기록
- 웹사이트의 리뷰를 작성
- 튜토리얼
### (3) 자신의 체감 날씨를 기록할 수 있는 페이지
- 저장한 날씨 기록을 조회
- 사용자 정보 수정
### (4) 관리자 페이지
- 사용자, 날씨 기록, 별점을 조회

<br></br>
## 프로젝트 DB 설계
### 데이터베이스 설계 (데이터모델링)
#### (1) 개념 데이터 모델링 : ER 다이어그램
<img src="https://github.com/sounimul/Project1/assets/75112062/8f93988d-7497-4a6e-8c01-5f5837d45a1f.jpg" width="50%"/>

#### (2) 논리 데이터 모델링 : Relational 다이어그램
<img src="https://github.com/sounimul/Project1/assets/75112062/881ee0df-5829-4c5b-bce0-b251b108442b.jpg" width="50%"/>

#### (3) 물리 데이터 모델링 : DDL

