package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import weather.weatherspring.entity.Record;
import weather.weatherspring.entity.RecordId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JpaRecordRepository implements RecordRepository{

    private final EntityManager em;

    public JpaRecordRepository(EntityManager em) {
        this.em = em;
    }

    /* 날씨 기록 저장 */
    public Record save(Record record){
        em.persist(record);
        return record;
    }

    /* uid, rdate로 찾기 */
    public Optional<Record> findByUidAndRdate(Long uid, LocalDateTime rdate){
        RecordId recordId = new RecordId();
        recordId.setUid(uid);
        recordId.setRdate(rdate);
        Record record = em.find(Record.class,recordId);
        return Optional.ofNullable(record);
    }

    /* 리스트 조회 */
    public List<Record> findByUid(Long uid){
        return em.createQuery("select r from Record r where r.uid = :uid order by r.rdate desc", Record.class)
                .setParameter("uid",uid)
                .getResultList();
    }

    public void deleteByUidAndRdate(Long uid, LocalDateTime rdate) {
        RecordId recordId = new RecordId();
        recordId.setUid(uid);
        recordId.setRdate(rdate);
        System.out.println("delete record");
        em.remove(findByUidAndRdate(recordId.getUid(),recordId.getRdate()));
    }

//    public List<Record> findAll(Specification<Record> spec, Sort sort){
//        Long uid = -1L;
//        return findByUid(uid);
//    }

    public Page<Record> findAll(Specification<Record> spec, Pageable pageable){
        return Page.empty();
    }

}
