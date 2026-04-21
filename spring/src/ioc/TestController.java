package ioc;

@Component
public class TestController {

    @Autowired
    private TestService testService;

    public void test() {
        System.out.println("TestController 调用 testService");
        testService.hello();
    }
}
