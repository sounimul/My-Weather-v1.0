# WEATHER DATABASE

## 1. Requirement collection and Analysis

### (1) 개요
- 정보 커스텀 : 정보 종류(key attribute)
    - (강수, 풍속, 습도, 자외선, 일출/일몰 시간, 미세먼지, 온도)
- 지역 : 조회 지역 주소(key attribute), 위도, 경도, X좌표, Y 좌표
- 사용자 : uniq_id(key attribute), 닉네임, 아이디, 비밀번호, 자기소개
- 날씨 기록(날씨에 대한 평가) : 날짜(key attribute), 최고기온, 최저기온, 평가기록, 주소
    - 최고, 최저기온 불러와서 저장

### (2) 요구사항

- 사용자는 원하는 지역을 선택
- 사용자는 원하는 정보를 선택
- 사용자는 날씨 기록을 가짐

## 2. ER diagram
![Weather_ER drawio](https://user-images.githubusercontent.com/75112062/204085713-bd74dcd6-e62c-4b63-a641-bc3ba76a3e19.png)

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

#### (2) INFO_LIST : 보고싶은 날씨 정보(커스텀)
- Type_no : Key attribute, 날씨정보를 식별하는 타입 번호
- Type : 날씨 정보 타입
    - ex) 강수, 풍속, 습도, 자외선, 일출/일몰 시간, 미세먼지, 온도

#### (3) REGION : 사용자가 날씨 조회/저장한 지역, Weak entity(USER가 없으면 REGION이 사라짐)
- Address : Key attribute, 사용자가 검색하는 지역의 주소
- Latitude : 검색한 주소의 위도
- Longitude : 검색한 주소의 경도
- X_coorinate : 검색한 위치의 X좌표(날씨 api를 사용하기 위해)
- Y_coorinate : 검색한 위치의 Y좌표(날씨 api를 사용하기 위해)

#### (4) WEATHER_RECORD : 날씨기록(날씨에 대한 의견), Weak entity(USER가 없으면 WEATHER_RECORD가 사라짐)
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

#### (2) CHOOSE
- USER는 꼭 INFO_LIST를 가져야함(Total participation)
- INFO_LIST는 USER와 상관없이 항상 존재함(Partial participation)
- M:N

#### (3) WRITE
- USER는 WEATHER_RECORD를 꼭 가지지 않아도 됨 (Partial participation)
- WEATHER_RECORD는 USER를 꼭 가져야함(Total participation)
- 1:N
- 식별관계

## 3. Relational diagram
![Weather_Relational drawio](https://user-images.githubusercontent.com/75112062/204085950-3ab6e17b-ca32-4b5a-8802-c0fa8ef54cf6.png)

#### REGION
- Uuid, Address : primary key
- Uuid : USERLIST의 Uniq_id를 참조

#### USERLIST (ER diagram의 USER)
- Uniq_id : primary key
- User_id : unique

#### CHOOSE
- Uuid, Type_num : primary key
- Uuid : USERLIST의 Uniq_id를 참조
- Type_num : INFO_LIST의 Type_no를 참조

#### INFO_LIST
- Type_no : primary key
- Type_name : unique

#### WEATHER_RECORD
- Uuid, Rdate : primary
- Uuid : USERLIST의 Uniq_id를 참조
