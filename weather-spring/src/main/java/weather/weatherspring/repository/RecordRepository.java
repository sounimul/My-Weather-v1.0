package weather.weatherspring.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import weather.weatherspring.domain.Record;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordRepository{
    Record save(Record record);
    Optional<Record> findByUidAndRdate(Long uid, LocalDateTime rdate);
    List<Record> findByUid(Long uid);
    List<Record> findAll(Specification<Record> spec, Sort sort);
    void deleteByUidAndRdate(Long uid, LocalDateTime rdate);
}
