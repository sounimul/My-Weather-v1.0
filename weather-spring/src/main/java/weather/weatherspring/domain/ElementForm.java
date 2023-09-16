package weather.weatherspring.domain;

public class ElementForm {
    private String ad;
    private Double latitude;
    private Double longitude;
    private Long xcoor;
    private Long ycoor;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int min;
    private int sec;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }
}
