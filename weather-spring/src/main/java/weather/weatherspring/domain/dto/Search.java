package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Search {
    private Double startTemp;
    private Double endTemp;
    private Integer startHumid;
    private Integer endHumid;
    private Double startPrep;
    private Double endPrep;
}
