package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weather.weatherspring.domain.Member;
import weather.weatherspring.domain.Record;
import weather.weatherspring.entity.Search;
import weather.weatherspring.service.MemberService;
import weather.weatherspring.service.RecordService;
import weather.weatherspring.service.ReviewService;

import java.util.List;
import java.util.Optional;

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
    public String recordBoard(Model model, @RequestParam("temp") Optional<String> temp, @RequestParam("humid") Optional<String> humid, @RequestParam("prep") Optional<String> prep, @RequestParam(required = false, defaultValue = "0") int page){

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
        }else{
            temp = Optional.of("-1");
            humid = Optional.of("-1");
            prep = Optional.of("-1");
        }

        Page<Record> records = recordService.findAllRecords(search,page);
        int totalPage = records.getTotalPages();
        if(totalPage==0) totalPage = 1;

        model.addAttribute("temp",temp.get());
        model.addAttribute("humid",humid.get());
        model.addAttribute("prep", prep.get());
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("curPage",page);
        model.addAttribute("records",records);

        return "/admin/recordBoard";
    }

    @GetMapping("/board/review")
    public String reviewBoard(){
        return "/admin/reviewBoard";
    }
}
