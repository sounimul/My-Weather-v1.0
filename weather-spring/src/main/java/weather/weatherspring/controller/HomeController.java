package weather.weatherspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /* '/'로 접속 시 home 화면으로 */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /* '/joinForm으로 접속 시 회원가입창으로 */
    @GetMapping("/joinForm")
    public String joinPage(){
        return "members/createMemberForm";
    }

}
