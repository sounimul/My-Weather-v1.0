package weather.weatherspring.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity     // JPA가 관리하는 entity
@Table(name="userlist")
@Getter
@Setter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="Uniq_id")
    private Long uid;
    @Column(name="User_id")
    private String id;
    private String pw;
    private String nickname;
    @Column(name="Fvweather")
    private String fvweather;
    @Column(name="Available")
    private String avail;
}
