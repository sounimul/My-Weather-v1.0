package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Record;
import weather.weatherspring.entity.RecordId;
import weather.weatherspring.domain.Search;
import weather.weatherspring.repository.RecordRepository;
import weather.weatherspring.repository.RecordSpecification;

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
    }

    /* 중복 pk 체크 */
    private Optional<Record> validateDuplicateRecord(RecordId recordId){
        return recordRepository.findByUidAndRdate(recordId.getUid(),recordId.getRdate());
    }

    /* record 리스트 조회 */
    public List<Record> findRecordList(Long uid){
        return recordRepository.findByUid(uid);
    }

    /* record 삭제 */
    public void deleteRecord(RecordId recordId){
        recordRepository.deleteByUidAndRdate(recordId.getUid(), recordId.getRdate());
    }

    /* id -> 회원의 기록 조회 */
    public Page<Record> findRecords(Long uid, Search search,int page){
        Specification<Record> spec = Specification.where(RecordSpecification.equalUid(uid));
        if(search.getStartTemp() !=null && search.getEndTemp() != null)
            spec = spec.and(RecordSpecification.betweenWeather(search.getStartTemp(), search.getEndTemp(), "temp"));
        if(search.getStartHumid() != null && search.getEndHumid() != null)
            spec = spec.and(RecordSpecification.betweenHumid(search.getStartHumid(), search.getEndHumid()));
        if(search.getStartPrep() != null && search.getEndPrep() != null)
            spec = spec.and(RecordSpecification.betweenWeather(search.getStartPrep(), search.getEndPrep(), "precip"));

        Pageable pageable = PageRequest.of(page,7,Sort.by(Sort.Order.desc("rdate")));
        return recordRepository.findAll(spec,pageable);
    }

    public Page<Record> findAllRecords(Search search, int page){
        Specification<Record> spec = Specification.where(RecordSpecification.greaterUid(1L));

        if(search.getStartTemp() !=null && search.getEndTemp() != null)
            spec = spec.and(RecordSpecification.betweenWeather(search.getStartTemp(), search.getEndTemp(), "temp"));
        if(search.getStartHumid() != null && search.getEndHumid() != null)
            spec = spec.and(RecordSpecification.betweenHumid(search.getStartHumid(), search.getEndHumid()));
        if(search.getStartPrep() != null && search.getEndPrep() != null)
            spec = spec.and(RecordSpecification.betweenWeather(search.getStartPrep(), search.getEndPrep(), "precip"));

        Pageable pageable = PageRequest.of(page,17,Sort.by(Sort.Order.desc("rdate")));
        return recordRepository.findAll(spec,pageable);
    }

    public String getTemps(String temp){
        // 1. 기온 체감
        switch (temp){
            case "melting":
                return "무더워요";
            case "hot":
                return "더워요";
            case "warm":
                return "따뜻해요";
            case "mild":
                return "포근해요";
            case "cool":
                return "시원해요";
            case "pleasantly cool":
                return "선선해요";
            case "chilly":
                return "쌀쌀해요";
            case "cold":
                return "추워요";
            case "freezing cold":
                return "매우 추워요";
        }
        return "-";
    }

    public String getHumidity(String humid){
        // 2. 습도 체감
        switch(humid){
            case "humid":
                return "습해요";
            case "fresh":
                return "쾌적해요";
            case "dry":
                return "건조해요";
        }
        return "-";
    }

    public String getPrecip(String rain){
        // 3. 강수 체감
        switch(rain){
            case "no":
                return "안와요";
            case "light":
                return "약한 비";
            case "rain":
                return "보통 비";
            case "heavy":
                return "강한 비";
            case "shower":
                return "쏟아져요";
        }
        return "-";
    }
}
