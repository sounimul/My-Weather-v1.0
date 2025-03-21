package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentWeather {
    private String t1h="";     // 기온
    private String rn1="";     //강수량
    private String reh="";     // 습도
    private String pty="";     // 강수형태
    private String sky="";     // 하늘 상태
    private String status="";
    private String icon="";
}
