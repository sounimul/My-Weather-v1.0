package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;

import java.util.List;

public class JpaRecordRepository implements RecordRepository{

    private final EntityManager em;

    public JpaRecordRepository(EntityManager em) {
        this.em = em;
    }


    public List<Record> findAll(Long uid){
        return em.createQuery("select r from Record r where r.uid = :uid", Record.class)
                .setParameter("uid",uid)
                .getResultList();
    }
}
