package weather.weatherspring.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Wtype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String wcode;

    private String message;

    private String wname;

    public String getWcode() {
        return wcode;
    }

    public void setWcode(String wcode) {
        this.wcode = wcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }
}
