-- WEATHER 사이트 fake data

-- DELETE
-- DELETE FROM USERLIST;
-- DELETE FROM REGION;
-- DELETE FROM WEATHER_RECORD;

-- userlist
INSERT INTO USERLIST VALUES ('syj000','abcd1234',CONCAT('9993','_syj000'),'쏭','맑은 날씨가 좋아용~', 'Y');
INSERT INTO USERLIST VALUES ('eljfa1324','awgeae',CONCAT('9994','_eljfa1324'),'떡볶이','', 'Y');
INSERT INTO USERLIST VALUES ('eawifoj58','agw32y',CONCAT('9995','_eawifoj58'),'스트롱','비는 싫어요', 'Y');
INSERT INTO USERLIST VALUES ('rkskek','rkskek0987',CONCAT('9996','_rkskek'),'비슬산','', 'Y');
INSERT INTO USERLIST VALUES ('happygirl','gkgkgk201@',CONCAT('9997','_happygirl'),'해피','해피해피', 'Y');
INSERT INTO USERLIST VALUES ('rltkdcjd22','skfTlakwcnj!!',CONCAT('9998','_rltkdcjd22'),'기상청','날씨 제발 맞춰줘', 'Y');
INSERT INTO USERLIST VALUES ('hahaha','zmzmgkgk21!',CONCAT('9999','_hahaha'),'라떼','', 'Y');
commit;

-- REGION
INSERT INTO REGION VALUES ('9993_syj000','대구광역시 달서구 월서로 51',35.803056011965765,128.54775218132332,88,89);
INSERT INTO REGION VALUES ('9993_syj000','대구광역시 중구 동성로 2길 80',35.8474679567114, 128.60617151756293,89, 90);
INSERT INTO REGION VALUES ('9995_eawifoj58','서울특별시 송파구 한가람로 65',37.53255107601075, 127.10492959222863,62, 126);
INSERT INTO REGION VALUES ('9997_happygirl','부산광역시 수영구 광안해변로 361',35.14340833925269, 129.13889854002562,99, 75);
INSERT INTO REGION VALUES ('9998_rltkdcjd22','서울특별시 서초구 신반포로 270',37.487647434589, 126.98791624580888,60, 125);
INSERT INTO REGION VALUES ('9999_hahaha','대구광역시 북구 산격3동',35.89336253396866, 128.60801623372723,89, 91);
COMMIT;

-- WEATHER_RECORD - (매우춥다-춥다-쌀쌀하다-시원하다-따뜻하다-약간덥다-덥다-매우덥다)
INSERT INTO WEATHER_RECORD VALUES ('9993_syj000','2023-02-15','대구광역시 달서구 월서로 51','쌀쌀하다',5.7,1.4);
INSERT INTO WEATHER_RECORD VALUES ('9993_syj000','2023-02-23','대구광역시 중구 동성로 2길 80','따뜻하다',12.8,3.5);
INSERT INTO WEATHER_RECORD VALUES ('9995_eawifoj58','2023-02-09','서울특별시 송파구 한가람로 65','쌀쌀하다',9.9,-2.8);
INSERT INTO WEATHER_RECORD VALUES ('9997_happygirl','2023-02-19','부산광역시 수영구 광안해변로 361','시원하다',14.8,4.5);
INSERT INTO WEATHER_RECORD VALUES ('9998_rltkdcjd22','2023-02-02','서울특별시 서초구 신반포로 270','춥다',1.4,-5.1);
COMMIT;




