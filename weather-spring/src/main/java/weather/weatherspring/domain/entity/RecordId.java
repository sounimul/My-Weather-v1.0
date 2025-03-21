package weather.weatherspring.domain.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordId implements Serializable {
    @Column(name="Uuid")
    private Long uid;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime rdate;

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
