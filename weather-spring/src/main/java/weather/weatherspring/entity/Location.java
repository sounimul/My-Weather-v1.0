package weather.weatherspring.entity;


public class Location {
    private Long uid;
    private String ad;
    private Double latitude;
    private Double longitude;
    private Long xcoor;
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
