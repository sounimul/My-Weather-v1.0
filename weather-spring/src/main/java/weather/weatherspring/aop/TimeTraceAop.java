package weather.weatherspring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component      // 스프링 빈으로 등록
@Aspect
public class TimeTraceAop {
    /* 공통 관심 사항을 타겟팅 */
    @Around("execution(* weather.weatherspring.controller..*(..))")    // 패키지 하위의 모든 것을 적용
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();

        System.out.println("Start: "+joinPoint.toString());

        try{
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            System.out.println("End: "+joinPoint.toString()+" "+timeMs+"ms");
        }
    }
}
