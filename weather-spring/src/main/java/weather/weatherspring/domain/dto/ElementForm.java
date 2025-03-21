package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementForm {
    private String ad;
    private Double latitude;
    private Double longitude;
    private Long xcoor;
    private Long ycoor;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int min;
    private int sec;
}
