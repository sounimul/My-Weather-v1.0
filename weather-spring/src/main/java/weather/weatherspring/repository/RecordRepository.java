package weather.weatherspring.repository;

import org.springframework.stereotype.Repository;
import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.RecordId;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository{
    public Record save(Record record);
    public Optional<Record> findByPk(RecordId recordId);
    List<Record> findAll(Long uid);
//public interface RecordRepository extends JpaRepository<Record, RecordId>, JpaSpecificationExecutor<Record> {
//    List<Record> findAll(Specification<Record> spec, Sort sort);
}
