package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.domain.Coordinate;
import weather.weatherspring.domain.Location;

import java.io.IOException;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    private HttpServletRequest request;

    /* weather view */
    @GetMapping("")
    public ModelAndView weather() throws IOException{

        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");
        System.out.println("session "+uid);     // 체크용

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("weather");
        return modelAndView;
    }

    /* 현재 위치의 위도, 경도를 주소와 x,y좌표로 바꾸기 */
    @PostMapping("")
    public ModelAndView createLocation(@RequestBody Coordinate coordinate){
        Location location = new Location();

        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");

        location.setUid(uid);
        location.setLatitude(coordinate.getLatitude());
        location.setLongitude(coordinate.getLongitude());

        // 확인용
        System.out.println("session2 "+uid);
        System.out.println("latitude :"+location.getLatitude());
        System.out.println("longitude :"+location.getLongitude());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("weather");

        return modelAndView;
    }


}
