import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.dom4j.DocumentException;

public class ApplicationContextTest {
    public static void main(String[] args) throws DocumentException {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("beans.xml");
        Monster monster01 = ioc.getBean("monster01", Monster.class);

        System.out.println("Monster01=" + monster01);
        System.out.println("monster.name=" + monster01.getName());
        System.out.println("ok");
    }
}
