import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
    // * 什么时候被调用：在Bean的init方法前被调用
    // * @param bean 传入的在IOC容器中创建/配置Bean
    // * @param beanName 传入的在IOC容器中创建/配置Bean的id
    // * @return Object 程序员对传入的bean 进行修改/处理，返回
    // * @throws BeansException
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization()..bean =" + bean + "beanName =" + beanName);
        if(bean instanceof House){
            ((House)bean).setName("上海豪宅");
        }
        return bean;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
