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
    public String create(JoinForm form){
        Member member = new Member();

        member.setId(form.getUserid());
        member.setPw(form.getPw());
//        member.setUid((++sequence)+"_"+form.getUserid());        // Uid : 랜덤 숫자 + id 로 자동생성 -> ** 추후 수정(중복체크 전 uid가 설정됨)
        member.setNickname(form.getNickname());
        member.setIntro(form.getIntro());
        member.setAvail("Y");

        memberService.join(member);

        return "redirect:/weather";
    }

    @PostMapping("/login")
    public String login(){
        return "redirect:/weather";
    }

}
