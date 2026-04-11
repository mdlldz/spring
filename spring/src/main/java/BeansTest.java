import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import web.OrderAction;
import component.UserDao;
import java.io.File;
import component.UserService;
import component.UserAction;
class BeanTest {
    //注解配置属性
    @Test
    public void setBeanByAnnotation(){
        ApplicationContext ioc = new ApplicationContext("beans04.xml");
        UserDao userDao = ioc.getBean(UserDao.class);
        UserService userService = ioc.getBean(UserService.class);
        UserAction userAction = ioc.getBean(UserAction.class);
        System.out.println("userDao=" + userDao);
        System.out.println("userService=" + userService);
        System.out.println("userAction=" + userAction);

    }
    //Spring EI
    @Test
    public void testSpringEI(){
        ApplicationContext ioc = new ApplicationContext("beans02.xml");
        SpELBean spELBean = ioc.getBean("spELBean", SpELBean.class);
        System.out.println("spELBean=" + spELBean);
    }
    //自动装配
    @Test
    public void testBeanByAutowire(){
        ApplicationContext ioc = new ApplicationContext("beans02.xml");
        OrderAction orderAction = ioc.getBean("orderAction", OrderAction.class);
        System.out.println(orderAction.getOrderService());
        System.out.println(orderAction.getOrderService().getOrderDao());
    }
    //属性文件给bean属性赋值
    @Test
    public void testProperties(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Monster monster1000 = ioc.getBean("monster1000", Monster.class);
        System.out.println("monster1000=" + monster1000);
    }
    //测试后置处理器
    @Test
    public void testBeanPostProcessor(){
        ApplicationContext ioc = new ApplicationContext("beans02.xml");
        House house = ioc.getBean("house", House.class);
        System.out.println("使用house=" + house);
    }
    //测验bean的生命周期
    @Test
    public void testBeanLife(){
        ApplicationContext ioc = new ApplicationContext("beans.xml");
        Object bean = ioc.getBean("house",House.class);
        System.out.println("bean =" + bean);
        //1. ioc的编译类型 ApplicationContext ，运行类型 ClassPathXmlApplicationContext
    //2. 因为ClassPathXmlApplicationContext 实现了 ConfigurableApplicationContext
    //3. ClassPathXmlApplicationContext 是有close
    //4. 将ioc 转成ClassPathXmlApplicationContext,再调用close
    }
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