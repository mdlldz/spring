package component;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

//@Controller标识该类是一个控制器Controller,通常这个类是一个Servlet
@Controller
public class UserAction {
    @Resource(type = UserService.class)
    private UserService userService ;
    public void sayOk() {
        System.out.println("UserAction sayOk()");
        System.out.println("userAction装配的userService属性=" +  userService);
        userService.hi();
    }
}
