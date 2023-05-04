package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.domain.RecordId;

import java.util.List;

@Repository
public class JpaRecordRepository implements RecordRepository{

    private final EntityManager em;

    public JpaRecordRepository(EntityManager em) {
        this.em = em;
    }

//    // 날씨 기록 저장
//    public Record save(Record record){
//        em.persist(record);
//        return record;
//    }

    public List<Record> findAll(Long uid){
        return em.createQuery("select r from Record r where r.uid = :uid order by r.rdate desc", Record.class)
                .setParameter("uid",uid)
                .getResultList();
    }
}
