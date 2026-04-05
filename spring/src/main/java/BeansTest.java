import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

class BeanTest {
    //测试Scope
    @Test
    public void testBeanScope(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Cat cat = ioc.getBean("cat",Cat.class);
        Cat cat2 = ioc.getBean("cat",Cat.class);
        Cat cat3 = ioc.getBean("cat",Cat.class);
        System.out.println("cat=" + cat);
        System.out.println("cat2=" + cat2);
        System.out.println("cat3=" + cat3);
    }
    //Bean创建顺序验证
    @Test
    public void testBeanByCreate(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        System.out.println("ok");
    }
    //Bean信息复用测试
    @Test
    public void getBeanByExtends(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster monster11 = ioc.getBean("monster11", Monster.class);
        System.out.println("monster11=" + monster11 );
        Monster monster13 = ioc.getBean("monster13", Monster.class);
        System.out.println("monster13=" + monster13 );
    }
    //FactoryBean测试
    @Test
    public void setBeanByFactoryBean(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster monster04 = ioc.getBean("monster04", Monster.class);
        System.out.println("monster04=" + monster04 );
    }
    //实例工厂测试
    @Test
    public void setBeanByInstanceFactory(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster my_monster02 = ioc.getBean("mymonster02", Monster.class);
        System.out.println("mymonster02=" + my_monster02 );
    }
    //静态工厂测试
    @Test
    public void setBeanByStaticFactory(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster my_monster02= ioc.getBean("my_monster02", Monster.class);
        System.out.println("my_monster02=" + my_monster02 );
    }
    @Test
    public void setBeanByUtilList(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        BookStore bookStore = ioc.getBean("bookStore", BookStore.class);
        System.out.println("bookStore=" + bookStore) ;
    }
    @Test
    public void setBeanByCollection(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Master master = ioc.getBean("master", Master.class);
        System.out.println("master=" + master);
    }

    @Test
    public void getBeanByType(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        MemberServiceImpl memberService = ioc.getBean("memberService2", MemberServiceImpl.class);
        memberService.add();
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