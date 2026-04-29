package ioc;

import org.aspectj.lang.annotation.AfterThrowing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class AopProxy implements InvocationHandler {

    // 目标对象（被代理的业务Bean）
    private final Object target;
    // 切面对象（日志/事务等增强类）
    private final Object aspect;
    // 缓存切面方法，提升匹配效率
    private final Map<String, Method> beforeMethods = new HashMap<>();
    private final Map<String, Method> afterMethods = new HashMap<>();
    private final Map<String, Method> afterThrowingMethods = new HashMap<>();

    public AopProxy(Object target, Object aspect) {
        this.target = target;
        this.aspect = aspect;
        // 初始化时缓存所有增强方法，避免循环遍历
        cacheAdviceMethods();
    }

    /**
     * 缓存切面里的 @Before @After @AfterThrowing 方法
     */
    private void cacheAdviceMethods() {
        Method[] methods = aspect.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true); // 开启暴力访问
            if (method.isAnnotationPresent(Before.class)) {
                Before before = method.getAnnotation(Before.class);
                beforeMethods.put(before.value(), method);
            }
            if (method.isAnnotationPresent(After.class)) {
                After after = method.getAnnotation(After.class);
                afterMethods.put(after.value(), method);
            }
            if (method.isAnnotationPresent(AfterThrowing.class)) {
                AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
                afterThrowingMethods.put(afterThrowing.value(), method);
            }
        }
    }

    /**
     * 代理核心方法：完整模拟 Spring AOP 执行链
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        String methodName = method.getName();

        try {
            // ===================== 1. 前置通知 @Before =====================
            for (Map.Entry<String, Method> entry : beforeMethods.entrySet()) {
                if (matchExpression(methodName, entry.getKey())) {
                    entry.getValue().invoke(aspect);
                }
            }

            // ===================== 2. 执行目标方法 =====================
            result = method.invoke(target, args);

            // ===================== 3. 返回通知 @AfterReturning =====================
            // 这里可扩展 AfterReturning

        } catch (Throwable throwable) {
            // ===================== 4. 异常通知 @AfterThrowing =====================
            for (Map.Entry<String, Method> entry : afterThrowingMethods.entrySet()) {
                if (matchExpression(methodName, entry.getKey())) {
                    entry.getValue().invoke(aspect);
                }
            }
            throw throwable; // 抛出原始异常

        } finally {
            // ===================== 5. 最终通知 @After =====================
            for (Map.Entry<String, Method> entry : afterMethods.entrySet()) {
                if (matchExpression(methodName, entry.getKey())) {
                    entry.getValue().invoke(aspect);
                }
            }
        }

        return result;
    }

    /**
     * AOP 表达式匹配（支持 * 通配符）
     * 例如：add*、*User、*list*
     */
    private boolean matchExpression(String methodName, String expression) {
        if ("*".equals(expression)) {
            return true;
        }
        if (expression.startsWith("*") && expression.endsWith("*")) {
            String key = expression.substring(1, expression.length() - 1);
            return methodName.contains(key);
        }
        if (expression.startsWith("*")) {
            String suffix = expression.substring(1);
            return methodName.endsWith(suffix);
        }
        if (expression.endsWith("*")) {
            String prefix = expression.substring(0, expression.length() - 1);
            return methodName.startsWith(prefix);
        }
        return methodName.equals(expression);
    }

    /**
     * 获取 JDK 动态代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }
}