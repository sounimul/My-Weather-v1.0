package weather.weatherspring.entity;

import jakarta.persistence.Column;

public class RecordForm {
    private String rdate;
    private String address;
    private String wmsg;
    private Double temperature;
    private String saveTempComment;
    private int humidity;
    private String saveHumidComment;
    private Double precipitation;
    private String saveRainComment;

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWmsg() {
        return wmsg;
    }

    public void setWmsg(String wmsg) {
        this.wmsg = wmsg;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getSaveTempComment() {
        return saveTempComment;
    }

    public void setSaveTempComment(String saveTempComment) {
        this.saveTempComment = saveTempComment;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getSaveHumidComment() {
        return saveHumidComment;
    }

    public void setSaveHumidComment(String saveHumidComment) {
        this.saveHumidComment = saveHumidComment;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public String getSaveRainComment() {
        return saveRainComment;
    }

    public void setSaveRainComment(String saveRainComment) {
        this.saveRainComment = saveRainComment;
    }
}
