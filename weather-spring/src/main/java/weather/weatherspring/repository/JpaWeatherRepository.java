package weather.weatherspring.repository;

import jakarta.persistence.EntityManager;

public class JpaWeatherRepository implements WeatherRepository{
    private final EntityManager em;

    public JpaWeatherRepository(EntityManager em) {
        this.em = em;
    }


}
