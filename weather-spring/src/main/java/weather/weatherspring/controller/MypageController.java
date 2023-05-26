package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weather.weatherspring.domain.Member;
import weather.weatherspring.domain.Record;
import weather.weatherspring.entity.MemberForm;
import weather.weatherspring.entity.Search;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.RecordService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;


@Controller
public class MypageController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final MemberService memberService;
    @Autowired
    private final RecordService recordService;

    public MypageController(MemberService memberService, RecordService recordService) {
        this.memberService = memberService;
        this.recordService = recordService;
    }

    @GetMapping("/myPage")
    public String myPage(Model model, @RequestParam("temp") Optional<String> temp, @RequestParam("humid") Optional<String> humid, @RequestParam("prep") Optional<String> prep){
        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");
        Search search=new Search();
        String[] value;
        // 받아온 검색조건 string을 숫자로 변환하여 search 객체에 넣기
        if(temp.isPresent() & humid.isPresent() & prep.isPresent()){
            if(!temp.get().equals("-1")){
                value = temp.get().split("n");
                search.setStartTemp(Double.parseDouble(value[0])); search.setEndTemp(Double.parseDouble(value[1]));
                System.out.println("temp: "+search.getStartTemp()+" "+search.getEndTemp());
            }
            if(!humid.get().equals("-1")){
                value=humid.get().split("n");
                search.setStartHumid(Integer.parseInt(value[0])); search.setEndHumid(Integer.parseInt(value[1]));
                System.out.println("humid: "+search.getStartHumid()+" "+search.getEndHumid());
            }
            if(!prep.get().equals("-1")){
                value=prep.get().split("n");
                search.setStartPrep(Double.parseDouble(value[0])); search.setEndPrep(Double.parseDouble(value[1]));
                System.out.println("prep: "+search.getStartPrep()+" "+search.getEndPrep());
            }
        }

        List<Record> records = recordService.findRecordList(uid);
//        List<Record> records = recordService.findRecordList(uid,search);
        Member member= memberService.findMember(uid).get();

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

        try{
            PrintWriter w = response.getWriter();
            if(memberService.updatePw(uid,pwForm)){
                w.write("<script>alert('비밀번호가 변경되었습니다.');window.close();</script>");
                w.flush();
                w.close();
            }
            else{
                w.write("<script>alert('기존 비밀번호가 일치하지 않습니다.');history.go(-1);</script>");
                w.flush();
                w.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
