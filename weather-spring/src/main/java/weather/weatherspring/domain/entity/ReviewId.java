package weather.weatherspring.domain.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewId implements Serializable {
    private Long uuid;
    @Column(name="Rdate") @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime date;

    // equals() 구현
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return uuid.equals(reviewId.uuid) && date.equals(reviewId.date);
    }
    // hashCode() 구현
    @Override
    public int hashCode(){
        return Objects.hash(uuid,date);
    }

}
