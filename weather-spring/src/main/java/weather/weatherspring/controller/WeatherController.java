package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.domain.dto.*;
import weather.weatherspring.domain.entity.Member;
import weather.weatherspring.domain.entity.Record;
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
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class WeatherController {
    @Autowired
    private HttpServletRequest request;

    private final LocationService locationService;
    private final WeatherService weatherService;
    private final RecordService recordService;
    private final MemberService memberService;

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

        /*
        주소 처리
         */
        // 위도, 경도 -> 기상청 x,y좌표
        elementForm=locationService.transformCoordinate(elementForm);
        // 위도,경도를 행정구역으로 변환하는 카카오 api 호출
        elementForm.setAd(locationService.getAddress(elementForm));
        // 주소 -> 중기예보구역 코드
        String areaCode= locationService.getAreaCode(elementForm.getAd());


        /*
        날씨 예보 받아오고 처리
         */
        CompletableFuture<JsonNode> future1 = weatherService.getForecast(elementForm, 0).toFuture();
        CompletableFuture<JsonNode> future2 = weatherService.getForecast(elementForm, 1).toFuture();
        CompletableFuture<JsonNode> future3 = weatherService.getForecast2(elementForm).toFuture();
        CompletableFuture<JsonNode> future4 = weatherService.getForecast3(elementForm,1).toFuture();
        CompletableFuture<JsonNode> future5 = weatherService.getForecast3(elementForm,0).toFuture();
        CompletableFuture<JsonNode> future6 = weatherService.getMidForecast(elementForm,areaCode).toFuture();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3, future4, future5, future6);

        Map<String, JsonNode> response = allFutures.thenApply(v -> {
            Map<String,JsonNode> resultMap = new HashMap<>();
            resultMap.put("tmnTmx",future1.join());
            resultMap.put("twoDayFcst",future2.join());
            resultMap.put("curFutFcst",future3.join());
            resultMap.put("curWeather", future4.join());
            resultMap.put("pastFcst", future5.join());
            resultMap.put("midFcst", future6.join());
            return resultMap;
        }).join();

        String[][] curFutFcst = weatherService.jsonToCurFutFcst(response.get("curFutFcst"), response.get("curWeather"));
        //현재 시간 날씨 - 초단기실황 + 초단기예보(현재 하늘상태)
        CurrentWeather currentWeather = new CurrentWeather(curFutFcst[0]);   // 현재 날씨

        // 1시간 전 기온, 날씨 - 초단기예보
        String[] pastFcst = weatherService.jsonToPastFcst(response.get("pastFcst"));
        // 1시간 후 기온,날씨(초단기예보) + 1시간 전 기온,날씨 (초단계예보)
        BasicWeather pfWeather = new BasicWeather(curFutFcst[1],pastFcst);            // 1시간 전후 날씨

        // 오늘의 최고, 최저기온
        String[] tmnTmx = weatherService.jsonToMaxMinTemp(response.get("tmnTmx"));
        // 2일치 최고, 최저기온, 날씨
        String[][] twoDayFcst = weatherService.jsonToTwoDayFcst(response.get("twoDayFcst"),elementForm);
        // 3 ~ 5일 중기예보(날씨)
        String[][] midFcst = weatherService.jsonToMidFcst(elementForm,response.get("midFcst"));
        // 5일치 날씨예보
        MidWeather midWeather = new MidWeather(tmnTmx,twoDayFcst,midFcst);


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
