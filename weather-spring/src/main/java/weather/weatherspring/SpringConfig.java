package weather.weatherspring;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import weather.weatherspring.repository.*;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.MemberService;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;
    private final EntityManager em;

    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new JpaMemberRepository(em);
    }

//    @Bean
//    public LocationService locationService(){ return new LocationService();}

    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }

}
