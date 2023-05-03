package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Search;
import weather.weatherspring.repository.RecordRepository;
import weather.weatherspring.repository.RecordSpecification;

import java.util.List;

@Transactional
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    /* id -> 회원의 기록 조회 */
    public List<Record> findRecordList(Long uid, Search search){
        Specification<Record> spec = Specification.where(RecordSpecification.equalUid(uid));
        if(search.getStartTemp() !=null && search.getEndTemp() != null)
            spec = spec.and(RecordSpecification.betweenWeather(search.getStartTemp(), search.getEndTemp(), "temp"));
        if(search.getStartHumid() != null && search.getEndHumid() != null)
            spec = spec.and(RecordSpecification.betweenHumid(search.getStartHumid(), search.getEndHumid()));
        if(search.getStartPrep() != null && search.getEndPrep() != null)
            spec = spec.and(RecordSpecification.betweenWeather(search.getStartPrep(), search.getEndPrep(), "precip"));

        return recordRepository.findAll(spec);
    }
}
