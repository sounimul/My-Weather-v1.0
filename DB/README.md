# WEATHER DATABASE

## 1. Requirement collection and Analysis

### (1) 개요
- 정보 종류 : 강수, 풍속, 습도, 자외선, 일출/일몰 시간, 미세먼지, 온도 -- entity에서 제거
- 지역 : 조회 지역 주소(key attribute), 위도, 경도, X좌표, Y 좌표
- 사용자 : uniq_id(key attribute), 닉네임, 아이디, 비밀번호, 자기소개
- 날씨 기록(날씨에 대한 평가) : 날짜(key attribute), 최고기온, 최저기온, 평가기록, 주소
    - 최고, 최저기온 불러와서 저장

### (2) 요구사항

- 사용자는 원하는 지역을 선택
- 사용자는 날씨 기록을 가짐

## 2. ER diagram - INFO_LIST, CHOSSE 삭제
![Weather_ER drawio (1)](https://user-images.githubusercontent.com/75112062/221160619-0d5c31fa-2786-4968-b468-8e97eed0c4dc.png)

### Entity & Attribute
#### (1) USER : 사용자(회원) 정보
- Uniq_id : Key attribute, 사용자를 식별하는 id, 사용자 회원가입시 자동생성됨
- User_id : 회원가입 시 아이디
- pw : 회원가입 시 비밀번호
- Nickname : 사용자 닉네임
- Introduction : 사용자의 소개글귀
- Available : 사용자 계정 사용 유무 (Y/N)
    - Y : 사용 가능
    - N : 사용 불가능 (사용자가 계정 삭제시)

#### (2) REGION : 사용자가 날씨 조회/저장한 지역, Weak entity(USER가 없으면 REGION이 사라짐)
- Address : Key attribute, 사용자가 검색하는 지역의 주소
- Latitude : 검색한 주소의 위도
- Longitude : 검색한 주소의 경도
- X_coorinate : 검색한 위치의 X좌표(날씨 api를 사용하기 위해)
- Y_coorinate : 검색한 위치의 Y좌표(날씨 api를 사용하기 위해)

#### (3) WEATHER_RECORD : 날씨기록(날씨에 대한 의견), Weak entity(USER가 없으면 WEATHER_RECORD가 사라짐)
- Date : Key attribute, 날씨를 기록할 날짜
- Feeling : 체감 날씨 의견
    - ex) 추움, 더움, 따뜻함 등
- Address : 해당 날씨의 위치
- Temp_max: 최대 기온
- Temp_min : 최소 기온

### Relationship
#### (1) SAVE
- USER는 REGION을 꼭 가지지 않아도 됨 (Partial participation)
- REGION는 USER를 꼭 가져야함 (Total participation)
- 1:N
- 식별관계

#### (2) WRITE
- USER는 WEATHER_RECORD를 꼭 가지지 않아도 됨 (Partial participation)
- WEATHER_RECORD는 USER를 꼭 가져야함(Total participation)
- 1:N
- 식별관계

## 3. Relational diagram - CHOSSE, INFO_LIST 삭제
![Weather_Relational drawio (1)](https://user-images.githubusercontent.com/75112062/221161540-abc4f4b8-0baa-46ca-814b-42686bad4c72.png)


#### REGION
- Uuid, Address : primary key
- Uuid : USERLIST의 Uniq_id를 참조

#### USERLIST (ER diagram의 USER)
- Uniq_id : primary key
- User_id : unique

#### WEATHER_RECORD
- Uuid, Rdate : primary
- Uuid : USERLIST의 Uniq_id를 참조
