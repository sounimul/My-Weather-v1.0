package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MidWeather {
    private String tmx="";
    private String tmn="";
    private String[] fcstTmx={"",""};
    private String[] fcstTmn={"",""};
    private String[] minName={"",""};
    private String[] maxName={"",""};
    private String[] weather = {"","","","","",""};
    private String[] icon = {"","","","","",""};

    public MidWeather(String[] tmnTmx, String[][] twoDay, String[][] midFcst) {
        this.tmx = tmnTmx[0];
        this.tmn = tmnTmx[1];
        this.fcstTmx = twoDay[0];
        this.fcstTmn = twoDay[1];
        this.maxName = twoDay[2];
        this.minName = twoDay[3];
        this.weather = midFcst[0];
        this.icon = midFcst[1];
    }
}
