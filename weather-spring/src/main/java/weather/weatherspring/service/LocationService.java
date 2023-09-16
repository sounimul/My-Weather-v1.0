package weather.weatherspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import weather.weatherspring.domain.ElementForm;


//@Transactional
@Service
public class LocationService {
    private final WebClient.Builder kakaoWebClientBuilder;
    private static final String KAKAO_API_BASE_URL="https://dapi.kakao.com";
    @Value("${KAKAO_API_KEY}")
    private String KAKAO_API_KEY;

    public LocationService(WebClient.Builder kakaoWebClientBuilder) {
        this.kakaoWebClientBuilder = kakaoWebClientBuilder;
    }

    /* 위도와 경도를 통해 행정구역 정보를 가져옴 */
    public String getAddress(ElementForm elementForm){
        WebClient kakaoWebClient = kakaoWebClientBuilder
                .baseUrl(KAKAO_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK "+KAKAO_API_KEY)
                .build();

        String apiUrl = String.format("/v2/local/geo/coord2regioncode.json?x=%s&y=%s",elementForm.getLongitude(),elementForm.getLatitude());

        Mono<JsonNode> response = kakaoWebClient.get()
                .uri(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class);

        return response.block().get("documents").get(1).get("address_name").asText();
        // get(0) : 법정동, get(1) : 행정동 -> 기상청은 행정동이 기준
    }

    /* 위도와 경도를 기상청 x,y좌표로 변환 */
    public ElementForm getXY(ElementForm elementForm){
        /* 단기예보 지도 정보 */
        double GRID = 5.0; // 격자간격 (km)
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

    /* 위치를 중기육상예보 구역으로 변환 */
    public String getAreaCode(String ad){
        String[] gangwon_east={"강릉", "동해", "속초", "삼척", "태백", "고성", "양양"};
        String[] gangwon_west={"철원", "화천", "양구", "인제", "춘천", "홍천", "횡성", "원주", "평창", "영월", "정선"};

        if (ad.startsWith("서울") | ad.startsWith("인천") | ad.startsWith("경기도"))
            return "11B00000";
        else if (ad.startsWith("대전") | ad.startsWith("세종") | ad.startsWith("충청남도"))
            return "11C20000";
        else if (ad.startsWith("충청북도"))
            return "11C10000";
        else if (ad.startsWith("광주") | ad.startsWith("전라남도"))
            return "11F20000";
        else if (ad.startsWith("전라북도"))
            return"11F10000";
        else if (ad.startsWith("대구") | ad.startsWith("경상북도"))
            return"11H10000";
        else if (ad.startsWith("부산") | ad.startsWith("울산") | ad.startsWith("경상남도"))
            return "11H20000";
        else if (ad.startsWith("제주도"))
            return "11G00000";
        else if (ad.startsWith("강원도")){
            // 강원도 영동
            for(String loc:gangwon_east){
                if (ad.startsWith("강원도 "+loc))
                    return "11D20000";
            }
            // 강원도 영서
            for(String loc:gangwon_west){
                if(ad.startsWith("강원도 "+loc))
                    return "11D10000";
            }
        }
        return "11B00000";
    }


}
