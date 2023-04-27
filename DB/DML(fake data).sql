-- WEATHER 사이트 fake data

-- DELETE
-- DELETE FROM USERLIST;
-- DELETE FROM REGION;
-- DELETE FROM WEATHER_RECORD;

-- userlist, RECORD 생략

-- WTYPE
INSERT INTO WTYPE VALUES ('SKY_1','맑음','sunny');
INSERT INTO WTYPE VALUES ('SKY_3','구름 많음','partly_cloudy_day');
INSERT INTO WTYPE VALUES ('SKY_4','흐림','cloudy');
INSERT INTO WTYPE VALUES ('PTY_1','비','rainy');
INSERT INTO WTYPE VALUES ('PTY_2','비 또는 눈','weather_mix');
INSERT INTO WTYPE VALUES ('PTY_3','눈','weather_snowy');
INSERT INTO WTYPE VALUES ('PTY_4','소나기','rainy_heavy');
INSERT INTO WTYPE VALUES ('PTY_5','빗방울','rainy');
INSERT INTO WTYPE VALUES ('PTY_6','빗방울눈날림','weather_mix');
INSERT INTO WTYPE VALUES ('PTY_7','눈날림','weather_snowy');

-- WEATHER_RECORD - (매우춥다-춥다-쌀쌀하다-시원하다-따뜻하다-약간덥다-덥다-매우덥다)
INSERT INTO WEATHER_RECORD VALUES ('1','2023-02-15 15:35:24','대구광역시 달서구 상인동','SKY_3',5.7,'쌀쌀해요',20,'건조해요',0.0,'안와요');
INSERT INTO WEATHER_RECORD VALUES ('2','2023-02-23 10:24:36','대구광역시 중구 삼덕동','SKY_1',12.8,'선선해요',40,'쾌적해요',0.0,'안와요');
INSERT INTO WEATHER_RECORD VALUES ('2','2023-04-20 12:11:14','서울특별시 송파구 석촌동','SKY_4',20.2,'따뜻해요',50,'쾌적해요',0.0,'안와요');
INSERT INTO WEATHER_RECORD VALUES ('2','2023-04-26 17:01:00','부산광역시 수영구 민락동','PTY_1',18.1,'시원해요',80,'습해요',0.3,'보슬');
INSERT INTO WEATHER_RECORD VALUES ('10','2023-01-21 11:25:33','서울특별시 서초구 서초동','PTY_2',-2.2,'추워요',70,'습해요',1.0,'추적');
INSERT INTO WEATHER_RECORD VALUES ('10','2023-01-21 11:25:58','서울특별시 서초구 서초동','PTY_2',-2.2,'추워요',70,'습해요',1.1,'쏟아져요');

COMMIT;
