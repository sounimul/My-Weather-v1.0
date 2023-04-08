package weather.weatherspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;
import weather.weatherspring.entity.ElementForm;

@Service
public class WeatherService {
    private static final String KMA_API_BASE_URL="http://apis.data.go.kr/1360000";
    private static final String KMA_SRT_NCST_URL="/VilageFcstInfoService_2.0/getUltraSrtNcst";        // 초단기실황
    private static final String KMA_SRT_FCST_URL="/VilageFcstInfoService_2.0/getVilageFcst";          // 초단기예보
    private static final String KMA_VGE_FCST_URL="/VilageFcstInfoService_2.0/getVilageFcst";          // 단기예보


    private static final String DATA_API_KEY="gyaHQw8o7B6FzRy3woK7FUM4bVAm%2FSplTe8Rf8%2FQ%2BJSMJtOKUWKqFrVmz9uTwN9xIy%2BJJ7ryeRHFbI1LrKVQQQ%3D%3D";
    private final WebClient.Builder kmaWebClientBuilder;

    public WeatherService(WebClient.Builder kmaWebClientBuilder) {
        this.kmaWebClientBuilder = kmaWebClientBuilder;
    }

    /* 단기예보 */
    public Mono<JsonNode> getForecast(ElementForm elementForm){
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

        String date=elementForm.getYear()+String.format("%02d",elementForm.getMonth());
        String time="";
        if(elementForm.getHour()>=0 & elementForm.getHour()<2){
            date = date + String.format("%02d",elementForm.getDate()-1);
            time = "2300";
        }else{
            date = date + String.format("%02d",elementForm.getDate());
            if(elementForm.getHour()>=2 & elementForm.getHour()<5) time="0200";
            else if(elementForm.getHour()>=5 & elementForm.getHour()<8) time="0500";
            else if(elementForm.getHour()>=8 & elementForm.getHour()<11) time="0800";
            else if(elementForm.getHour()>=11 & elementForm.getHour()<14) time="1100";
            else if(elementForm.getHour()>=14 & elementForm.getHour()<17) time="1400";
            else if(elementForm.getHour()>=17 & elementForm.getHour()<20) time="1700";
            else if(elementForm.getHour()>=20 & elementForm.getHour()<23) time="2000";
            else time="2300";
        }

        String finalDate = date;
        String finalTime = time;

        return kmaWebClient.get()
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

    /* 초단기실황 */
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

        String date=elementForm.getYear()+String.format("%02d",elementForm.getMonth())+String.format("%02d",elementForm.getDate());
        String time=String.format("%02d",elementForm.getHour())+"00";

        return kmaWebClient2.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",DATA_API_KEY)
                        .queryParam("numOfRows","8")
                        .queryParam("pageNo","1")
                        .queryParam("dataType","JSON")
                        .queryParam("base_date",date)
                        .queryParam("base_time",time)
                        .queryParam("nx",elementForm.getXcoor()+"")
                        .queryParam("ny",elementForm.getYcoor()+"")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    /* 과거 관측 */

}
