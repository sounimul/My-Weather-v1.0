package weather.weatherspring.service;

import weather.weatherspring.repository.WeatherRepository;

public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


}
