package weather.weatherspring.entity;

public class Temperature {
    private double tmx;
    private double tmn;
    private double[] fcstTmx;
    private double[] fcstTmn;
    private String[] pty;
    private String[] sky;
    private String[] status;

    public double getTmx() {
        return tmx;
    }

    public void setTmx(double tmx) {
        this.tmx = tmx;
    }

    public double getTmn() {
        return tmn;
    }

    public void setTmn(double tmn) {
        this.tmn = tmn;
    }

    public double[] getFcstTmx() {
        return fcstTmx;
    }

    public void setFcstTmx(double[] fcstTmx) {
        this.fcstTmx = fcstTmx;
    }

    public double[] getFcstTmn() {
        return fcstTmn;
    }

    public void setFcstTmn(double[] fcstTmn) {
        this.fcstTmn = fcstTmn;
    }

    public String[] getPty() {
        return pty;
    }

    public void setPty(String[] pty) {
        this.pty = pty;
    }

    public String[] getSky() {
        return sky;
    }

    public void setSky(String[] sky) {
        this.sky = sky;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }
}
