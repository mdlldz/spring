package proxy3;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class VehicleAop {

    // 切点：匹配 Vehicle 接口下所有方法
    @Pointcut("execution(* com.example.Vehicle.*(..))")
    public void vehiclePointcut() {}

    // 1. 前置通知
    @Before("vehiclePointcut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("\n===== 前置通知 =====");
        System.out.println("方法名：" + joinPoint.getSignature().getName());
        System.out.println("参数：" + Arrays.toString(joinPoint.getArgs()));
    }

    // 2. 返回通知
    @AfterReturning(value = "vehiclePointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("===== 返回通知 =====");
        System.out.println("返回值：" + result);
    }

    // 3. 异常通知
    @AfterThrowing(value = "vehiclePointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        System.out.println("===== 异常通知 =====");
        System.out.println("异常：" + ex.getMessage());
    }

    // 4. 最终通知
    @After("vehiclePointcut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("===== 最终通知 =====");
    }

    // 5. 环绕通知（最强大）
    @Around("vehiclePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed(); // 执行目标方法
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("===== 环绕通知：耗时 " + (end - start) + "ms =====");
        }
        return result;
    }
}
