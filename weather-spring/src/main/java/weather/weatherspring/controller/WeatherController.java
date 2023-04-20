package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.domain.Location;
import weather.weatherspring.domain.Wtype;
import weather.weatherspring.entity.CurrentWeather;
import weather.weatherspring.entity.ElementForm;
import weather.weatherspring.repository.JpaWeatherRepository;
import weather.weatherspring.repository.MemberRepository;
import weather.weatherspring.repository.WeatherRepository;
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
    @Autowired
    private final WeatherRepository weatherRepository;

    public WeatherController(LocationService locationService, WeatherService weatherService, MemberRepository memberRepository, WeatherRepository weatherRepository) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.memberRepository = memberRepository;
        this.weatherRepository = weatherRepository;
    }

    /* weather view */
    @GetMapping("/weather")
    public ModelAndView weather() throws IOException{

        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        CurrentWeather cw=new CurrentWeather();
        CurrentWeather fw=new CurrentWeather();
        CurrentWeather pw=new CurrentWeather();

        // session으로부터 uid 가져와 modelAndView에 저장
        Long uid=(Long) session.getAttribute("uid");
        modelAndView.addObject("username",memberRepository.findByUid(uid).get().getNickname());
        System.out.println("session "+uid);     // 체크용

        // session으로부터 주소 가져와 modelAndView에 저장
        String ad = (String) session.getAttribute("address");
        if(ad==null) modelAndView.addObject("ad","");
        else modelAndView.addObject("ad",ad);

        // session으로부터 현재날씨 가져와 modelAndView에 저장
        CurrentWeather currentWeather = (CurrentWeather) session.getAttribute("current-weather");
        if(currentWeather==null) modelAndView.addObject("current",cw);
        else modelAndView.addObject("current",currentWeather);

        // session으로부터 1시간 후 날씨 가져와 modelAndView에 저장
        CurrentWeather futureWeather = (CurrentWeather) session.getAttribute("future-weather");
        if (futureWeather==null) modelAndView.addObject("future",fw);
        else modelAndView.addObject("future",futureWeather);

        // session으로부터 1시간 전 날씨 가져와 modelAndView에 저장
        CurrentWeather pastWeather = (CurrentWeather) session.getAttribute("past-weather");
        if (pastWeather==null) modelAndView.addObject("past",pw);
        else modelAndView.addObject("past",pastWeather);

        modelAndView.setViewName("weather");

        return modelAndView;
    }

    /* 현재 위치의 날씨 구하기 */
    @PostMapping("/weather")
    public ModelAndView createWeather(@RequestBody ElementForm elementForm){
        Location location = new Location();
        HttpSession session = request.getSession();
        CurrentWeather currentWeather = new CurrentWeather();
        CurrentWeather futureWeather = new CurrentWeather();
        CurrentWeather pastWeather = new CurrentWeather();
        Wtype wtype = new Wtype();
        Wtype wtype1 = new Wtype();
        Wtype wtype2 = new Wtype();

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
        // 초단기실황 - 현재 날씨
        JsonNode srtNcst=weatherService.getForecast2(elementForm).block();
        // 초단기예보 - 현재 날씨 + 1시간 뒤 날씨
        JsonNode srtFcst=weatherService.getForecast3(elementForm,0).block();
        // 초단기예보 - 1시간 전 날씨
        JsonNode srtFcst2=weatherService.getForecast3(elementForm,-1).block();

        //현재 시간 날씨
        currentWeather.setPty(srtNcst.get("response").get("body").get("items").get("item").get(0).get("obsrValue").asText());   // 현재 강수상태
        currentWeather.setReh(srtNcst.get("response").get("body").get("items").get("item").get(1).get("obsrValue").asText());   // 현재 습도
        currentWeather.setRn1(srtNcst.get("response").get("body").get("items").get("item").get(2).get("obsrValue").asText());   // 현재 강수량
        currentWeather.setT1h(srtNcst.get("response").get("body").get("items").get("item").get(3).get("obsrValue").asText());   // 현재 기온
        currentWeather.setSky(srtFcst.get("response").get("body").get("items").get("item").get(18).get("fcstValue").asText());   // 현재 하늘상태
        if(currentWeather.getPty().equals("0")) wtype.setWcode("SKY_"+currentWeather.getSky());
        else wtype.setWcode("PTY_"+currentWeather.getPty());
        currentWeather.setStatus(weatherRepository.findByWcode(wtype.getWcode()).get().getMessage());

        // 1시간 후 기온,날씨
        futureWeather.setPty(srtFcst.get("response").get("body").get("items").get("item").get(7).get("fcstValue").asText());
        futureWeather.setSky(srtFcst.get("response").get("body").get("items").get("item").get(19).get("fcstValue").asText());
        futureWeather.setT1h(srtFcst.get("response").get("body").get("items").get("item").get(25).get("fcstValue").asText());
        if (futureWeather.getPty().equals("0")) wtype1.setWcode("SKY_"+futureWeather.getSky());
        else wtype1.setWcode("PTY_"+futureWeather.getPty());
        futureWeather.setStatus(weatherRepository.findByWcode(wtype1.getWcode()).get().getMessage());

        // 1시간 전 기온, 날씨
        pastWeather.setPty(srtFcst2.get("response").get("body").get("items").get("item").get(6).get("fcstValue").asText());
        pastWeather.setSky(srtFcst2.get("response").get("body").get("items").get("item").get(18).get("fcstValue").asText());
        pastWeather.setT1h(srtFcst2.get("response").get("body").get("items").get("item").get(24).get("fcstValue").asText());
        if (pastWeather.getPty().equals("0")) wtype2.setWcode("SKY_"+pastWeather.getSky());
        else wtype2.setWcode("PTY_"+pastWeather.getPty());
        pastWeather.setStatus(weatherRepository.findByWcode(wtype2.getWcode()).get().getMessage());


        // 과거 날씨 구하기


        // 디버깅용
//        System.out.println("session2 "+uid);
//        System.out.println("latitude :"+location.getLatitude());
//        System.out.println("longitude :"+location.getLongitude());
//        System.out.println("Address :"+location.getAd());
//        System.out.println("X좌표 :"+location.getXcoor());
//        System.out.println("Y좌표 :"+location.getYcoor());
//        System.out.println(elementForm.getYear()+"년 "+elementForm.getMonth()+"일 "+elementForm.getDate()+"일");
//        System.out.println(elementForm.getHour()+"시 "+elementForm.getMin()+"분");

        // 위치정보, 날씨 정보 session에 저장
        session.setAttribute("address",location.getAd());
        session.setAttribute("current-weather",currentWeather);
        session.setAttribute("future-weather",futureWeather);
        session.setAttribute("past-weather",pastWeather);

        ModelAndView modelAndView = new ModelAndView("redirect:/weather");

        return modelAndView;
    }



}
