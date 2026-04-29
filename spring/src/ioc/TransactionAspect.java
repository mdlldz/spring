package ioc;

import java.lang.reflect.Method;
import java.sql.SQLException;

@Component
@Aspect
public class TransactionAspect {
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Before("*")
    public void beforeMethod(Method method, Object[] args) throws Exception {
        // 检查方法是否有@Transactional注解
        Transactional attr = getTransactionalAttr(method);
        if (attr == null) {
            return;
        }
        // 开启事务
        TransactionStatus status = transactionManager.getTransaction(attr);
        // 把事务状态存入线程本地，方便后置处理
        TransactionContextHolder.set(status);
    }

    @After("*")
    public void afterMethod(Method method, Object[] args, Object result) throws Exception {
        TransactionStatus status = TransactionContextHolder.get();
        if (status == null) {
            return;
        }
        // 提交事务
        transactionManager.commit(status);
        TransactionContextHolder.clear();
    }

    @AfterThrowing(value = "*", throwing = "ex")
    public void afterThrowing(Method method, Object[] args, Exception ex) throws Exception {
        Transactional attr = getTransactionalAttr(method);
        if (attr == null) {
            return;
        }
        // 判断是否需要回滚
        boolean needRollback = false;
        for (Class<? extends Throwable> rollbackEx : attr.rollbackFor()) {
            if (rollbackEx.isInstance(ex)) {
                needRollback = true;
                break;
            }
        }
        for (Class<? extends Throwable> noRollbackEx : attr.noRollbackFor()) {
            if (noRollbackEx.isInstance(ex)) {
                needRollback = false;
                break;
            }
        }
        if (needRollback) {
            TransactionStatus status = TransactionContextHolder.get();
            transactionManager.rollback(status);
        }
        TransactionContextHolder.clear();
    }

    // 获取方法上的@Transactional注解，优先方法上的，其次类上的
    private Transactional getTransactionalAttr(Method method) {
        if (method.isAnnotationPresent(Transactional.class)) {
            return method.getAnnotation(Transactional.class);
        }
        Class<?> clazz = method.getDeclaringClass();
        if (clazz.isAnnotationPresent(Transactional.class)) {
            return clazz.getAnnotation(Transactional.class);
        }
        return null;
    }
}