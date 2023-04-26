package weather.weatherspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/myPage")
    public String myPage(){
        HttpSession session = request.getSession();
        Long uid=(Long) session.getAttribute("uid");
        System.out.println(uid);

        return "myPage.html";
    }
}
