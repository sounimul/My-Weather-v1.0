package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.RecordId;
import weather.weatherspring.repository.RecordRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    /* record 저장 */
    public Optional<Record> saveRecord(Record record){
        RecordId recordId = new RecordId();
        recordId.setUid(record.getUid());
        recordId.setRdate(record.getRdate());

        // Record 중복 아이디,시간 체크
        Optional<Record> valid=Optional.empty();
        if(validateDuplicateRecord(recordId).isPresent()){
            return valid;
        }
        // 중복되지 않을 때
        return Optional.of(recordRepository.save(record));
//        return Optional.of(springDataJpaRecordRepository.save(record));
    }

    /* 중복 pk 체크 */
    private Optional<Record> validateDuplicateRecord(RecordId recordId){
        return recordRepository.findByUidAndRdate(recordId.getUid(),recordId.getRdate());
//        return springDataJpaRecordRepository.findByPk(recordId);
    }

    /* record 리스트 조회 */
    public List<Record> findRecordList(Long uid){
        return recordRepository.findByUid(uid);
//        return springDataJpaRecordRepository.findAll(uid);
    }

    /* record 삭제 */
    public void deleteRecord(RecordId recordId){
        recordRepository.deleteByUidAndRdate(recordId.getUid(), recordId.getRdate());
//        springDataJpaRecordRepository.deleteById(recordId);
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
