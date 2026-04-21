package ioc;


@Component
public class TestService {
    public void hello() {
        System.out.println("TestService hello() 执行");
    }
}