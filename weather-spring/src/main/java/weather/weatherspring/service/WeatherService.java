package weather.weatherspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import weather.weatherspring.entity.Wtype;
import weather.weatherspring.domain.ElementForm;
import weather.weatherspring.repository.WeatherRepository;

import java.util.Optional;

@Service
public class WeatherService {
    private static final String KMA_API_BASE_URL="http://apis.data.go.kr/1360000";
    private static final String KMA_SRT_NCST_URL="/VilageFcstInfoService_2.0/getUltraSrtNcst";        // 초단기실황
    private static final String KMA_SRT_FCST_URL="/VilageFcstInfoService_2.0/getUltraSrtFcst";          // 초단기예보
    private static final String KMA_VGE_FCST_URL="/VilageFcstInfoService_2.0/getVilageFcst";          // 단기예보
    private static final String KMA_MIDLAND_FCST_URL="/MidFcstInfoService/getMidLandFcst";
    @Value("${DATA_API_KEY}")
    private String DATA_API_KEY;
    private final WebClient.Builder kmaWebClientBuilder;
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WebClient.Builder kmaWebClientBuilder, WeatherRepository weatherRepository) {
        this.kmaWebClientBuilder = kmaWebClientBuilder;
        this.weatherRepository = weatherRepository;
    }

    /* 단기예보 API 호출 */
    public Mono<JsonNode> getForecast(ElementForm elementForm,int option){
        String base_url = KMA_API_BASE_URL+KMA_VGE_FCST_URL;    // 단기예보

        // UriBuild 설정을 해주는 DefaultUriBuilderFactory class의 인스턴스 생성
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(base_url);
        // 인코딩 mode 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient kmaWebClient = kmaWebClientBuilder
                .uriBuilderFactory(factory)
                .baseUrl(base_url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        /* 날짜, 시간 계산 */
        int[] enddate={31,28,31,30,31,30,31,31,30,31,30,31};    // 월의 마지막 날
        if(elementForm.getYear()%4==0) enddate[1]=29;           // 윤년인 경우 2월 29일까지
        String date="";
        String time="";
        String row="";
        // 최고, 최저 기온 datetime
        if (option == 0){
            time="2300";
            row="290";
            // 년이 바뀔 때 - 1월 1일
            if(elementForm.getMonth()==1 & elementForm.getDate()==1)
                date = (elementForm.getYear() - 1) + "1231";
            // 월이 바뀔 때 - 1일
            else if(elementForm.getDate()==1)
                date = elementForm.getYear() + String.format("%02d", elementForm.getMonth() - 1) + enddate[elementForm.getMonth() - 2];
            // 년,월이 바뀌지 않을 때
            else
                date=elementForm.getYear()+String.format("%02d",elementForm.getMonth())+String.format("%02d",elementForm.getDate()-1);
        }
        // 1~2일 예보 datetime
        else if (option == 1){
            row="870";
            // 0~1시
            if(elementForm.getHour()>=0 & elementForm.getHour()<2){
                // 년이 바뀔 때 - 1월 1일
                if(elementForm.getMonth()==1 & elementForm.getDate()==1)
                    date = (elementForm.getYear() - 1) + "1231";
                    // 월이 바뀔 때 - 1일
                else if(elementForm.getDate()==1)
                    date = elementForm.getYear() + String.format("%02d", elementForm.getMonth() - 1) + enddate[elementForm.getMonth() - 2];
                    // 년,월이 바뀌지 않을 때
                else
                    date=elementForm.getYear()+String.format("%02d",elementForm.getMonth())+String.format("%02d",elementForm.getDate()-1);
                time = "2300";
            }
            // 2~23시
            else{
                date = elementForm.getYear() + String.format("%02d",elementForm.getMonth()) + String.format("%02d",elementForm.getDate());
                if(elementForm.getHour()>=2 & elementForm.getHour()<5) time="0200";
                else if(elementForm.getHour()>=5 & elementForm.getHour()<8) time="0500";
                else if(elementForm.getHour()>=8 & elementForm.getHour()<11) time="0800";
                else if(elementForm.getHour()>=11 & elementForm.getHour()<14) time="1100";
                else if(elementForm.getHour()>=14 & elementForm.getHour()<17) time="1400";
                else if(elementForm.getHour()>=17 & elementForm.getHour()<20) time="1700";
                else if(elementForm.getHour()>=20 & elementForm.getHour()<23) time="2000";
                else time="2300";
            }
        }

        String finalDate = date;
        String finalTime = time;
        String finalRow = row;
        return kmaWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",DATA_API_KEY)
                        .queryParam("numOfRows", finalRow)
                        .queryParam("pageNo","1")
                        .queryParam("dataType","JSON")
                        .queryParam("base_date", finalDate)
                        .queryParam("base_time", finalTime)
                        .queryParam("nx",elementForm.getXcoor().toString())
                        .queryParam("ny",elementForm.getYcoor().toString())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .subscribeOn(Schedulers.parallel());
    }

    /* 초단기실황 API 호출 */
    public Mono<JsonNode> getForecast2(ElementForm elementForm){
        String base_url2=KMA_API_BASE_URL+KMA_SRT_NCST_URL;    // 단기예보

        // UriBuild 설정을 해주는 DefaultUriBuilderFactory class의 인스턴스 생성
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(base_url2);
        // 인코딩 mode 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient kmaWebClient2 = kmaWebClientBuilder
                .uriBuilderFactory(factory)
                .baseUrl(base_url2)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        /* 날짜,시간 계산
        * 40분을 기준으로 실황 API가 발표됨 */
        String date="";
        String time="";
        // 40분이후 -> 연월일시간 모두 그대로
        if(elementForm.getMin()>=40){
            date=elementForm.getYear()+String.format("%02d",elementForm.getMonth())+String.format("%02d",elementForm.getDate());
            time=String.format("%02d",elementForm.getHour())+"00";
        }
        // 40분 이전 + 날짜가 바뀔 때(0시)
        else if(elementForm.getHour()==0) {
            date=calDate(elementForm,0);
            time="2300";
        }
        // 40분 이전 + 날짜가 바뀌지 않을 때(1~23시)
        else {
            date = elementForm.getYear() + String.format("%02d", elementForm.getMonth()) + String.format("%02d", elementForm.getDate());
            time = String.format("%02d", elementForm.getHour() - 1) + "00";
        }

        String finalTime = time;
        String finalDate = date;
        return kmaWebClient2.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",DATA_API_KEY)
                        .queryParam("numOfRows","8")
                        .queryParam("pageNo","1")
                        .queryParam("dataType","JSON")
                        .queryParam("base_date", finalDate)
                        .queryParam("base_time", finalTime)
                        .queryParam("nx",elementForm.getXcoor()+"")
                        .queryParam("ny",elementForm.getYcoor()+"")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .subscribeOn(Schedulers.parallel());
    }

    /* 초단기예보 API 호출 */
    public Mono<JsonNode> getForecast3(ElementForm elementForm,int option){
        String base_url3=KMA_API_BASE_URL+KMA_SRT_FCST_URL;    // 단기예보

        // UriBuild 설정을 해주는 DefaultUriBuilderFactory class의 인스턴스 생성
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(base_url3);
        // 인코딩 mode 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient kmaWebClient3 = kmaWebClientBuilder
                .uriBuilderFactory(factory)
                .baseUrl(base_url3)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        /* 날짜 계산 */
        String date="";
        String time="";
        // 1시간 전 datetime
        if (option==0){
            // 날짜가 바뀔 때 (0~1시)
            if(elementForm.getHour()<=1){
                date=calDate(elementForm,1);
                time = (22 + elementForm.getHour()) + "30";
            }
            // 날짜가 바뀌지 않을 때 (2~23시)
            else{
                date=elementForm.getYear()+String.format("%02d",elementForm.getMonth())+String.format("%02d",elementForm.getDate());
                time=String.format("%02d",elementForm.getHour()-2)+"30";
            }
        }
        // 현재, 1시간 후 datetime
        else if (option == 1){
            // 날짜가 바뀔 때 (0시)
            if(elementForm.getHour()==0){
                date=calDate(elementForm,0);
                time="2330";
            }
            // 날짜가 바뀌지 않을 때(1~23시)
            else{
                date=elementForm.getYear()+String.format("%02d",elementForm.getMonth())+String.format("%02d",elementForm.getDate());
                time=String.format("%02d",elementForm.getHour()-1)+"30";
            }
        }

        String finalDate = date;
        String finalTime = time;
        return kmaWebClient3.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",DATA_API_KEY)
                        .queryParam("numOfRows","60")
                        .queryParam("pageNo","1")
                        .queryParam("dataType","JSON")
                        .queryParam("base_date", finalDate)
                        .queryParam("base_time", finalTime)
                        .queryParam("nx",elementForm.getXcoor()+"")
                        .queryParam("ny",elementForm.getYcoor()+"")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .subscribeOn(Schedulers.parallel());
    }

    /* 3~5일 중기예보 - 중기예보 API 호출 */
    @Cacheable(value = "midForecast", key ="#elementForm.year.toString()+'-'+#elementForm.month.toString()+'-'+#elementForm.date.toString()+'-'+#regId")
    public Mono<JsonNode> getMidForecast(ElementForm elementForm, String regId){
        System.out.println("중기예보 호출-api");
        String base_url4=KMA_API_BASE_URL+KMA_MIDLAND_FCST_URL;    // 중기 육상 예보

        // UriBuild 설정을 해주는 DefaultUriBuilderFactory class의 인스턴스 생성
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(base_url4);
        // 인코딩 mode 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient kmaWebClient4 = kmaWebClientBuilder
                .uriBuilderFactory(factory)
                .baseUrl(base_url4)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        /* 날짜, 시간 계산 */
        String datetime=calDate(elementForm,5) + ((elementForm.getHour()>=6 & elementForm.getHour()<18) ? "0600" : "1800");

        String finalDatetime = datetime;

        return kmaWebClient4.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",DATA_API_KEY)
                        .queryParam("numOfRows","50")
                        .queryParam("pageNo","1")
                        .queryParam("dataType","JSON")
                        .queryParam("regId", regId)
                        .queryParam("tmFc", finalDatetime)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .subscribeOn(Schedulers.parallel());
    }

    /* 최고, 최저기온 예보 데이터 변환 */
    public String[] jsonToMaxMinTemp(JsonNode response){
        String[] tmnTmx = {"",""};

        for(int i=0;i<290;i++){
            String cate=response.get("response").get("body").get("items").get("item").get(i).get("category").asText();
            if(cate.equals("TMN"))
                tmnTmx[1] = response.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
            else if(cate.equals("TMX"))
                tmnTmx[0] = response.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
        }

        return tmnTmx;
    }

    /* 1~2일 예보 데이터 변환 */
    public String[][] jsonToTwoDayFcst(JsonNode response, ElementForm elementForm){
        Wtype wtype = new Wtype();      // 하늘상태 + 강수형태

        // 2일치 최고, 최저기온, 날씨
        String[][] twoDayFcst = {{"",""},{"",""},{"",""},{"",""}};  // fcstTmx(0), fcstTmn(1), maxName(2), minName(3)
        String p=""; String s="";
        String todaydate=elementForm.getYear() + String.format("%02d",elementForm.getMonth()) + String.format("%02d",elementForm.getDate());
        int j=0,k=0;
        int total=response.get("response").get("body").get("totalCount").asInt();
        for(int i=0;i<870;i++){
            if (i >= total)
                break;
            // 오늘 날짜 pass
            if(response.get("response").get("body").get("items").get("item").get(i).get("fcstDate").asText().equals(todaydate))
                continue;
            // 카테고리 확인
            String cate = response.get("response").get("body").get("items").get("item").get(i).get("category").asText();
            // 카테고리가 pty, sky -> 일단 저장
            if(cate.equals("PTY")) p = response.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
            else if(cate.equals("SKY")) s = response.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
            //최저, 최고 기온 찾기
            if(cate.equals("TMN")){
                twoDayFcst[1][j]=response.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
                if (p.equals("0")) wtype.setWcode("SKY_"+s);
                else wtype.setWcode("PTY_"+p);
                twoDayFcst[3][j] = findWtype(wtype.getWcode()).get().getWname();
                j++;
            }
            else if(cate.equals("TMX")){
                twoDayFcst[0][k]=response.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
                if (p.equals("0")) wtype.setWcode("SKY_"+s);
                else wtype.setWcode("PTY_"+p);
                twoDayFcst[2][k] = findWtype(wtype.getWcode()).get().getWname();
                k++;
            }
            if(j==2&k==2)
                break;
        }

        return twoDayFcst;
    }

    /* 현재, 1시간후 예보 데이터 변환 */
    public String[][] jsonToCurFutFcst(JsonNode srtNcst, JsonNode srtFcst){
        String[][] curFutFcst = {
                {"","","","","","",""},
                {"","","",""}};
        Wtype wtype = new Wtype();

        /* 현재 날씨 - 초단기실황 + 초단기예보(현재 하늘상태) */
        curFutFcst[0][0] = srtNcst.get("response").get("body").get("items").get("item").get(0).get("obsrValue").asText();   // pty - 현재 강수상태
        curFutFcst[0][1] = srtNcst.get("response").get("body").get("items").get("item").get(1).get("obsrValue").asText();   // reh - 현재 습도
        curFutFcst[0][2] = srtNcst.get("response").get("body").get("items").get("item").get(2).get("obsrValue").asText();   // rn1 - 현재 강수량
        curFutFcst[0][3] = srtNcst.get("response").get("body").get("items").get("item").get(3).get("obsrValue").asText();   // t1h - 현재 기온
        curFutFcst[0][4] = srtFcst.get("response").get("body").get("items").get("item").get(18).get("fcstValue").asText();  // sky - 현재 하늘상태
        wtype.setWcode(curFutFcst[0][0].equals("0") ? "SKY_"+curFutFcst[0][4] : "PTY_"+curFutFcst[0][0]);
        wtype = findWtype(wtype.getWcode()).get();
        curFutFcst[0][5] = wtype.getMessage();
        curFutFcst[0][6] = wtype.getWname();

        /* 1시간 뒤 날씨 */
        curFutFcst[1][0] = srtFcst.get("response").get("body").get("items").get("item").get(7).get("fcstValue").asText();   // pty
        curFutFcst[1][1] = srtFcst.get("response").get("body").get("items").get("item").get(19).get("fcstValue").asText();  // sky
        curFutFcst[1][2] = srtFcst.get("response").get("body").get("items").get("item").get(25).get("fcstValue").asText();  // t1h
        wtype.setWcode(curFutFcst[1][0].equals("0") ? "SKY_"+curFutFcst[1][1] : "PTY_"+curFutFcst[1][0]);
        curFutFcst[1][3] = findWtype(wtype.getWcode()).get().getWname();

        return curFutFcst;
    }

    /* 1시간전 예보 데이터 변환 */
    public String[] jsonToPastFcst(JsonNode response){
        String[] pastFcst = {"","","",""};     // pty, sky, t1h, icon
        Wtype wtype = new Wtype();

        // 1시간 전 날씨 가져오기
        pastFcst[0] = response.get("response").get("body").get("items").get("item").get(6).get("fcstValue").asText();   // ppty
        pastFcst[1] = response.get( "response").get("body").get("items").get("item").get(18).get("fcstValue").asText(); // psky
        pastFcst[2] = response.get("response").get("body").get("items").get("item").get(24).get("fcstValue").asText(); // pt1h
        wtype.setWcode((pastFcst[0].equals("0") ? "SKY_"+pastFcst[1] : "PTY_"+pastFcst[0]));
        pastFcst[3] = findWtype(wtype.getWcode()).get().getWname();

        return pastFcst;
    }

    /* 중기예보 데이터 변환 */
    public String[][] jsonToMidFcst(ElementForm elementForm, JsonNode response){
        String[][] mid3days = new String[2][6];

        if(elementForm.getHour()<6){    // 4,5,6일 후 자료 가져오기
            mid3days[0][0] = response.get("response").get("body").get("items").get("item").get(0).get("wf4Am").asText();
            mid3days[0][1] = response.get("response").get("body").get("items").get("item").get(0).get("wf4Pm").asText();
            mid3days[0][2] = response.get("response").get("body").get("items").get("item").get(0).get("wf5Am").asText();
            mid3days[0][3] = response.get("response").get("body").get("items").get("item").get(0).get("wf5Pm").asText();
            mid3days[0][4] = response.get("response").get("body").get("items").get("item").get(0).get("wf6Am").asText();
            mid3days[0][5] = response.get("response").get("body").get("items").get("item").get(0).get("wf6Pm").asText();
        } else{     // 3,4,5일 후 자료 가져오기
            mid3days[0][0] = response.get("response").get("body").get("items").get("item").get(0).get("wf3Am").asText();
            mid3days[0][1] = response.get("response").get("body").get("items").get("item").get(0).get("wf3Pm").asText();
            mid3days[0][2] = response.get("response").get("body").get("items").get("item").get(0).get("wf4Am").asText();
            mid3days[0][3] = response.get("response").get("body").get("items").get("item").get(0).get("wf4Pm").asText();
            mid3days[0][4] = response.get("response").get("body").get("items").get("item").get(0).get("wf5Am").asText();
            mid3days[0][5] = response.get("response").get("body").get("items").get("item").get(0).get("wf5Pm").asText();
        }
        for (int i=0; i<6; i++)
            mid3days[1][i]= getIcon(mid3days[0][i]);

        return mid3days;
    }

    public String calDate(ElementForm ef, int h){   // h : 날짜가 바뀌는 시간
        int[] enddate={31,28,31,30,31,30,31,31,30,31,30,31};    // 월의 마지막 날
        if(ef.getYear()%4==0) enddate[1]=29;           // 윤년인 경우 2월 29일까지
        String date="";

        // 년이 바뀔 때 - 1월 1일 h시 이전
        if(ef.getMonth()==1 & ef.getDate()==1 & ef.getHour()<=h)
            date = (ef.getYear() - 1) + "1231";
        // 월이 바뀔 때 - 1일 h시 이전
        else if(ef.getDate()==1 & ef.getHour()<=h)
            date = ef.getYear() + String.format("%02d", ef.getMonth() - 1) + enddate[ef.getMonth() - 2];
        // 일이 바뀔 때 - h시 이전
        else if(ef.getHour()<=h)
            date = ef.getYear() + String.format("%02d", ef.getMonth()) + String.format("%02d", ef.getDate() - 1);
        // 날짜가 바뀌지 않을 때
        else
            date = ef.getYear() + String.format("%02d", ef.getMonth()) + String.format("%02d", ef.getDate());

        return date;
    }

    public Optional<Wtype> findWtype(String wcode){
        return weatherRepository.findByWcode(wcode);
    }

    public String getIcon(String wea){
        String icon="";
        if(wea.equals("맑음")) icon="sunny";
        else if(wea.equals("구름많음")) icon="partly_cloudy_day";
        else if(wea.equals("흐림")) icon="cloudy";
        else if(wea.equals("구름많고 비") | wea.equals("흐리고 비")) icon="rainy";
        else if(wea.equals("구름많고 눈") | wea.equals("흐리고 눈")) icon="weather_snowy";
        else if(wea.equals("구름많고 비/눈") | wea.equals("흐리고 비/눈")) icon="weather_mix";
        else if(wea.equals("구름많고 소나기") | wea.equals("흐리고 소나기")) icon="rainy_heavy";

        return icon;
    }

}
