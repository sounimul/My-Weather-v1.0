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
    private String rdate;
    @Column(name="Address")
    private String ad;
    private String wcode;
    private Double temp;
    private String tfeel;
    @Column(name="Humidity")
    private int humid;
    private String hfeel;
    @Column(name="Precipitaion")
    private Double precip;
    private String pfeel;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getWcode() {
        return wcode;
    }

    public void setWcode(String wcode) {
        this.wcode = wcode;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getTfeel() {
        return tfeel;
    }

    public void setTfeel(String tfeel) {
        this.tfeel = tfeel;
    }

    public int getHumid() {
        return humid;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }

    public String getHfeel() {
        return hfeel;
    }

    public void setHfeel(String hfeel) {
        this.hfeel = hfeel;
    }

    public Double getPrecip() {
        return precip;
    }

    public void setPrecip(Double precip) {
        this.precip = precip;
    }

    public String getPfeel() {
        return pfeel;
    }

    public void setPfeel(String pfeel) {
        this.pfeel = pfeel;
    }
}
