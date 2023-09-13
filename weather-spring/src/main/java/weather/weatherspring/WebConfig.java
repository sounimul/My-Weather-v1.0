package weather.weatherspring;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import weather.weatherspring.interceptor.AdminInterceptor;
import weather.weatherspring.interceptor.LogInterceptor;
import weather.weatherspring.interceptor.LoginCheckInterceptor;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/","/joinForm","/join","/login","/checkId");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/weather","/saveWeather","/myPage","/changePwform","/updateProfile",
                        "/update-pw","/deleteRecord","/logout","/withdrawal","/review","/board/**");

        registry.addInterceptor(new AdminInterceptor())
                .order(2)
                .addPathPatterns("/board/**");
    }
}
