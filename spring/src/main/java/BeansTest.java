import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

class BeanTest {
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