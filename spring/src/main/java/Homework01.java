import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Homework01 {
    @Test
    public void getMonster() {
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("beans.xml");

        Monster monster01 = ioc.getBean("monster01", Monster.class);
        System.out.println("monster01=" + monster01);
        System.out.println("monster01.monsterId=" + monster01.getMonsterId());

        Monster monster02 = ioc.getBean("monster02", Monster.class);
        System.out.println("monster02=" + monster02);
        System.out.println("monster02.monsterId=" + monster02.getMonsterId());
    }
}