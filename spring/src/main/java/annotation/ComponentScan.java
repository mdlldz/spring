package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//1.@Target(ElementType.TYPE)指定我们的ComponentScan注解可以修饰 Type程序元素
//2.@Retention(RetentionPolicy.RUNTIME) 指定ComponentScan注解 保留范围
//3.String value() default "";表示ComponentScan可以传入value
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {
    String value() default "";
}
