package weather.weatherspring.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import weather.weatherspring.domain.repository.*;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.RecordService;
import weather.weatherspring.service.ReviewService;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;
    private final EntityManager em;
    private final RecordRepository recordRepository;

    public SpringConfig(DataSource dataSource, EntityManager em, RecordRepository recordRepository) {
        this.dataSource = dataSource;
        this.em = em;
        this.recordRepository = recordRepository;
    }

    @Bean
    public MemberRepository memberRepository(){
        return new JpaMemberRepository(em);
    }

    @Bean
    public WeatherRepository weatherRepository(){ return new JpaWeatherRepository(em);}

    @Bean
    public ReviewRepository reviewRepository(){
        return new JpaReviewRepository(em);
    }

}
