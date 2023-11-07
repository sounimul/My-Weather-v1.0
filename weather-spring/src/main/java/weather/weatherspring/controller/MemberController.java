package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import weather.weatherspring.entity.Member;
import weather.weatherspring.domain.*;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.WeatherService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@Controller
public class MemberController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final MemberService memberService;
    @Autowired
    private final LocationService locationService;
    @Autowired
    private final WeatherService weatherService;

    public MemberController(MemberService memberService, LocationService locationService, WeatherService weatherService) {
        this.memberService = memberService;
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    @PostMapping("/join")
    public String create(MemberForm form){
        Member member = new Member();
        String[] emailDomain=form.getEmailDomain().split(",");
        form.setEmailDomain(String.join("",emailDomain));
        form.setUserid(form.getEmailLocal()+"@"+form.getEmailDomain());

        member.setId(form.getUserid());
        member.setPw(form.getPw());
        member.setNickname(form.getNickname());
        member.setFvweather(form.getFvweather());
        member.setAvail("Y");

        memberService.join(member);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(MemberForm form, ElementForm elementForm) throws Exception{
        HttpSession session = request.getSession();
        CurrentWeather currentWeather = new CurrentWeather();   // 현재 날씨
        BasicWeather pfWeather = new BasicWeather();            // 1시간 전후 날씨
        MidWeather midWeather = new MidWeather();   // 3-5일치 날씨예보


        /*
        로그인
        */
        // 입력한 아이디, 비밀번호로 회원 확인하기
        Optional<Member> member = memberService.findOne(form.getUserid(),form.getPw());
        // 로그인 실패
        if(member.isEmpty()){
            System.out.println("아이디 또는 비밀번호가 정확하지 않습니다.");
            request.setAttribute("msg","아이디 또는 비밀번호가 정확하지 않습니다.");
            request.setAttribute("url","/");
            return "alert";
        }

        // 로그인 성공
        session.setAttribute("uid",member.get().getUid());
        session.setAttribute("auth",member.get().getAvail());
        // 로그인한 아이디가 관리자일 경우 관리자 페이지로 이동
        if(member.get().getAvail().equals("A"))
            return "redirect:/board/user";


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
        weatherService.getForecast(elementForm,0).onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather"))).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("tmnTmx",e));
        // 단기예보 - 2일치 예보
        weatherService.getForecast(elementForm,1).onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather"))).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("twoDayFcst",e));
        // 초단기실황 - 현재 날씨 / 초단기예보 - 현재날씨 + 1시간후 날씨
        weatherService.getForecast2(elementForm).onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather"))).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("curFutFcst",e));
        weatherService.getForecast3(elementForm,1).onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather"))).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("curWeather",e));
        // 초단기예보 - 1시간 전 날씨
        weatherService.getForecast3(elementForm,0).onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather"))).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("pastFcst",e));
        // 중기예보 - 3~5일 최고, 최저기온 및 날씨
        weatherService.getMidForecast(elementForm,areaCode).onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.FOUND,"redirect:/weather"))).doOnTerminate(() -> cdl.countDown()).subscribe(e -> response.put("midFcst",e));

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

        return "redirect:/weather";
    }

    @PostMapping("/checkId")
    @ResponseBody
    public Object checkId(@RequestParam("id") String id, @RequestParam("mail") String mail){
        String user = id+"@"+mail;
        System.out.println(user);

        Optional<Member> member = memberService.validateDuplicateId(user);

        if(member.isPresent()){
            System.out.println("중복된 아이디입니다");
            return "";
        }
        System.out.println("사용 가능한 아이디입니다");

        return user;
    }

}
