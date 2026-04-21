package proxy.proxy3;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestAop {
    @Test
    public void Test(){
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        Vehicle vehicle = context.getBean(Vehicle.class);
        vehicle.run();
        System.out.println("-------------------");
        vehicle.fly(10000);
}
    @Test
    public void TestXml(){
        // 加载 XML 配置
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Vehicle vehicle = (Vehicle) context.getBean("car");

        vehicle.run();
        System.out.println("-------------------");
        vehicle.fly(10000);
    }
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);

        Vehicle vehicle = context.getBean(Vehicle.class);

        vehicle.run();
        System.out.println("-------------------");
        vehicle.fly(10000);
    }
}