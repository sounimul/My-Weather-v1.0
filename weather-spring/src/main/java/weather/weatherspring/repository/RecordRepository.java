package weather.weatherspring.repository;

import java.util.List;

public interface RecordRepository {
    public List<Record> findAll(Long uid);
}
