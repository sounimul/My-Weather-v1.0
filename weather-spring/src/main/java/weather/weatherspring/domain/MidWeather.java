package weather.weatherspring.domain;

public class MidWeather {
    private String tmx="";
    private String tmn="";
    private String[] fcstTmx={"",""};
    private String[] fcstTmn={"",""};
    private String[] minName={"",""};
    private String[] maxName={"",""};
    private String[] weather = {"","","","","",""};
    private String[] icon = {"","","","","",""};

    public String getTmx() {
        return tmx;
    }

    public void setTmx(String tmx) {
        this.tmx = tmx;
    }

    public String getTmn() {
        return tmn;
    }

    public void setTmn(String tmn) {
        this.tmn = tmn;
    }

    public String[] getFcstTmx() {
        return fcstTmx;
    }

    public void setFcstTmx(String[] fcstTmx) {
        this.fcstTmx = fcstTmx;
    }

    public String[] getFcstTmn() {
        return fcstTmn;
    }

    public void setFcstTmn(String[] fcstTmn) {
        this.fcstTmn = fcstTmn;
    }

    public String[] getMinName() {
        return minName;
    }

    public void setMinName(String[] minName) {
        this.minName = minName;
    }

    public String[] getMaxName() {
        return maxName;
    }

    public void setMaxName(String[] maxName) {
        this.maxName = maxName;
    }

    public String[] getWeather() {
        return weather;
    }

    public void setWeather(String[] weather) {
        this.weather = weather;
    }

    public String[] getIcon() {
        return icon;
    }

    public void setIcon(String[] icon) {
        this.icon = icon;
    }
}
