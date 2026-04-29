package ioc;

import java.sql.Connection;
import java.sql.SQLException;

public interface PlatformTransactionManager {
    // 获取当前事务
    TransactionStatus getTransaction(Transactional attr) throws SQLException;
    // 提交事务
    void commit(TransactionStatus status) throws SQLException;
    // 回滚事务
    void rollback(TransactionStatus status) throws SQLException;
}