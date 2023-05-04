package weather.weatherspring.domain;

import java.io.Serializable;
import java.util.Objects;

public class RecordId implements Serializable {
    private Long uid;
    private String rdate;

    // 기본 생성자 선언
    public RecordId() { }

    public RecordId(Long uid, String rdate) {
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
}
