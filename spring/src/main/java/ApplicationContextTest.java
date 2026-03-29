import org.dom4j.DocumentException;

public class ApplicationContextTest {
    public static void main(String[] args) throws DocumentException {
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster monster01 = (Monster) ioc.getBean("monster01");
        System.out.println("Monster01=" + monster01);
        System.out.println("monster.name=" + monster01.getName());
        System.out.println("ok");
    }
}
