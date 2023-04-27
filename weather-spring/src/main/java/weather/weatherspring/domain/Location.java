package weather.weatherspring.domain;

import jakarta.persistence.*;

@Entity     // JPA가 관리하는 entity
//@Table(name="region")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="Uuid")
    private Long uid;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="Address")
    private String ad;
    private Double latitude;
    private Double longitude;
    @Column(name="X_coor")
    private Long xcoor;
    @Column(name="Y_coor")
    private Long ycoor;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getXcoor() {
        return xcoor;
    }

    public void setXcoor(Long xcoor) {
        this.xcoor = xcoor;
    }

    public Long getYcoor() {
        return ycoor;
    }

    public void setYcoor(Long ycoor) {
        this.ycoor = ycoor;
    }
}
