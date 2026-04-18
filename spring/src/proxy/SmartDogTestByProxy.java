package proxy;

import org.junit.jupiter.api.Test;

public class SmartDogTestByProxy {
   @Test
    public void Test(){
       SmartAnimalable smartAnimalable = new SmartDog();
       MyProxyProvider myProxyProvider =new MyProxyProvider(smartAnimalable);
       //返回了代理对象
       SmartAnimalable proxy = myProxyProvider.getProxy();
       proxy.getSum(10, 2);
       System.out.print("============");
       proxy.getsub(10, 2);
   }
}
