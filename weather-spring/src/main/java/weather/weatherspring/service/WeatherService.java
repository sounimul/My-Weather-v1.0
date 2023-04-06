package weather.weatherspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import weather.weatherspring.controller.ElementForm;
import weather.weatherspring.repository.WeatherRepository;

@Transactional
public class WeatherService {
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


}
