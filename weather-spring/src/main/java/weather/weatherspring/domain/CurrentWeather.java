package weather.weatherspring.domain;

public class CurrentWeather {
    private String t1h="";     // 기온
    private String rn1="";     //강수량
    private String reh="";     // 습도
    private String pty="";     // 강수형태
    private String sky="";     // 하늘 상태
    private String status="";
    private String icon="";

    public String getT1h() {
        return t1h;
    }

    public void setT1h(String t1h) {
        this.t1h = t1h;
    }

    public String getRn1() {
        return rn1;
    }

    public void setRn1(String rn1) {
        this.rn1 = rn1;
    }

    public String getReh() {
        return reh;
    }

    public void setReh(String reh) {
        this.reh = reh;
    }

    public String getPty() {
        return pty;
    }

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
