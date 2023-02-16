package weather.weatherspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @PostMapping("/join")
    public String join(){
        return "redirect:/weather";
    }

    @PostMapping("/login")
    public String login(){
        return "redirect:/weather";
    }

}
