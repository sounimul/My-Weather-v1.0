package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weather.weatherspring.domain.dto.*;
import weather.weatherspring.domain.entity.Member;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.WeatherService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
public class MemberController {
    @Autowired
    private HttpServletRequest request;

    private final MemberService memberService;
    private final LocationService locationService;
    private final WeatherService weatherService;

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
