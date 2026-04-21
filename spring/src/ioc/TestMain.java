package ioc;
public class TestMain {
    public static void main(String[] args) {
        MySpringApplicationContext context
                = new MySpringApplicationContext("ioc");

        TestController controller
                = (TestController) context.getBean("testController");

        controller.test();
    }
}