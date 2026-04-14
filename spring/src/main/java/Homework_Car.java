
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Homework_Car {

    public void getCar() {
        // 补全类名，删除错误的命名参数
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Car car = context.getBean("car", Car.class);
        System.out.println(car);
    }
}