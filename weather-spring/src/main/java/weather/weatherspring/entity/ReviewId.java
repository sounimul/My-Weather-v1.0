package weather.weatherspring.entity;

import jakarta.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ReviewId implements Serializable {
    private Long uuid;
    @Column(name="Rdate") @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime date;

    // 기본 생성자 선언
    public ReviewId(){ }

    public ReviewId(Long uuid, LocalDateTime date) {
        this.uuid = uuid;
        this.date = date;
    }

    // equals() 구현
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return uuid.equals(reviewId.uuid) && date.equals(reviewId.date);
    }
    // hashCode() 구현
    @Override
    public int hashCode(){
        return Objects.hash(uuid,date);
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
