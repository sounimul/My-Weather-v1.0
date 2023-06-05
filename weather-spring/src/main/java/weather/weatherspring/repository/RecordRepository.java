package weather.weatherspring.repository;

import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.RecordId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordRepository{
    Record save(Record record);
    Optional<Record> findByUidAndRdate(Long uid, LocalDateTime rdate);
    List<Record> findByUid(Long uid);
    void deleteByUidAndRdate(Long uid, LocalDateTime rdate);
//public interface RecordRepository extends JpaRepository<Record, RecordId>, JpaSpecificationExecutor<Record> {
//    List<Record> findAll(Specification<Record> spec, Sort sort);
}
