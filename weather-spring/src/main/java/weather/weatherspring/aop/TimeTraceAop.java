package weather.weatherspring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component      // 스프링 빈으로 등록
@Aspect
public class TimeTraceAop {
    @Value("${LOG_FILE}")
    private String logFilePath;

    /* 공통 관심 사항을 타겟팅 */
    @Around("execution(* weather.weatherspring.controller.WeatherController.createWeather(..)) || " + "execution(* weather.weatherspring.controller.MemberController.login(..))")    // 패키지 하위의 모든 것을 적용
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();

        System.out.println("Start: "+joinPoint.toString());

        try{
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            logToFile(joinPoint.toString(),timeMs);
            System.out.println("End: "+joinPoint.toString()+" "+timeMs+"ms");

        }
    }

    private void logToFile(String method, long timeMs){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath,true))){
            String logEntry = "Method: "+method+" / Execution time: "+timeMs +"ms\n";
            writer.write(logEntry);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
