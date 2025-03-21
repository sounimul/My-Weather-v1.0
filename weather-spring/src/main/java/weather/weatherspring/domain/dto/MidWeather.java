package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidWeather {
    private String tmx="";
    private String tmn="";
    private String[] fcstTmx={"",""};
    private String[] fcstTmn={"",""};
    private String[] minName={"",""};
    private String[] maxName={"",""};
    private String[] weather = {"","","","","",""};
    private String[] icon = {"","","","","",""};
}
