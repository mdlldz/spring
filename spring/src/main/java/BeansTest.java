import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

class BeanTest {
    @Test
    public void getBeanByType(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster bean = (Monster) ioc.getBean(Monster.class);
        System.out.println("bean=" + bean);
    }
    @Test
    public void setBeanByRef(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        MemberServiceImpl memberService = ioc.getBean("memberService", MemberServiceImpl.class);
        memberService.add();
    }
    @Test
    public void getBeanByp(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster monster04 = ioc.getBean("monster04",Monster.class);
        System.out.println("monster04="  + monster04);
    }
@Test
public void setBeanByConstructor() {
    ApplicationContext ioc = new ApplicationContext("beans.xml");
    Monster monster03 = ioc.getBean("monster03", Monster.class);
    System.out.println("构造器被使用");
    System.out.println("Monster03 = " + monster03);
}
    @Test
    public void getMonster() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Object monster01 = context.getBean("monster01");
        Monster monster02 = (Monster) context.getBean("monster01");
        System.out.println("monster01=" + monster01 + "，monster01运行类型为" + monster01.getClass().getName());
        System.out.println("monster02=" + monster02 + "，获取属性" + monster02.getName());
        Monster monster03 = context.getBean(Monster.class);
        System.out.println("monster03=" + monster03);
        System.out.println("monster03.name=" + monster03.getName());
    }
    @Test
    public void classPath(){
        File file = new File(this.getClass().getResource("/").getPath());
        System.out.println("file=" + file);
    }
}