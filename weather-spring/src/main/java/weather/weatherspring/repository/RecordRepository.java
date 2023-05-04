package weather.weatherspring.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import weather.weatherspring.domain.RecordId;

import java.util.List;

@Repository
public interface RecordRepository{
    List<Record> findAll(Long uid);
//public interface RecordRepository extends JpaRepository<Record, RecordId>, JpaSpecificationExecutor<Record> {
//    List<Record> findAll(Specification<Record> spec, Sort sort);
}
