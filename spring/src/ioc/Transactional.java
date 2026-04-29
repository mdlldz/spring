package ioc;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
    // 事务传播行为
    Propagation propagation() default Propagation.REQUIRED;
    // 事务隔离级别
    Isolation isolation() default Isolation.DEFAULT;
    // 超时时间（秒）
    int timeout() default -1;
    // 是否只读
    boolean readOnly() default false;
    // 哪些异常回滚
    Class<? extends Throwable>[] rollbackFor() default {Exception.class};
    // 哪些异常不回滚
    Class<? extends Throwable>[] noRollbackFor() default {};
}