package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import weather.weatherspring.domain.Member;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.RecordService;
import weather.weatherspring.service.ReviewService;

import java.util.List;

@Controller
public class BoardController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final MemberService memberService;
    @Autowired
    private final RecordService recordService;
    @Autowired
    private final ReviewService reviewService;

    public BoardController(MemberService memberService, RecordService recordService, ReviewService reviewService) {
        this.memberService = memberService;
        this.recordService = recordService;
        this.reviewService = reviewService;
    }

    @GetMapping("/board/user")
    public String userBoard(Model model){
        List<Member> members = memberService.findMembers("Y");
        List<Member> deletedMembers = memberService.findMembers("N");

        model.addAttribute("users",members);
        model.addAttribute("delUsers",deletedMembers);

        return "/admin/userBoard";
    }

    @GetMapping("/board/record")
    public String recordBoard(){

        return "/admin/recordBoard";
    }

    @GetMapping("/board/review")
    public String reviewBoard(){
        return "/admin/reviewBoard";
    }
}
