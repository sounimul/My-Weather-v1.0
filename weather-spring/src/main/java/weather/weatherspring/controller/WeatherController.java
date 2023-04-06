package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.domain.Location;
import weather.weatherspring.service.LocationService;

import java.io.IOException;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final LocationService locationService;

    @Autowired
    public WeatherController(LocationService locationService) {
        this.locationService = locationService;
    }

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
    public ModelAndView createWeather(@RequestBody ElementForm elementForm){
        Location location = new Location();

        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");
        location.setUid(uid);
        location.setLatitude(elementForm.getLatitude());
        location.setLongitude(elementForm.getLongitude());

        // 위도,경도 -> 주소
        JsonNode address=locationService.getAddress(elementForm).block();
        location.setAd(address.get("documents").get(1).get("address_name").asText());

        // 위도, 경도 -> 기상청 x,y좌표
        elementForm=locationService.getXY(elementForm);
        location.setXcoor(elementForm.getXcoor());
        location.setYcoor(elementForm.getYcoor());


        // 디버깅용
        System.out.println("session2 "+uid);
        System.out.println("latitude :"+location.getLatitude());
        System.out.println("longitude :"+location.getLongitude());
        System.out.println("Address :"+location.getAd());
        System.out.println("X좌표 :"+location.getXcoor());
        System.out.println("Y좌표 :"+location.getYcoor());
        System.out.println(elementForm.getYear()+"년 "+elementForm.getMonth()+"일 "+elementForm.getDate()+"일");
        System.out.println(elementForm.getHour()+"시 "+elementForm.getMin()+"분");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("weather");

        return modelAndView;
    }


}
