package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
//import weather.weatherspring.repository.LocationRepository;

//@Controller
@RestController
@RequestMapping("/weather")
public class WeatherController {
//    @Autowired
//    private LocationRepository locationRepository;

//    @Autowired
//    private final WeatherService weatherService;
//    public WeatherController(WeatherService weatherService) {
//        this.weatherService = weatherService;
//    }
//    private final LocationService locationService;
//
//    @Autowired
//    public WeatherController(LocationService locationService) {
//        this.locationService = locationService;
//    }

    @Autowired
    private HttpServletRequest request;

    /* weather view */
    @GetMapping("")
    public ModelAndView weather() throws IOException{
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();

        modelAndView.setViewName("weather");
        Long uid=(Long) session.getAttribute("uid");

        System.out.println("session "+uid);

        return modelAndView;
    }

    // location service를 호출
//    @GetMapping("/current-location")
//    public Mono<Location> getCurrentLocation(){
//        return locationService.getCurrentLocation();
//    }

//    @PostMapping("")
//    public void createLocation(@RequestParam Long latitude, @RequestParam Long longitude){
////    public Location createLocation(@RequestParam Long latitude, @RequestParam Long longitude){
////        Location location = new Location();
//        HttpSession session = request.getSession();
//        Long uid=(Long) session.getAttribute("uid");
//
//        System.out.println(uid);
//        System.out.println(latitude);
//        System.out.println(longitude);
//
////        location.setUid(uid);
////        location.setLatitude(latitude);
////        location.setLongitude(longitude);
//
////
////        Location savedLocation = locationRepository.save(location);
////        System.out.println(savedLocation);
////        return savedLocation;
//
//    }

}
