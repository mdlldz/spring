package annotation;

import component.MyComponent;
import component.UserAction;
import component.UserDao;
import component.UserService;

public class MdlSpringApplicationContextTest {
    public static void main(String[] args){
        try {
            MdlSpringApplicationContext ioc = new MdlSpringApplicationContext(MdlSpringConfig.class);
            UserAction userAction = (UserAction)ioc.getBean("UserAction");
            System.out.println("userAction=" + userAction);
            System.out.println("ok");
            MyComponent myComponent = (MyComponent) ioc.getBean("MyComponent");
            System.out.println("myComponent" + myComponent);
            UserService userService = (UserService) ioc.getBean("UserService");
            System.out.println("userService=" + userService);
            UserDao userDao = (UserDao) ioc.getBean("UserDao");
            System.out.println("userDao=" + userDao);
            System.out.println("ok");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
