package weather.weatherspring.entity;

import jakarta.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class RecordId implements Serializable {
    @Column(name="Uuid")
    private Long uid;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime rdate;

    // 기본 생성자 선언
    public RecordId() { }

    public RecordId(Long uid, LocalDateTime rdate) {
        this.uid = uid;
        this.rdate = rdate;
    }

    // equals() 구현
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        RecordId recordId = (RecordId) o;
        return uid.equals(recordId.uid) && rdate.equals(recordId.rdate);
    }
    // hashCode() 구현
    @Override
    public int hashCode(){
        return Objects.hash(uid,rdate);
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public LocalDateTime getRdate() {
        return rdate;
    }

    public void setRdate(LocalDateTime rdate) {
        this.rdate = rdate;
    }

}
