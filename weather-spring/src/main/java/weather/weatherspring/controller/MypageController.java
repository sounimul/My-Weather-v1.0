package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import weather.weatherspring.domain.Member;
import weather.weatherspring.repository.MemberRepository;
import weather.weatherspring.repository.RecordRepository;
import weather.weatherspring.service.RecordService;

import java.util.List;


@Controller
public class MypageController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final RecordService recordService;

    public MypageController(MemberRepository memberRepository, RecordService recordService) {
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


}
