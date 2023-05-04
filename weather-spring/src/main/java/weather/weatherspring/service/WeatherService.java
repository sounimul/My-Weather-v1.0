package weather.weatherspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import weather.weatherspring.domain.Wtype;
import weather.weatherspring.entity.ElementForm;
import weather.weatherspring.repository.WeatherRepository;

import java.util.Optional;

@Service
public class WeatherService {
    private static final String KMA_API_BASE_URL="http://apis.data.go.kr/1360000";
    private static final String KMA_SRT_NCST_URL="/VilageFcstInfoService_2.0/getUltraSrtNcst";        // 초단기실황
    private static final String KMA_SRT_FCST_URL="/VilageFcstInfoService_2.0/getUltraSrtFcst";          // 초단기예보
    private static final String KMA_VGE_FCST_URL="/VilageFcstInfoService_2.0/getVilageFcst";          // 단기예보
    private static final String DATA_API_KEY="gyaHQw8o7B6FzRy3woK7FUM4bVAm%2FSplTe8Rf8%2FQ%2BJSMJtOKUWKqFrVmz9uTwN9xIy%2BJJ7ryeRHFbI1LrKVQQQ%3D%3D";
    private final WebClient.Builder kmaWebClientBuilder;
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WebClient.Builder kmaWebClientBuilder, WeatherRepository weatherRepository) {
        this.kmaWebClientBuilder = kmaWebClientBuilder;
        this.weatherRepository = weatherRepository;
    }

    /* 단기예보 API 호출 */
    public Mono<JsonNode> getForecast(ElementForm elementForm, int num){
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
        String row="0";
        // 오늘 최고, 최저 기온
        if(num==0){
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
            time="2300";
        }
        // 3일치 최고, 최저 기온 및 날씨
        else{
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
                        .queryParam("nx",elementForm.getXcoor()+"")
                        .queryParam("ny",elementForm.getYcoor()+"")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class);
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
                .bodyToMono(JsonNode.class);
    }

    /* 초단기예보 API 호출 */
    public Mono<JsonNode> getForecast3(ElementForm elementForm,int num){
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

        /* 날짜, 시간 계산 */
        String date="";
        String time="";

        // 현재 날씨 + 1시간 뒤 날씨
        if(num==0){
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
        // 1시간 전 날씨
        else {
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
                .bodyToMono(JsonNode.class);
    }

    public String calDate(ElementForm ef, int h){   // h : 날짜가 바뀌는 시간
        int[] enddate={31,28,31,30,31,30,31,31,30,31,30,31};    // 월의 마지막 날
        if(ef.getYear()%4==0) enddate[1]=29;           // 윤년인 경우 2월 29일까지

        String date="";

        // 년이 바뀔 때 - 1월 1일 h(0~1)시
        if(ef.getMonth()==1 & ef.getDate()==1 & ef.getHour()<=h)
            date = (ef.getYear() - 1) + "1231";
        // 월이 바뀔 때 - 1일 h(0~1)시
        else if(ef.getDate()==1 & ef.getHour()<=h)
            date = ef.getYear() + String.format("%02d", ef.getMonth() - 1) + enddate[ef.getMonth() - 2];
        // 일이 바뀔 때 - h(0~1)시
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


}
