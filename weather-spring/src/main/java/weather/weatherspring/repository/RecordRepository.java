package weather.weatherspring.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import weather.weatherspring.domain.RecordId;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, RecordId>, JpaSpecificationExecutor<Record> {
    List<Record> findAll(Specification<Record> spec);
}
