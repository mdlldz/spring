package proxy;

import org.junit.Test;

public class AopTest {
    @Test
    public void smartDogTest() {
        SmartAnimalable smartAnimalable = new SmartDog();
        smartAnimalable.getSum(10, 2);
        System.out.println("====smartAnimalable.getSub(10, 2)");
    }
}
