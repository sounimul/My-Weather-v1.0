package weather.weatherspring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Wtype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String wcode;

    private String message;

    private String wname;
}
