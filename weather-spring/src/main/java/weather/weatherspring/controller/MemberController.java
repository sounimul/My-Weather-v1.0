package weather.weatherspring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weather.weatherspring.domain.Location;
import weather.weatherspring.domain.Member;
import weather.weatherspring.domain.Wtype;
import weather.weatherspring.entity.*;
import weather.weatherspring.service.LocationService;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.WeatherService;

import java.io.PrintWriter;
import java.util.Optional;

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
        Member member = new Member();
        Location location = new Location();
        CurrentWeather currentWeather = new CurrentWeather();   // 현재 날씨
        BasicWeather pfWeather = new BasicWeather();            // 1시간 전후 날씨
        Wtype wtype = new Wtype();      // 하늘상태 + 강수형태
        MidWeather midWeather = new MidWeather();   // 3-5일치 날씨예보

        /*
        로그인
        */
        member.setId(form.getUserid());
        member.setPw(form.getPw());
        Optional<Member> member2 = memberService.findOne(member);
        // 로그인 실패
        if(member2.isEmpty()){
            System.out.println("아이디 또는 비밀번호가 정확하지 않습니다.");
            request.setAttribute("msg","아이디 또는 비밀번호가 정확하지 않습니다.");
            request.setAttribute("url","/");
            return "alert";
        }

        // 로그인 성공 시 아래 코드 수행
        // 로그인에 성공한 Member 객체를 세션에 저장
        session.setAttribute("uid",member2.get().getUid());


        /*
        날씨 받아오기
        */

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

        return "redirect:/weather";
    }

    @GetMapping("/logout")
    public String logout(){
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
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
