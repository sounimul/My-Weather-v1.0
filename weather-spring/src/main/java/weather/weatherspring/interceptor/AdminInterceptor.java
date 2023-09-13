package weather.weatherspring.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String requestURI = request.getRequestURI();
        System.out.println("[Interceptor] requestURI : "+requestURI);
        HttpSession session = request.getSession(false);

        // 권한이 없는 사용자
        if(!session.getAttribute("auth").equals("A")){
            System.out.println("[권한이 없는 사용자 요청]");
            response.sendRedirect("/myPage");
            return false;
        }
        // 로그인 되어있는 경우
        return true;
    }

}
