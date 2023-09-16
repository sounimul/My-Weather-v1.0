package weather.weatherspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Record;
import weather.weatherspring.entity.RecordId;
import weather.weatherspring.repository.RecordRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RecordServiceIntegrationTest {

    @Autowired
    RecordService recordService;
    @Autowired
    RecordRepository recordRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void 날씨기록_저장() throws Exception{
        // Given
        Record record = new Record();
        record.setUid(1L);
        record.setRdate(LocalDateTime.parse("2023-05-26 17:00:21", formatter));
        record.setRmd("5월 26일");
        record.setAd("대구광역시 달서구 월성1동");
        record.setWmsg("sunny");
        record.setTemp(25.1);
        record.setTfeel("따뜻해요");
        record.setHumid(50);
        record.setHfeel("쾌적해요");
        record.setPrecip(0.0);
        record.setPfeel("안와요");

        // When
        Optional<Record> savedRecord = recordService.saveRecord(record);

        // Then
        Record findRecord = recordRepository.findByUidAndRdate(savedRecord.get().getUid(),savedRecord.get().getRdate()).get();
        assertThat(record.getUid()).isEqualTo(findRecord.getUid());
        assertThat(record.getRdate()).isEqualTo(findRecord.getRdate());

    }

    @Test
    public void 중복기록_예외() throws Exception{
        // Given
        Record record = new Record();
        record.setUid(1L);
        record.setRdate(LocalDateTime.parse("2023-05-26 17:00:21", formatter));
        record.setRmd("5월 26일");
        record.setAd("대구광역시 달서구 월성1동");
        record.setWmsg("sunny");
        record.setTemp(25.1);
        record.setTfeel("따뜻해요");
        record.setHumid(50);
        record.setHfeel("쾌적해요");
        record.setPrecip(0.0);
        record.setPfeel("안와요");

        Record record2 = new Record();
        record2.setUid(1L);
        record2.setRdate(LocalDateTime.parse("2023-05-26 17:00:21", formatter));
        record2.setRmd("5월 26일");
        record2.setAd("대구광역시 달서구 월성1동");
        record2.setWmsg("sunny");
        record2.setTemp(25.1);
        record2.setTfeel("따뜻해요");
        record2.setHumid(50);
        record2.setHfeel("쾌적해요");
        record2.setPrecip(0.0);
        record2.setPfeel("안와요");

        // When
        recordService.saveRecord(record);
        Optional<Record> savedRecord = recordService.saveRecord(record2);
//        IllegalStateException e = assertThrows(IllegalStateException.class,
//                () -> recordService.saveRecord(record2));
//
//        assertThat(e.getMessage()).isEqualTo("이미 존재하는 기록입니다.");

        // Then
        assertThat(savedRecord).isEmpty();

    }

    @Test
    public void 날씨기록_삭제(){
        // Given
        Record record = new Record();
        record.setUid(1L);
        record.setRdate(LocalDateTime.parse("2023-05-26 17:00:21", formatter));
        record.setRmd("5월 26일");
        record.setAd("대구광역시 달서구 월성1동");
        record.setWmsg("sunny");
        record.setTemp(25.1);
        record.setTfeel("따뜻해요");
        record.setHumid(50);
        record.setHfeel("쾌적해요");
        record.setPrecip(0.0);
        record.setPfeel("안와요");

        RecordId recordId = new RecordId();
        recordId.setUid(record.getUid());
        recordId.setRdate(record.getRdate());

        // When
        recordService.saveRecord(record);
        recordService.deleteRecord(recordId);

        // Then
        Optional<Record> record1 = recordRepository.findByUidAndRdate(recordId.getUid(),recordId.getRdate());
        assertThat(record1).isEmpty();

    }

}
