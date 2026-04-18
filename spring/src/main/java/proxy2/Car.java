package proxy2;

public class Car implements Vehicle{
    @Override
    public void run() {
        System.out.println("交通工具开始运行");
        System.out.println("汽车在路上running");
        System.out.println("汽车停止运行");
    }
}
