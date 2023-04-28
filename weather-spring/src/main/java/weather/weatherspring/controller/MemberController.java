package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import weather.weatherspring.domain.Member;
import weather.weatherspring.entity.MemberForm;
import weather.weatherspring.service.MemberService;

import javax.net.ssl.HandshakeCompletedEvent;
import java.io.PrintWriter;
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        // setAttribute: Session [F7F0ED65551C81744635EB00CF4CB7D0] has already been invalidated 해결하기
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }

    @PostMapping("/update-pw")
    public void changePassword(HttpServletRequest request, MemberForm pwForm, HttpServletResponse response){
        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");
        response.setContentType("text/html; charset=utf-8");

        if(memberService.updatePw(uid,pwForm)) {
            try {
                PrintWriter w = response.getWriter();
                w.write("<script>alert('비밀번호가 변경되었습니다.');history.go(-1);</script>");
                w.flush();
                w.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            return "redirect:/myPage";
        }
        else {
            try {
                PrintWriter w = response.getWriter();
                w.write("<script>alert('기존 비밀번호가 일치하지 않습니다.');history.go(-1);</script>");
                w.flush();
                w.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
//            return "redirect:/myPage";
        }
    }

}
