package ioc;

public interface BeanPostProcessor {
    Object postProcessBefore(Object bean, String beanName);
    Object postProcessAfter(Object bean, String beanName);
}