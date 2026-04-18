package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MyProxyProvider {

    // 被代理的目标对象
    private final SmartAnimalable target_obj;

    // 构造器传入目标对象
    public MyProxyProvider(SmartAnimalable target_obj) {
        // 增加空指针校验
        if (target_obj == null) {
            throw new IllegalArgumentException("目标对象不能为空！");
        }
        this.target_obj = target_obj;
    }

    /**
     * 获取代理对象（方法增加注释，更规范）
     * @return 代理对象
     */
    public SmartAnimalable getProxy() {
        // 1. 获取类加载器
        ClassLoader classLoader = target_obj.getClass().getClassLoader();
        // 2. 获取目标对象实现的所有接口
        Class<?>[] interfaces = target_obj.getClass().getInterfaces();
        // 3. 创建调用处理器
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 处理 null 参数，避免打印 null 列表
                if (args == null) {
                    args = new Object[0];
                }

                // ========== 前置通知 ==========
                System.out.println("====== 方法执行前 ======");
                System.out.println("方法名：" + method.getName());
                System.out.println("参数：" + Arrays.toString(args));

                // 执行目标方法
                Object result = null;
                try {
                    result = method.invoke(target_obj, args);

                    // ========== 返回通知 ==========
                    System.out.println("====== 方法执行成功 ======");
                    System.out.println("返回值：" + result);
                } catch (Exception e) {
                    // ========== 异常通知 ==========
                    System.out.println("====== 方法执行异常 ======");
                    System.out.println("异常信息：" + e.getMessage());
                    throw e; // 抛出原异常，不影响业务
                }

                return result;
            }
        };

        // 4. 创建并返回 JDK 动态代理对象
        return (SmartAnimalable) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }
}