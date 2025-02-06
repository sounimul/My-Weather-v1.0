package weather.weatherspring.domain.repository;

import weather.weatherspring.domain.entity.Wtype;

import java.util.Optional;

public interface WeatherRepository {

    Optional<Wtype> findByWcode(String wcode);
}
