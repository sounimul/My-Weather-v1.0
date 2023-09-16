package weather.weatherspring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import weather.weatherspring.entity.Record;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordRepository{
    Record save(Record record);
    Optional<Record> findByUidAndRdate(Long uid, LocalDateTime rdate);
    List<Record> findByUid(Long uid);
//    List<Record> findAll(Specification<Record> spec, Sort sort);
    Page<Record> findAll(Specification<Record> spec, Pageable pageable);
    void deleteByUidAndRdate(Long uid, LocalDateTime rdate);
}
