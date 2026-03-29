import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Homework_Car {
    @Test
    public void getCar(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("car_beans.xml");
        Car car = context.getBean("car", Car.class);
        System.out.println(car);
    }
}
