package weather.weatherspring.domain;

public class Weather {
    private Long latitude;
    private Long longitude;
    private Long xcoor;
    private Long ycoor;

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
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
