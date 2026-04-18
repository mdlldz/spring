package proxy3;

import org.springframework.stereotype.Component;

@Component
public class Car implements Vehicle {
    @Override
    public void run() {
        System.out.println("小汽车运行中...");
    }

    @Override
    public String fly(int height) {
        System.out.println("小汽车飞行，高度：" + height);
        return "飞行成功";
    }
}
