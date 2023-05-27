package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weather.weatherspring.domain.Location;
import weather.weatherspring.domain.Record;
import weather.weatherspring.domain.Wtype;
import weather.weatherspring.entity.*;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.RecordService;
import weather.weatherspring.service.WeatherService;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public WeatherController(LocationService locationService, WeatherService weatherService, RecordService recordService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.recordService = recordService;
    }

    /* weather view */
    @GetMapping("/weather")
    public ModelAndView weather() throws IOException{

        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        CurrentWeather cw=new CurrentWeather();
        BasicWeather pfw = new BasicWeather();
        ElementForm ef = new ElementForm();
        MidWeather mw = new MidWeather();

        // session으로부터 uid 가져와 modelAndView에 저장
        Long uid=(Long) session.getAttribute("uid");
        modelAndView.addObject("uid",uid);

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

        // session으로부터 중기날씨예보 가져와 modelAndView에 저장
        MidWeather midWeather = (MidWeather) session.getAttribute("mid-weather");
        if (midWeather==null) modelAndView.addObject("mid",mw);
        else modelAndView.addObject("mid",midWeather);

        // 현재 날짜, 시간
        ElementForm elementForm = (ElementForm) session.getAttribute("element");
        if(elementForm==null) modelAndView.addObject("element",ef);
        else modelAndView.addObject("element",elementForm);

        modelAndView.setViewName("weather");

        System.out.println("weather(get) "+elementForm.getHour()+" "+elementForm.getMin());

        return modelAndView;
    }

    /* 현재 위치의 날씨 구하기 */
    @PostMapping("/weather")
    public Object createWeather(@RequestBody ElementForm elementForm){
        Location location = new Location();
        HttpSession session = request.getSession();
        CurrentWeather currentWeather = new CurrentWeather();   // 현재 날씨
        BasicWeather pfWeather = new BasicWeather();            // 1시간 전후 날씨
        Wtype wtype = new Wtype();      // 하늘상태 + 강수형태
        MidWeather midWeather = new MidWeather();   // 5일치 날씨예보

        //session에서 id 가져오기
        Long uid=(Long) session.getAttribute("uid");
        location.setUid(uid);
        location.setLatitude(elementForm.getLatitude());
        location.setLongitude(elementForm.getLongitude());

        /*
        주소 처리
         */
        // 위도, 경도 -> 기상청 x,y좌표
        elementForm=locationService.getXY(elementForm);
        location.setXcoor(elementForm.getXcoor());
        location.setYcoor(elementForm.getYcoor());

        // 위도,경도 -> 주소
        JsonNode address=locationService.getAddress(elementForm).block();
        location.setAd(address.get("documents").get(1).get("address_name").asText());   // get(0) : 법정동, get(1) : 행정동 -> 기상청은 행정동이 기준

        // 주소 -> 중기예보구역 코드
        String areaCode= locationService.getAreaCode(location.getAd());

        /*
        날씨 예보 받아오고 처리
         */
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
        JsonNode midFcst=weatherService.getMidForecast(elementForm, areaCode).block();

        //현재 시간 날씨 - 초단기실황 + 초단기예보(현재 하늘상태)
        currentWeather.setPty(srtNcst.get("response").get("body").get("items").get("item").get(0).get("obsrValue").asText());   // 현재 강수상태
        currentWeather.setReh(srtNcst.get("response").get("body").get("items").get("item").get(1).get("obsrValue").asText());   // 현재 습도
        currentWeather.setRn1(srtNcst.get("response").get("body").get("items").get("item").get(2).get("obsrValue").asText());   // 현재 강수량
        currentWeather.setT1h(srtNcst.get("response").get("body").get("items").get("item").get(3).get("obsrValue").asText());   // 현재 기온
        currentWeather.setSky(srtFcst.get("response").get("body").get("items").get("item").get(18).get("fcstValue").asText());   // 현재 하늘상태
        if(currentWeather.getPty().equals("0")) wtype.setWcode("SKY_"+currentWeather.getSky());
        else wtype.setWcode("PTY_"+currentWeather.getPty());
        wtype=weatherService.findWtype(wtype.getWcode()).get();
        currentWeather.setStatus(wtype.getMessage());
        currentWeather.setIcon(wtype.getWname());

        // 1시간 후 기온,날씨 - 초단기예보
        pfWeather.setFpty(srtFcst.get("response").get("body").get("items").get("item").get(7).get("fcstValue").asText());
        pfWeather.setFsky(srtFcst.get("response").get("body").get("items").get("item").get(19).get("fcstValue").asText());
        pfWeather.setFt1h(srtFcst.get("response").get("body").get("items").get("item").get(25).get("fcstValue").asText());
        if (pfWeather.getFpty().equals("0")) wtype.setWcode("SKY_"+pfWeather.getFsky());
        else wtype.setWcode("PTY_"+pfWeather.getFpty());
        pfWeather.setFicon(weatherService.findWtype(wtype.getWcode()).get().getWname());

        // 1시간 전 기온, 날씨 - 초단기예보
        pfWeather.setPpty(srtFcst2.get("response").get("body").get("items").get("item").get(6).get("fcstValue").asText());
        pfWeather.setPsky(srtFcst2.get( "response").get("body").get("items").get("item").get(18).get("fcstValue").asText());
        pfWeather.setPt1h(srtFcst2.get("response").get("body").get("items").get("item").get(24).get("fcstValue").asText());
        if (pfWeather.getPpty().equals("0")) wtype.setWcode("SKY_"+pfWeather.getPsky());
        else wtype.setWcode("PTY_"+pfWeather.getPpty());
        pfWeather.setPicon(weatherService.findWtype(wtype.getWcode()).get().getWname());

        // 오늘의 최고, 최저기온
        for(int i=0;i<290;i++){
            String cate=vilFcst.get("response").get("body").get("items").get("item").get(i).get("category").asText();
            if(cate.equals("TMN")) midWeather.setTmn(vilFcst.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText());
            else if(cate.equals("TMX")) midWeather.setTmx(vilFcst.get("response").get("body").get("items").get("item").get(i).get("fcstValue").asText());
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
        midWeather.setFcstTmx(fcstTmx); midWeather.setFcstTmn(fcstTmn);
        midWeather.setMaxName(maxName); midWeather.setMinName(minName);

        // 3 ~ 5일 중기예보(날씨)
        String[] wea3days = {"","","","","",""};
        String[] icon3days = {"","","","","",""};
        if(elementForm.getHour()<6) {    // 4,5,6일 후 자료 가져오기
            wea3days[0] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf4Am").asText();
            wea3days[1] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf4Pm").asText();
            wea3days[2] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf5Am").asText();
            wea3days[3] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf5Pm").asText();
            wea3days[4] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf6Am").asText();
            wea3days[5] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf6Pm").asText();
        } else{     // 3,4,5일 후 자료 가져오기
            wea3days[0] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf3Am").asText();
            wea3days[1] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf3Pm").asText();
            wea3days[2] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf4Am").asText();
            wea3days[3] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf4Pm").asText();
            wea3days[4] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf5Am").asText();
            wea3days[5] = midFcst.get("response").get("body").get("items").get("item").get(0).get("wf5Pm").asText();
        }
        for (int i=0; i<6; i++)
            icon3days[i]= weatherService.getIcon(wea3days[i]);
        midWeather.setWeather(wea3days);
        midWeather.setIcon(icon3days);

        // 위치정보, 날씨 정보 session에 저장
        session.setAttribute("address",location.getAd());
        session.setAttribute("current-weather",currentWeather);
        session.setAttribute("pf-weather",pfWeather);
        session.setAttribute("mid-weather",midWeather);
        session.setAttribute("element",elementForm);

        System.out.println("weather(post) "+elementForm.getHour()+" "+elementForm.getMin());

        return elementForm;
    }

    @PostMapping("/saveWeather")
    public ResponseEntity<?> saveWeather(RecordForm recordForm) throws Exception{
        // uid, rdate, rmd, ad, wmsg, temp, humid, precip 은 session에서 받아오기
        // tfeel, hfeel, pfeel은 @RequestBody로 받아오기
        HttpSession session = request.getSession();
        Record record = new Record();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        /* 시간, 위치, 날씨 정보(session) */
        ElementForm elementForm = (ElementForm) session.getAttribute("element");
        CurrentWeather current = (CurrentWeather) session.getAttribute("current-weather");

        String rdate = elementForm.getYear() + String.format("-%02d",elementForm.getMonth()) + String.format("-%02d",elementForm.getDate())
                + String.format(" %02d",elementForm.getHour()) + String.format(":%02d",elementForm.getMin()) + String.format(":%02d",elementForm.getSec());
        String rmd = elementForm.getMonth() + "월 " + elementForm.getDate() + "일";

        record.setUid((Long) session.getAttribute("uid"));
        record.setRdate(LocalDateTime.parse(rdate, formatter));
        record.setRmd(rmd);
        record.setAd((String) session.getAttribute("address"));
        record.setWmsg(current.getIcon());
        record.setTemp(Double.parseDouble(current.getT1h()));
        record.setHumid(Integer.parseInt(current.getReh()));
        record.setPrecip(Double.parseDouble(current.getRn1()));

        /* 체감 날씨 기록(<form>) */
        // 1. 기온 체감
        switch (recordForm.getSaveTempComment()){
            case "melting":
                record.setTfeel("무더워요");
                break;
            case "hot":
                record.setTfeel("더워요");
                break;
            case "warm":
                record.setTfeel("따뜻해요");
                break;
            case "mild":
                record.setTfeel("포근해요");
                break;
            case "cool":
                record.setTfeel("시원해요");
                break;
            case "pleasantly cool":
                record.setTfeel("선선해요");
                break;
            case "chilly":
                record.setTfeel("쌀쌀해요");
                break;
            case "cold":
                record.setTfeel("추워요");
                break;
            case "freezing cold":
                record.setTfeel("매우 추워요");
                break;
            default:
                record.setTfeel("-");
        }
        // 2. 습도 체감
        switch(recordForm.getSaveHumidComment()){
            case "humid":
                record.setHfeel("습해요");
                break;
            case "fresh":
                record.setHfeel("쾌적해요");
                break;
            case "dry":
                record.setHfeel("건조해요");
                break;
            default:
                record.setHfeel("-");
        }
        // 3. 강수 체감
        switch(recordForm.getSaveRainComment()){
            case "no":
                record.setPfeel("안와요");
                break;
            case "light":
                record.setPfeel("약한 비");
                break;
            case "rain":
                record.setPfeel("보통 비");
                break;
            case "heavy":
                record.setPfeel("강한 비");
                break;
            case "shower":
                record.setPfeel("쏟아져요");
                break;
            default:
                record.setPfeel("-");
        }

        System.out.println(record.getRdate());
        System.out.println(record.getAd());
        System.out.println(record.getTfeel());
        System.out.println(record.getHfeel());
        System.out.println(record.getPfeel());

        // 체감 날씨 Record 저장
        recordService.saveRecord(record);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/weather"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

    }

}
