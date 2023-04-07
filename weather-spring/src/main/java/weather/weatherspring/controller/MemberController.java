package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import weather.weatherspring.domain.Member;
import weather.weatherspring.entity.MemberForm;
import weather.weatherspring.service.MemberService;

import java.util.Optional;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public String create(MemberForm form){
        Member member = new Member();

        member.setId(form.getUserid());
        member.setPw(form.getPw());
        member.setNickname(form.getNickname());
        member.setFvweather(form.getFvweather());
        member.setAvail("Y");

        memberService.join(member);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(MemberForm form, HttpServletRequest request) throws Exception{
        Member member = new Member();

        member.setId(form.getUserid());
        member.setPw(form.getPw());

        Optional<Member> member2 = memberService.findOne(member);

        // 로그인에 성공한 Member 객체를 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("uid",member2.get().getUid());

        return "redirect:/weather";
    }

}
