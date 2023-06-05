package weather.weatherspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.RecordId;

import java.time.LocalDateTime;


public interface SpringDataJpaRecordRepository extends JpaRepository<Record, RecordId>, RecordRepository {
    @Override
    void deleteByUidAndRdate(Long uid, LocalDateTime rdate);
}
