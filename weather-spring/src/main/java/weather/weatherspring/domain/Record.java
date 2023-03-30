package weather.weatherspring.domain;

import jakarta.persistence.*;

@Entity     // JPA가 관리하는 entity
@Table(name="weather_record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="Uuid")
    private Long uid;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="Rdate")
    private String Rdate;
    @Column(name="Address")
    private String ad;
    @Column(name="Feeling")
    private String feel;
    private double Tmax;
    private double Tmin;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getRdate() {
        return Rdate;
    }

    public void setRdate(String rdate) {
        Rdate = rdate;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel;
    }

    public double getTmax() {
        return Tmax;
    }

    public void setTmax(double tmax) {
        Tmax = tmax;
    }

    public double getTmin() {
        return Tmin;
    }

    public void setTmin(double tmin) {
        Tmin = tmin;
    }
}
