package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.repository.RecordRepository;

import java.util.List;

@Transactional
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    /* id -> 회원의 기록 조회 */
    public List<Record> findRecords(Long uid){
        return recordRepository.findAll(uid);
    }
}
