package weather.weatherspring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table
@IdClass(ReviewId.class)
@Getter
@Setter
public class Review {
    @Id
    private Long uuid;
    @Id @Column(name="Rdate") @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime date;
    private int stars;
    private String comments;
}
