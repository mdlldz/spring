package proxy2;

import org.junit.jupiter.api.Test;

public class TestVehicle {
    @Test
    public void run(){
        proxy2.Vehicle car = new Car();
        car.run();
        proxy2.Vehicle ship = new Ship();
        ship.run();
    }
    @Test
    public void proxyRun(){
        Vehicle vehicle = new Ship();
        VehicleProxyProvider vehicleProxyProvider =
                new VehicleProxyProvider(vehicle);
        vehicle.run();
        //1.porxy编译类型Vehicle
        // 2，运行类型是代理类型
        Vehicle proxy = vehicleProxyProvider.getProxy();
        System.out.println("proxy的编译类型是 Vehicle");
        System.out.println("proxy的运行类型是" + proxy.getClass());
        //proxy的编译类型是 Vehicle,运行类型是 class com.sun.proxy.$Proxy9
        // 所以当执行run方法时，会执行到 代理对象的invoke
        System.out.println("ok");
        proxy.run();
    }
}
