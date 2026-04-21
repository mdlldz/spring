package proxy.proxy1;

public class AopTest {
    public static void main(String[] args) {
        SmartAnimalable dog = new Dog();
        // 获取代理对象
        SmartAnimalable proxy = AopProxyFactory.createProxy(dog);
        proxy.getSum(10, 20);
        System.out.println("------------------------");
        proxy.getSub(30, 10);
    }
}
