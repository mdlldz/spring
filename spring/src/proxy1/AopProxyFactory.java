package proxy1;

import java.lang.reflect.Proxy;

public class AopProxyFactory {

    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AopAspect(target)
        );
    }
}