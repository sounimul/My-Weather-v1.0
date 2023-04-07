package weather.weatherspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import weather.weatherspring.entity.ElementForm;


//@Transactional
@Service
public class LocationService {
    private final WebClient.Builder kakaoWebClientBuilder;
    private static final String KAKAO_API_BASE_URL="https://dapi.kakao.com";
    private static final String KAKAO_API_KEY="018a2a8e391ab5140cb2641061a56e11";

    public LocationService(WebClient.Builder kakaoWebClientBuilder) {
        this.kakaoWebClientBuilder = kakaoWebClientBuilder;
    }

    /* 위도와 경도를 통해 행정구역 정보를 가져옴 */
    public Mono<JsonNode> getAddress(ElementForm elementForm){
        WebClient kakaoWebClient = kakaoWebClientBuilder
                .baseUrl(KAKAO_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK "+KAKAO_API_KEY)
                .build();

        String apiUrl = String.format("/v2/local/geo/coord2regioncode.json?x=%s&y=%s",elementForm.getLongitude(),elementForm.getLatitude());

        return kakaoWebClient.get()
                .uri(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class);

    }

    /* 위도와 경도를 기상청 x,y좌표로 변환 */
    public ElementForm getXY(ElementForm elementForm){
        /* 단기예보 지도 정보 */
//        double RE = 6371.00877; // 지도반경
        double GRID = 5.0; // 격자간격 (km)
//        double SLAT1 = 30.0; // 표준위도 1
//        double SLAT2 = 60.0; // 표준위도 2
//        double OLON = 126.0; // 기준점 경도
//        double OLAT = 38.0; // 기준점 위도
        double XO = 210/GRID; // 기준점 X좌표
        double YO = 675/GRID; // 기준점 Y좌표

        double DEGRAD = Math.PI / 180.0;

        double re = 6371.00877 / GRID;
        double slat1 = 30.0 * DEGRAD;
        double slat2 = 60.0 * DEGRAD;
        double olon = 126.0 * DEGRAD;
        double olat = 38.0 * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf,sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro,sn);

        /* 위경도 -> (X,Y) */
        double ra = Math.tan(Math.PI * 0.25 + elementForm.getLatitude() * DEGRAD *0.5);
        ra = re * sf / Math.pow(ra,sn);
        double theta = elementForm.getLongitude() * DEGRAD - olon;
        if(theta > Math.PI) theta -= 2.0 * Math.PI;
        if(theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        double x = (int)(ra * Math.sin(theta) + XO)+1.5;
        double y = (int)(ro - ra*Math.cos(theta) + YO)+1.5;
        elementForm.setXcoor((long)x);
        elementForm.setYcoor((long)y);

        return elementForm;
    }

}
