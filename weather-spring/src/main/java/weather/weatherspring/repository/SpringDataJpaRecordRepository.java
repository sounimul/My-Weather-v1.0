package weather.weatherspring.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.RecordId;

import java.time.LocalDateTime;
import java.util.List;


public interface SpringDataJpaRecordRepository extends JpaRepository<Record, RecordId>, RecordRepository, JpaSpecificationExecutor<Record> {
    @Override
    List<Record> findAll(Specification<Record> spec, Sort sort);
    @Override
    void deleteByUidAndRdate(Long uid, LocalDateTime rdate);
}
