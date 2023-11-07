package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.entity.Member;
import weather.weatherspring.entity.Record;
import weather.weatherspring.domain.*;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.RecordService;
import weather.weatherspring.service.WeatherService;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@RestController
public class WeatherController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final LocationService locationService;
    @Autowired
    private final WeatherService weatherService;
    @Autowired
    private final RecordService recordService;
    @Autowired
    private final MemberService memberService;

    public WeatherController(LocationService locationService, WeatherService weatherService, RecordService recordService, MemberService memberService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.recordService = recordService;
        this.memberService = memberService;
    }

    /* weather view */
    @GetMapping("/weather")
    public ModelAndView weather() throws IOException{

        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();

        // session으로부터 uid 가져와 modelAndView에 저장
        Long uid=(Long) session.getAttribute("uid");
        Member member = memberService.findMember(uid).get();
        modelAndView.addObject("member",member);

        // session으로부터 현재날씨 가져와 modelAndView에 저장
        CurrentWeather currentWeather = (CurrentWeather) session.getAttribute("current-weather");
        modelAndView.addObject("current",(currentWeather==null ? new CurrentWeather() : currentWeather));

        // session으로부터 1시간 전후 날씨 가져와 modelAndView에 저장
        BasicWeather pfWeather = (BasicWeather) session.getAttribute("pf-weather");
        modelAndView.addObject("pastfuture", (pfWeather==null ? new BasicWeather() : pfWeather));

        // session으로부터 중기날씨예보 가져와 modelAndView에 저장
        MidWeather midWeather = (MidWeather) session.getAttribute("mid-weather");
        modelAndView.addObject("mid",(midWeather==null ? new MidWeather() : midWeather));

        // 현재 날짜, 시간
        ElementForm elementForm = (ElementForm) session.getAttribute("element");
        modelAndView.addObject("element",(elementForm==null ? new ElementForm() : elementForm));

        modelAndView.setViewName("weather");

        return modelAndView;
    }

    /* 현재 위치의 날씨 구하기 */
    @PostMapping("/weather")
    public Object createWeather(@RequestBody ElementForm elementForm){
        HttpSession session = request.getSession();
        CurrentWeather currentWeather = new CurrentWeather();   // 현재 날씨
        BasicWeather pfWeather = new BasicWeather();            // 1시간 전후 날씨
        MidWeather midWeather = new MidWeather();   // 5일치 날씨예보


        /*
        주소 처리
         */
        // 위도, 경도 -> 기상청 x,y좌표
        elementForm=locationService.getXY(elementForm);
        // 위도,경도를 행정구역으로 변환하는 카카오 api 호출
        elementForm.setAd(locationService.getAddress(elementForm));
        // 주소 -> 중기예보구역 코드
        String areaCode= locationService.getAreaCode(elementForm.getAd());


        /*
        날씨 예보 받아오고 처리
         */
        CountDownLatch cdl = new CountDownLatch(6);
        Map<String,JsonNode> response = new HashMap<>();

        // 단기예보 - 오늘 최고, 최저기온
        weatherService.getForecast(elementForm,0).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("tmnTmx",e), error -> { throw new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather");});
        // 단기예보 - 2일치 예보
        weatherService.getForecast(elementForm,1).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("twoDayFcst",e), error -> { throw new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather");});
        // 초단기실황 - 현재 날씨 / 초단기예보 - 현재날씨 + 1시간후 날씨
        weatherService.getForecast2(elementForm).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("curFutFcst",e), error -> { throw new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather");});
        weatherService.getForecast3(elementForm,1).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("curWeather",e), error -> { throw new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather");});
        // 초단기예보 - 1시간 전 날씨
        weatherService.getForecast3(elementForm,0).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("pastFcst",e), error -> { throw new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather");});
        // 중기예보 - 3~5일 최고, 최저기온 및 날씨
        weatherService.getMidForecast(elementForm,areaCode).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("midFcst",e), error -> { throw new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather");});

        try {
            cdl.await();
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }


        String[][] curFutFcst = weatherService.jsonToCurFutFcst(response.get("curFutFcst"), response.get("curWeather"));
        //현재 시간 날씨 - 초단기실황 + 초단기예보(현재 하늘상태)
        currentWeather.setPty(curFutFcst[0][0]);
        currentWeather.setReh(curFutFcst[0][1]);
        currentWeather.setRn1(curFutFcst[0][2]);
        currentWeather.setT1h(curFutFcst[0][3]);
        currentWeather.setSky(curFutFcst[0][4]);
        currentWeather.setStatus(curFutFcst[0][5]);
        currentWeather.setIcon(curFutFcst[0][6]);

        // 1시간 후 기온,날씨 - 초단기예보
        pfWeather.setFpty(curFutFcst[1][0]);
        pfWeather.setFsky(curFutFcst[1][1]);
        pfWeather.setFt1h(curFutFcst[1][2]);
        pfWeather.setFicon(curFutFcst[1][3]);

        // 1시간 전 기온, 날씨 - 초단기예보
        String[] pastFcst = weatherService.jsonToPastFcst(response.get("pastFcst"));
        pfWeather.setPpty(pastFcst[0]);
        pfWeather.setPsky(pastFcst[1]);
        pfWeather.setPt1h(pastFcst[2]);
        pfWeather.setPicon(pastFcst[3]);

        // 오늘의 최고, 최저기온
        String[] tmnTmx = weatherService.jsonToMaxMinTemp(response.get("tmnTmx"));
        midWeather.setTmx(tmnTmx[0]);
        midWeather.setTmn(tmnTmx[1]);

        // 2일치 최고, 최저기온, 날씨
        String[][] twoDayFcst = weatherService.jsonToTwoDayFcst(response.get("twoDayFcst"),elementForm);
        midWeather.setFcstTmx(twoDayFcst[0]);
        midWeather.setFcstTmn(twoDayFcst[1]);
        midWeather.setMaxName(twoDayFcst[2]);
        midWeather.setMinName(twoDayFcst[3]);

        // 3 ~ 5일 중기예보(날씨)
        String[][] midFcst = weatherService.jsonToMidFcst(elementForm,response.get("midFcst"));
        midWeather.setWeather(midFcst[0]);
        midWeather.setIcon(midFcst[1]);

        /* 위치정보, 날씨 정보 session에 저장 */
        session.setAttribute("current-weather",currentWeather);
        session.setAttribute("pf-weather",pfWeather);
        session.setAttribute("mid-weather",midWeather);
        session.setAttribute("element",elementForm);

        return elementForm;
    }

    @PostMapping("/saveWeather")
    public ResponseEntity<?> saveWeather(RecordForm recordForm) throws Exception{
        // uid, rdate, rmd, ad, wmsg, temp, humid, precip 은 session에서 받아오기
        // tfeel, hfeel, pfeel은 @RequestBody로 받아오기
        HttpSession session = request.getSession();
        Record record = new Record();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HttpHeaders headers = new HttpHeaders();

        /* 시간, 위치, 날씨 정보(session) */
        ElementForm elementForm = (ElementForm) session.getAttribute("element");
        CurrentWeather current = (CurrentWeather) session.getAttribute("current-weather");

        String rdate = elementForm.getYear() + String.format("-%02d",elementForm.getMonth()) + String.format("-%02d",elementForm.getDate())
                + String.format(" %02d",elementForm.getHour()) + String.format(":%02d",elementForm.getMin()) + String.format(":%02d",elementForm.getSec());

        record.setUid((Long) session.getAttribute("uid"));
        record.setRdate(LocalDateTime.parse(rdate, formatter));
        record.setRmd(elementForm.getYear() + "년 " + elementForm.getMonth() + "월 " + elementForm.getDate() + "일 " + elementForm.getHour() + "시 " + elementForm.getMin() + "분");
        record.setAd(elementForm.getAd());
        record.setWmsg(current.getIcon());
        record.setTemp(Double.parseDouble(current.getT1h()));
        record.setHumid(Integer.parseInt(current.getReh()));
        record.setPrecip(Double.parseDouble(current.getRn1()));

        /* 체감 날씨 기록(<form>)*/
        // 1. 기온 체감
        record.setTfeel(recordService.getTemps(recordForm.getSaveTempComment()));
        // 2. 습도 체감
        record.setHfeel(recordService.getHumidity(recordForm.getSaveHumidComment()));
        // 3. 강수 체감
        record.setPfeel(recordService.getPrecip(recordForm.getSaveRainComment()));

        // 체감 날씨 Record 저장
        Optional<Record> savedRecord = recordService.saveRecord(record);
        headers.setLocation(URI.create(savedRecord.isEmpty() ? "/alert" : "/weather"));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

    }

    @GetMapping("/alert")
    public ModelAndView alertRecord(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("msg","해당 날짜, 시간의 기록이 이미 존재합니다.");
        modelAndView.addObject("url","/weather");

        modelAndView.setViewName("alert");
        return modelAndView;
    }

}
