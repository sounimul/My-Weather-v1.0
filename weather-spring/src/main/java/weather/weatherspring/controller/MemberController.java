package weather.weatherspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import weather.weatherspring.domain.Member;
import weather.weatherspring.service.MemberService;

@Controller
public class MemberController {

    private final MemberService memberService;
    private static long sequence=0L;

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
        member.setIntro(form.getIntro());
        member.setAvail("Y");

        memberService.join(member);

        return "redirect:/weather";
    }

    @PostMapping("/login")
    public String login(MemberForm form){
        Member member = new Member();

        member.setId(form.getUserid());
        member.setPw(form.getPw());

        memberService.findOne(member);

        return "redirect:/weather";
    }

}
