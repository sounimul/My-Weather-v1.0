package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.domain.Location;
import weather.weatherspring.entity.CurrentWeather;
import weather.weatherspring.entity.ElementForm;
import weather.weatherspring.repository.MemberRepository;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.WeatherService;

import java.io.IOException;

@RestController
public class WeatherController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final LocationService locationService;
    @Autowired
    private final WeatherService weatherService;
    @Autowired
    private final MemberRepository memberRepository;

    public WeatherController(LocationService locationService, WeatherService weatherService, MemberRepository memberRepository) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.memberRepository = memberRepository;
    }

    /* weather view */
    @GetMapping("/weather")
    public ModelAndView weather() throws IOException{

        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        CurrentWeather cw=new CurrentWeather();

        // session으로부터 uid 가져와 modelAndView에 저장
        Long uid=(Long) session.getAttribute("uid");
        modelAndView.addObject("username",memberRepository.findByUid(uid).get().getNickname());
        System.out.println("session "+uid);     // 체크용

        // 주소값 확인
        String ad = (String) session.getAttribute("address");
        if(ad==null) modelAndView.addObject("ad","");
        else modelAndView.addObject("ad",ad);

        // session으로부터 현재날씨 가져와 modelAndView에 저장
        CurrentWeather currentWeather = (CurrentWeather) session.getAttribute("current-weather");
        if(currentWeather==null) modelAndView.addObject("current",cw);
        else modelAndView.addObject("current",currentWeather);

        modelAndView.setViewName("weather");

        return modelAndView;
    }

    /* 현재 위치의 날씨 구하기 */
    @PostMapping("/weather")
    public ModelAndView createWeather(@RequestBody ElementForm elementForm){
        Location location = new Location();
        HttpSession session = request.getSession();
        CurrentWeather currentWeather = new CurrentWeather();

        //session에서 id 가져오기
        Long uid=(Long) session.getAttribute("uid");
        location.setUid(uid);
        location.setLatitude(elementForm.getLatitude());
        location.setLongitude(elementForm.getLongitude());

        // 위도,경도 -> 주소
        JsonNode address=locationService.getAddress(elementForm).block();
        location.setAd(address.get("documents").get(1).get("address_name").asText());   // get(0) : 법정동, get(1) : 행정동 -> 기상청은 행정동이 기준

        // 위도, 경도 -> 기상청 x,y좌표
        elementForm=locationService.getXY(elementForm);
        location.setXcoor(elementForm.getXcoor());
        location.setYcoor(elementForm.getYcoor());

        // 단기예보(3일치 예보)
        JsonNode vilageFcst=weatherService.getForecast(elementForm).block();
        // 초단기실황
        JsonNode srtFcst=weatherService.getForecast2(elementForm).block();
        currentWeather.setPty(srtFcst.get("response").get("body").get("items").get("item").get(0).get("obsrValue").asText());
        currentWeather.setReh(srtFcst.get("response").get("body").get("items").get("item").get(1).get("obsrValue").asText());
        currentWeather.setRn1(srtFcst.get("response").get("body").get("items").get("item").get(2).get("obsrValue").asText());
        currentWeather.setT1h(srtFcst.get("response").get("body").get("items").get("item").get(3).get("obsrValue").asText());
        System.out.println(currentWeather.getT1h());

        // 과거 날씨 구하기

        // 디버깅용
        System.out.println("session2 "+uid);
        System.out.println("latitude :"+location.getLatitude());
        System.out.println("longitude :"+location.getLongitude());
        System.out.println("Address :"+location.getAd());
        System.out.println("X좌표 :"+location.getXcoor());
        System.out.println("Y좌표 :"+location.getYcoor());
        System.out.println(elementForm.getYear()+"년 "+elementForm.getMonth()+"일 "+elementForm.getDate()+"일");
        System.out.println(elementForm.getHour()+"시 "+elementForm.getMin()+"분");
        System.out.println(vilageFcst);
        System.out.println(srtFcst);

        // 위치정보, 날씨 정보 session에 저장
//        session.setAttribute("location",location);
        session.setAttribute("address",location.getAd());
        session.setAttribute("current-weather",currentWeather);

//        ModelAndView modelAndView = new ModelAndView("weather");
//        modelAndView.addObject("username",memberRepository.findByUid(uid).get().getNickname());
//        modelAndView.addObject("ad",location.getAd());
//        modelAndView.addObject("current",currentWeather);
        ModelAndView modelAndView = new ModelAndView("redirect:/weather");

        return modelAndView;
    }



}
