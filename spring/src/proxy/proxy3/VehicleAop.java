package proxy.proxy3;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import java.util.Arrays;

public class VehicleAop {

    // 前置通知
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("\n===== 前置通知 =====");
        System.out.println("方法：" + joinPoint.getSignature().getName());
        System.out.println("参数：" + Arrays.toString(joinPoint.getArgs()));
    }

    // 返回通知
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        System.out.println("===== 返回通知 =====");
        System.out.println("返回值：" + result);
    }

    // 异常通知
    public void afterThrowingMethod(JoinPoint joinPoint, Exception ex) {
        System.out.println("===== 异常通知 =====");
        System.out.println("异常信息：" + ex.getMessage());
    }

    // 最终通知
    public void afterMethod(JoinPoint joinPoint) {
        System.out.println("===== 最终通知 =====");
    }

    // 环绕通知
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            System.out.println("===== 环绕通知：耗时 " + (end - start) + "ms =====");
        }
        return result;
    }
}