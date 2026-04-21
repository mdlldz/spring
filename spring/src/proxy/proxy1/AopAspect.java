package proxy.proxy1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AopAspect implements InvocationHandler {

    // 被代理的目标对象
    private final Object target;

    public AopAspect(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        long start = System.currentTimeMillis();

        try {
            //  前置通知
            before(method, args);

            //  执行目标方法（环绕核心）
            result = method.invoke(target, args);

            //  返回通知
            afterReturning(method, result);

        } catch (Throwable throwable) {
            //  异常通知
            afterThrowing(method, throwable);
            throw throwable; // 继续抛出，不吞异常

        } finally {
            //  后置最终通知
            long end = System.currentTimeMillis();
            afterFinally(method, end - start);
        }

        return result;
    }

    private void before(Method method, Object[] args) {
        System.out.println("===== 【前置通知】=====");
        System.out.println("方法：" + method.getName());
        System.out.println("参数：" + (args == null ? "无" : Arrays.toString(args)));
        System.out.println("准备执行目标方法...");
    }

    private void afterReturning(Method method, Object result) {
        System.out.println("===== 【返回通知】=====");
        System.out.println("方法：" + method.getName());
        System.out.println("返回值：" + result);
        System.out.println("方法正常执行完成");
    }


    private void afterThrowing(Method method, Throwable throwable) {
        System.out.println("===== 【异常通知】=====");
        System.out.println("方法：" + method.getName());
        System.out.println("异常类型：" + throwable.getClass().getSimpleName());
        System.out.println("异常信息：" + throwable.getMessage());
    }

    private void afterFinally(Method method, long costTime) {
        System.out.println("===== 【后置通知】=====");
        System.out.println("方法：" + method.getName());
        System.out.println("耗时：" + costTime + "ms");
        System.out.println("方法调用流程结束\n");
    }
}
