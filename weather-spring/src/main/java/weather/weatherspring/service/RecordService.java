package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.RecordId;
import weather.weatherspring.repository.RecordRepository;

import java.util.List;

@Transactional
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    /* record 저장 */
    public Record saveRecord(Record record){
        RecordId recordId = new RecordId();
        recordId.setUid(record.getUid());
        recordId.setRdate(record.getRdate());
        validateDuplicateRecord(recordId);
        return recordRepository.save(record);
    }

    /* 중복 pk 체크 */
    private void validateDuplicateRecord(RecordId recordId){
        recordRepository.findByPk(recordId)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 기록입니다.");
                });
    }

    /* record 리스트 조회 */
    public List<Record> findRecordList(Long uid){
        return recordRepository.findAll(uid);
    }

    /* id -> 회원의 기록 조회 */
//    public List<Record> findRecordList(Long uid, Search search){
//        Specification<Record> spec = Specification.where(RecordSpecification.equalUid(uid));
//        if(search.getStartTemp() !=null && search.getEndTemp() != null)
//            spec = spec.and(RecordSpecification.betweenWeather(search.getStartTemp(), search.getEndTemp(), "temp"));
//        if(search.getStartHumid() != null && search.getEndHumid() != null)
//            spec = spec.and(RecordSpecification.betweenHumid(search.getStartHumid(), search.getEndHumid()));
//        if(search.getStartPrep() != null && search.getEndPrep() != null)
//            spec = spec.and(RecordSpecification.betweenWeather(search.getStartPrep(), search.getEndPrep(), "precip"));
//
//        Sort sort = Sort.by(Sort.Order.desc("rdate"));
//
//        return recordRepository.findAll(spec,sort);
//    }
}
