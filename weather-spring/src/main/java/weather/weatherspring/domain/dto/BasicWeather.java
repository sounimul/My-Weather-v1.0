package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BasicWeather {
    private String pt1h="";
    private String ppty="";
    private String psky="";
    private String picon="";
    private String ft1h="";
    private String fpty="";
    private String fsky="";
    private String ficon="";

    public BasicWeather(String[] current, String[] past) {
        this.fpty = current[0];
        this.fsky = current[1];
        this.ft1h = current[2];
        this.ficon = current[3];
        this.ppty = past[0];
        this.psky = past[1];
        this.pt1h = past[2];
        this.picon = past[3];
    }
}
