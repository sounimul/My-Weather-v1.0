package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CurrentWeather {
    private String t1h="";     // 기온
    private String rn1="";     //강수량
    private String reh="";     // 습도
    private String pty="";     // 강수형태
    private String sky="";     // 하늘 상태
    private String status="";
    private String icon="";

    public CurrentWeather(String[] current) {
        this.pty = current[0];
        this.reh = current[1];
        this.rn1 = current[2];
        this.t1h = current[3];
        this.sky = current[4];
        this.status = current[5];
        this.icon = current[6];
    }
}
