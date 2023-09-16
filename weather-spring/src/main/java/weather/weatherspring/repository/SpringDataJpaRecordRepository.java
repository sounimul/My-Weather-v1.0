package weather.weatherspring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import weather.weatherspring.entity.Record;
import weather.weatherspring.entity.RecordId;

import java.time.LocalDateTime;


public interface SpringDataJpaRecordRepository extends JpaRepository<Record, RecordId>, RecordRepository, JpaSpecificationExecutor<Record> {
//    @Override
//    List<Record> findAll(Specification<Record> spec, Sort sort);
    @Override
    Page<Record> findAll(Specification<Record> spec, Pageable pageable);
    @Override
    void deleteByUidAndRdate(Long uid, LocalDateTime rdate);
}
