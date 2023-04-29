package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import weather.weatherspring.domain.Member;
import weather.weatherspring.entity.MemberForm;
import weather.weatherspring.repository.MemberRepository;
import weather.weatherspring.repository.RecordRepository;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.RecordService;

import java.io.PrintWriter;
import java.util.List;


@Controller
public class MypageController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final MemberService memberService;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final RecordService recordService;

    public MypageController(MemberService memberService, MemberRepository memberRepository, RecordService recordService) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.recordService = recordService;
    }

    @GetMapping("/myPage")
    public String myPage(Model model){
        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");
        System.out.println(uid);
        Member member = new Member();
        List<Record> records = recordService.findRecords(uid);
        member= memberRepository.findByUid(uid).get();

        model.addAttribute("user",member);
        model.addAttribute("records",records);

        return "myPage";
    }

    @GetMapping ("/changePwform")
    public String pwForm(){
        return "passwordChange.html";
    }

    @PostMapping("/updateProfile")
    public String newProfile(MemberForm profileForm){
        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");

        Member member = memberService.updateProfile(uid,profileForm).get();

        System.out.println(member.getNickname());

        return "redirect:/myPage";
    }

    @PostMapping("/update-pw")
    public void changePassword(MemberForm pwForm, HttpServletResponse response){
        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");
        response.setContentType("text/html; charset=utf-8");

        if(memberService.updatePw(uid,pwForm)) {
            try {
                PrintWriter w = response.getWriter();
                w.write("<script>alert('비밀번호가 변경되었습니다.');window.close();</script>");
                w.flush();
                w.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        }
    }
}
