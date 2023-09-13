package weather.weatherspring.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String requestURI = request.getRequestURI();
        System.out.println("[Interceptor] requestURI : "+requestURI);
        HttpSession session = request.getSession(false);

        // 로그인 실패
        if(session == null || session.getAttribute("uid") == null) {
            System.out.println("[미인증 사용자 요청]");
            // 로그인으로 redirect
            response.sendRedirect("/");
            return false;
        }
        // 탈퇴한 사용자의 요청
        else if(session.getAttribute("auth").equals("N")) {
            System.out.println("[탈퇴한 사용자 요청]");
            // 회원가입으로 redirect
            response.sendRedirect("/joinForm");
            return false;
        }
        // 로그인 되어있는 경우
        return true;
    }
}
