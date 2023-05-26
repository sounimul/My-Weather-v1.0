package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.Member;
import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.RecordId;

import java.util.List;
import java.util.Optional;

@Repository
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
    public Optional<Record> findByPk(RecordId recordId){
        Record record = em.find(Record.class,recordId);
        return Optional.ofNullable(record);
    }

    /* 리스트 조회 */
    public List<Record> findAll(Long uid){
        return em.createQuery("select r from Record r where r.uid = :uid order by r.rdate desc", Record.class)
                .setParameter("uid",uid)
                .getResultList();
    }
}
