package weather.weatherspring.domain;

public class Search {
    private Double startTemp;
    private Double endTemp;
    private Integer startHumid;
    private Integer endHumid;
    private Double startPrep;
    private Double endPrep;

    public Double getStartTemp() {
        return startTemp;
    }

    public void setStartTemp(Double startTemp) {
        this.startTemp = startTemp;
    }

    public Double getEndTemp() {
        return endTemp;
    }

    public void setEndTemp(Double endTemp) {
        this.endTemp = endTemp;
    }

    public Integer getStartHumid() {
        return startHumid;
    }

    public void setStartHumid(Integer startHumid) {
        this.startHumid = startHumid;
    }

    public Integer getEndHumid() {
        return endHumid;
    }

    public void setEndHumid(Integer endHumid) {
        this.endHumid = endHumid;
    }

    public Double getStartPrep() {
        return startPrep;
    }

    public void setStartPrep(Double startPrep) {
        this.startPrep = startPrep;
    }

    public Double getEndPrep() {
        return endPrep;
    }

    public void setEndPrep(Double endPrep) {
        this.endPrep = endPrep;
    }
}
