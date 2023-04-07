package weather.weatherspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import weather.weatherspring.controller.ElementForm;

@Service
public class WeatherService {
    private static final String KMA_API_BASE_URL="http://apis.data.go.kr/1360000";
    private static final String DATA_API_KEY="gyaHQw8o7B6FzRy3woK7FUM4bVAm%2FSplTe8Rf8%2FQ%2BJSMJtOKUWKqFrVmz9uTwN9xIy%2BJJ7ryeRHFbI1LrKVQQQ%3D%3D";
    private final WebClient.Builder webClientBuilder;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /* 단기예보 */
    public Mono<String> getForecast(ElementForm elementForm){
        WebClient webClient = webClientBuilder
                .baseUrl(KMA_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String date=elementForm.getYear()+String.format("%02d",elementForm.getMonth())+String.format("%02d",elementForm.getDate());
        String time=String.format("%02d",elementForm.getHour())+"00";
        System.out.println(date+" "+time);
        String apiUrl = String.format("/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=%s&dataType=JSON&base_date=%s&base_time=%s&nx=%d&ny=%d"
                ,DATA_API_KEY,date,time,elementForm.getXcoor(),elementForm.getYcoor());

        return webClient.get()
                .uri(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    /* 과거 관측 */

}
