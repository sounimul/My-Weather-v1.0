package weather.weatherspring.repository;

import weather.weatherspring.domain.Wtype;

import java.util.Map;
import java.util.Optional;

public interface WeatherRepository {

    Optional<Wtype> findByWcode(String wcode);
}
