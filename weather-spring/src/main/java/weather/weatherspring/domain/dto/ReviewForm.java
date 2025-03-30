package weather.weatherspring.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {
    private int reviewStar;
    private String comment;
}
