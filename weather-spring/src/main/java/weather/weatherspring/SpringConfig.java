package weather.weatherspring;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import weather.weatherspring.repository.*;
import weather.weatherspring.service.MemberService;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;
    private final EntityManager em;
//    private final WebClient webClient;

    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }

    //    public SpringConfig(DataSource dataSource, EntityManager em, WebClient webClient) {
//        this.dataSource = dataSource;
//        this.em = em;
//        this.webClient = webClient;
//    }
    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new JpaMemberRepository(em);
    }
//    @Bean
//    public WebClient webClient(){
//        return WebClient.builder().build();
//    }
//    @Bean
//    public LocationService locationService(){
//        return new LocationService(webClient);
//    }


//    @Bean
//    public LocationRepository locationRepository(){
//        return new LocationRepository();
//    }
//    @Bean
//    public WeatherService weatherService(){
//        return new WeatherService(weatherRepository());
//    }
//
//    @Bean
//    public WeatherRepository weatherRepository(){
//        return new JpaWeatherRepository(em);
//    }
}
