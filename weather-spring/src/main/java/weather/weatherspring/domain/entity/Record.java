package weather.weatherspring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity     // JPA가 관리하는 entity
@Table(name="weather_record")
@IdClass(RecordId.class)
@Getter
@Setter
public class Record implements Serializable {
    @Id @Column(name="Uuid")
    private Long uid;
    @Id @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime rdate;
    private String rmd;
    @Column(name="Address")
    private String ad;
    private String wmsg;
    private Double temp;
    private String tfeel;
    @Column(name="Humidity")
    private int humid;
    private String hfeel;
    @Column(name="Precipitation")
    private Double precip;
    private String pfeel;
}
