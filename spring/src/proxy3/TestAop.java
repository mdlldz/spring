package proxy3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestAop {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);

        Vehicle vehicle = context.getBean(Vehicle.class);

        vehicle.run();
        System.out.println("-------------------");
        vehicle.fly(10000);
    }
}