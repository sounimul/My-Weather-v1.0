//package weather.weatherspring.repository;
//
//import jakarta.persistence.EntityManager;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.List;
//
//public class JpaRecordRepository{
//
//    private final EntityManager em;
//
//    public JpaRecordRepository(EntityManager em) {
//        this.em = em;
//    }
//
//
////    public List<Record> findAll(Long uid){
////        return em.createQuery("select r from Record r where r.uid = :uid order by r.rdate desc", Record.class)
////                .setParameter("uid",uid)
////                .getResultList();
////    }
//}
