package proxy.proxy2;

public class Ship implements Vehicle{
    @Override
    public void run() {
        System.out.println("交通工具开始运行");
        System.out.println("轮船在水上running");
        System.out.println("轮船停止运行");
    }
}
