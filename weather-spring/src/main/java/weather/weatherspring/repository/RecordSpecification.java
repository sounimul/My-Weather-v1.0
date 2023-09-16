package weather.weatherspring.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import weather.weatherspring.entity.Record;

@Transactional
public class RecordSpecification{
    // uid=?
    public static Specification<Record> equalUid(Long uid){
        return new Specification<Record>() {
            @Override
            public Predicate toPredicate(Root<Record> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("uid"),uid);
            }
        };
    }
    // temp,precip between ? and ?
    // weather : temp / precip
    public static Specification<Record> betweenWeather(Double startWeather, Double endWeather, String weather){
        return new Specification<Record>() {
            @Override
            public Predicate toPredicate(Root<Record> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get(weather),startWeather, endWeather);
            }
        };
    }
    // humid between ? and ?
    public static Specification<Record> betweenHumid(int startHumid, int endHumid){
        return new Specification<Record>() {
            @Override
            public Predicate toPredicate(Root<Record> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("humid"),startHumid,endHumid);
            }
        };
    }
    // uid가 1초과인것 (관리자 uid가 아닌 것)
    public static Specification<Record> greaterUid(Long uid){
        return new Specification<Record>() {
            @Override
            public Predicate toPredicate(Root<Record> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThan(root.get("uid"),uid);
            }
        };
    }
}
