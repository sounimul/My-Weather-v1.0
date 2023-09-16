package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;
import weather.weatherspring.entity.Wtype;

import java.util.Optional;

public class JpaWeatherRepository implements WeatherRepository{
    private final EntityManager em;

    public JpaWeatherRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<Wtype> findByWcode(String wcode){
        Wtype wtype = em.find(Wtype.class,wcode);
        return Optional.ofNullable(wtype);
    }

}
