package proxy1;

public class Dog implements SmartAnimalable {

    @Override
    public int getSum(int a, int b) {
        System.out.println("Dog getSum 执行：" + a + "+" + b);
        return a + b;
    }

    @Override
    public int getSub(int a, int b) {
        System.out.println("Dog getSub 执行：" + a + "-" + b);
        return a - b;
    }
}