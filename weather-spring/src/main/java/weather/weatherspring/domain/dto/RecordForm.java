package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordForm {
    private String saveTempComment;
    private String saveHumidComment;
    private String saveRainComment;
}
