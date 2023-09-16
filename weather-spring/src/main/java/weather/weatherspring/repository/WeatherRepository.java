package weather.weatherspring.repository;

import weather.weatherspring.entity.Wtype;

import java.util.Optional;

public interface WeatherRepository {

    Optional<Wtype> findByWcode(String wcode);
}
