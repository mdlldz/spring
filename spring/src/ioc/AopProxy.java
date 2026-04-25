package ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopProxy implements InvocationHandler {
    private final Object target;
    private final Object aspect;

    public AopProxy(Object target, Object aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 前置通知
        for (Method m : aspect.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Before.class)
                    && method.getName().contains(m.getAnnotation(Before.class).value())) {
                m.invoke(aspect);
            }
        }
        Object result = method.invoke(target, args);
        // 后置通知
        for (Method m : aspect.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(After.class)
                    && method.getName().contains(m.getAnnotation(After.class).value())) {
                m.invoke(aspect);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }
}