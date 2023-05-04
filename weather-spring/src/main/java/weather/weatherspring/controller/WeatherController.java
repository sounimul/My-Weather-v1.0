package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.domain.Location;
import weather.weatherspring.domain.Wtype;
import weather.weatherspring.entity.BasicWeather;
import weather.weatherspring.entity.CurrentWeather;
import weather.weatherspring.entity.ElementForm;
import weather.weatherspring.entity.Temperature;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.MemberService;
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
    private final MemberService memberService;

    public WeatherController(LocationService locationService, WeatherService weatherService, MemberService memberService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.memberService = memberService;
    }

    /* weather view */
    @GetMapping("/weather")
    public ModelAndView weather() throws IOException{

        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        CurrentWeather cw=new CurrentWeather();
        BasicWeather pfw = new BasicWeather();
        Temperature t = new Temperature();

        // session으로부터 uid 가져와 modelAndView에 저장
        Long uid=(Long) session.getAttribute("uid");
        modelAndView.addObject("username",memberService.findMember(uid).get().getNickname());

        // session으로부터 주소 가져와 modelAndView에 저장
        String ad = (String) session.getAttribute("address");
        if(ad==null) modelAndView.addObject("ad","");
        else modelAndView.addObject("ad",ad);

        // session으로부터 현재날씨 가져와 modelAndView에 저장
        CurrentWeather currentWeather = (CurrentWeather) session.getAttribute("current-weather");
        if(currentWeather==null) modelAndView.addObject("current",cw);
        else modelAndView.addObject("current",currentWeather);

        // session으로부터 1시간 전후 날씨 가져와 modelAndView에 저장
        BasicWeather pfWeather = (BasicWeather) session.getAttribute("pf-weather");
        if (pfWeather==null) modelAndView.addObject("pastfuture",pfw);
        else modelAndView.addObject("pastfuture",pfWeather);

        // session으로부터 최고, 최저 기온 가져와 modelAndView에 저장
        Temperature temp = (Temperature) session.getAttribute("minmax-temp");
        if (temp==null) modelAndView.addObject("minmax",t);
        else modelAndView.addObject("minmax",temp);

        modelAndView.setViewName("weather");

        return modelAndView;
    }

    /* 현재 위치의 날씨 구하기 */
    @PostMapping("/weather")
    public ModelAndView createWeather(@RequestBody ElementForm elementForm){
        Location location = new Location();
        HttpSession session = request.getSession();
        CurrentWeather currentWeather = new CurrentWeather();   // 현재 날씨
        BasicWeather pfWeather = new BasicWeather();            // 1시간 전후 날씨
        Wtype wtype = new Wtype();      // 하늘상태 + 강수형태
        Temperature temp = new Temperature();   // 최고, 최저기온

        //session에서 id 가져오기
        Long uid=(Long) session.getAttribute("uid");
        location.setUid(uid);
        location.setLatitude(elementForm.getLatitude());
        location.setLongitude(elementForm.getLongitude());
        // 위도, 경도 -> 기상청 x,y좌표
        elementForm=locationService.getXY(elementForm);
        location.setXcoor(elementForm.getXcoor());
        location.setYcoor(elementForm.getYcoor());

        // 위도,경도 -> 주소
        JsonNode address=locationService.getAddress(elementForm).block();
        location.setAd(address.get("documents").get(1).get("address_name").asText());   // get(0) : 법정동, get(1) : 행정동 -> 기상청은 행정동이 기준

        // 단기예보 - 오늘 최고, 최저기온
        JsonNode vilFcst=weatherService.getForecast(elementForm,0).block();
        // 단기예보 - 2일치 예보
        JsonNode vilFcst2=weatherService.getForecast(elementForm,1).block();
        // 초단기실황 - 현재 날씨
        JsonNode srtNcst=weatherService.getForecast2(elementForm).block();
        // 초단기예보 - 현재 날씨 + 1시간 뒤 날씨
        JsonNode srtFcst=weatherService.getForecast3(elementForm,0).block();
        // 초단기예보 - 1시간 전 날씨
        JsonNode srtFcst2=weatherService.getForecast3(elementForm,-1).block();
        // 중기예보 - 3~5일 최고, 최저기온 및 날씨

        //현재 시간 날씨 - 초단기실황 + 초단기예보(현재 하늘상태)
        currentWeather.setPty(srtNcst.get("response").get("body").get("items").get("item").get(0).get("obsrValue").asText());   // 현재 강수상태
        currentWeather.setReh(srtNcst.get("response").get("body").get("items").get("item").get(1).get("obsrValue").asText());   // 현재 습도
        currentWeather.setRn1(srtNcst.get("response").get("body").get("items").get("item").get(2).get("obsrValue").asText());   // 현재 강수량
        currentWeather.setT1h(srtNcst.get("response").get("body").get("items").get("item").get(3).get("obsrValue").asText());   // 현재 기온
        currentWeather.setSky(srtFcst.get("response").get("body").get("items").get("item").get(18).get("fcstValue").asText());   // 현재 하늘상태
        if(currentWeather.getPty().equals("0")) wtype.setWcode("SKY_"+currentWeather.getSky());
        else wtype.setWcode("PTY_"+currentWeather.getPty());
        currentWeather.setStatus(weatherService.findWtype(wtype.getWcode()).get().getMessage());

        // 1시간 후 기온,날씨 - 초단기예보
        pfWeather.setFpty(srtFcst.get("response").get("body").get("items").get("item").get(7).get("fcstValue").asText());
        pfWeather.setFsky(srtFcst.get("response").get("body").get("items").get("item").get(19).get("fcstValue").asText());
        pfWeather.setFt1h(srtFcst.get("response").get("body").get("items").get("item").get(25).get("fcstValue").asText());
        if (pfWeather.getFpty().equals("0")) wtype.setWcode("SKY_"+pfWeather.getFsky());
        else wtype.setWcode("PTY_"+pfWeather.getFpty());
        pfWeather.setFicon(weatherService.findWtype(wtype.getWcode()).get().getWname());

        // 1시간 전 기온, 날씨 - 초단기예보
        pfWeather.setPpty(srtFcst2.get("response").get("body").get("items").get("item").get(6).get("fcstValue").asText());
        pfWeather.setPsky(srtFcst2.get("response").get("body").get("items").get("item").get(18).get("fcstValue").asText());
        pfWeather.setPt1h(srtFcst2.get("response").get("body").get("items").get("item").get(24).get("fcstValue").asText());
        if (pfWeather.getPpty().equals("0")) wtype.setWcode("SKY_"+pfWeather.getPsky());
        else wtype.setWcode("PTY_"+pfWeather.getPpty());
        pfWeather.setPicon(weatherService.findWtype(wtype.getWcode()).get().getWname());

        // 오늘의 최고, 최저기온
        for(int i=0;i<290;i++){
            String cate=vilFcst.get("response").get("body").get("items").get("item").get(i).get("category").asText();
            if(cate.equals("TMN")) temp.setTmn(vilFcst.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText());
            else if(cate.equals("TMX")) temp.setTmx(vilFcst.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText());
        }

        // 2일치 최고, 최저기온, 날씨
        String[] fcstTmx={"",""}; String[] fcstTmn={"",""};
        String[] minName={"",""}; String[] maxName={"",""};
        String p=""; String s="";
        String todaydate=elementForm.getYear() + String.format("%02d",elementForm.getMonth()) + String.format("%02d",elementForm.getDate());
        int j=0,k=0;
        int total=vilFcst2.get("response").get("body").get("totalCount").asInt();
        for(int i=0;i<870;i++){
            if (i>=total) break;
            String date=vilFcst2.get("response").get("body").get("items").get("item").get(i).get("fcstDate").asText();
            // 오늘 날짜 pass
            if(date.equals(todaydate)) continue;
            // 카테고리 확인
            String cate=vilFcst2.get("response").get("body").get("items").get("item").get(i).get("category").asText();
            // 카테고리가 pty, sky -> 일단 저장
            if(cate.equals("PTY")) p=vilFcst2.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
            else if(cate.equals("SKY")) s=vilFcst2.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
            //최저, 최고 기온 찾기
            if(cate.equals("TMN")){
                fcstTmn[j]=vilFcst2.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
                if (p.equals("0")) wtype.setWcode("SKY_"+s);
                else wtype.setWcode("PTY_"+p);
                minName[j] = weatherService.findWtype(wtype.getWcode()).get().getWname();
                j++;
            }
            else if(cate.equals("TMX")){
                fcstTmx[k]=vilFcst2.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText();
                if (p.equals("0")) wtype.setWcode("SKY_"+s);
                else wtype.setWcode("PTY_"+p);
                maxName[k] = weatherService.findWtype(wtype.getWcode()).get().getWname();
                k++;
            }
            if(j==2&k==2) break;
        }
        temp.setFcstTmx(fcstTmx); temp.setFcstTmn(fcstTmn);
        temp.setMaxName(maxName); temp.setMinName(minName);

        // 위치정보, 날씨 정보 session에 저장
        session.setAttribute("address",location.getAd());
        session.setAttribute("current-weather",currentWeather);
        session.setAttribute("pf-weather",pfWeather);
        session.setAttribute("minmax-temp",temp);

        ModelAndView modelAndView = new ModelAndView("redirect:/weather");

        return modelAndView;
    }



}
