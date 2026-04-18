package proxy2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class VehicleProxyProvider {
    //定义一个属性
    //target_vehicle 表示真正要执行的对象
    // 该对象实现了Vehicle接口
    private final Vehicle target_vehicle;  // 改成 final，防止被篡改

    // 构造器增加空指针校验
    public VehicleProxyProvider(Vehicle target_vehicle) {
        if (target_vehicle == null) {
            throw new IllegalArgumentException("目标交通工具对象不能为空！");
        }
        this.target_vehicle = target_vehicle;
    }

    //编写一个方法，可以返回一个代理对象
    public Vehicle getProxy(){
        //得到类加载器
        ClassLoader classLoader =
                target_vehicle.getClass().getClassLoader();
        //得到要代理的对象/被执行对象 的接口信息，底层是通过接口来调用
        Class<?>[] interfaces = target_vehicle.getClass().getInterfaces();
        //创建InvocationHandler 对象
        //因为InvocationHandLer 是接口，所以我们可以通过匿名对象的方式米创建该对象
        //* public interface InvocationHandler {
        //            public Object invoke(Object proxy, Method method, Object[] args)throws Throwable;
        //*
        //invoke方法将来执行target_vehicle方法时会用到
        InvocationHandler invocationHandler = new InvocationHandler(){
            @Override
            public Object invoke(Object o, Method method,Object [] objects) throws Throwable{
                System.out.println("交通工具开始运行");
                // 反射基础 => OOP
                // method 是?:   public abstract void proxy2.Vehicle.run
                // target_vehicle 是?    Ship对象（或Car对象，也就是你要代理的真实对象）
                // args 是null（因为run方法没有参数）
                // 这里通过反射+动态绑定机制，就会执行到被代理对象的方法
                // 执行完毕就返回

                // 处理参数为 null 的情况，避免报错
                Object[] args = objects == null ? new Object[0] : objects;

                Object result = method.invoke(target_vehicle, args);
                System.out.println("交通工具停止运行");
                return result;
            }
        };
        //public static Object newProxyInstance(ClassLoader loader,
        //                                      Class<?>[] interfaces,
        //                                      InvocationHandler h)
        //1.Proxy.newProxyInstance() 可以返回一个理对象
        // 2.ClassLoader loader:类的加载器.
        //3.Class<?>[]interfaces 就是将来要代理的对象的接口信息
        // 4.InvocationHandler h 调用处理器/对象 有一个非常重要的方法invoke
        Vehicle proxy =
                (Vehicle)Proxy.newProxyInstance(classLoader,interfaces,invocationHandler);
        return proxy;
    }
}