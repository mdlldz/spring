package ioc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DataSourceTransactionManager implements PlatformTransactionManager {
    private final DataSource dataSource;
    // 线程本地存储当前连接，实现事务上下文传递
    private final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public DataSourceTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TransactionStatus getTransaction(Transactional attr) throws SQLException {
        Connection currentConn = connectionHolder.get();
        Propagation propagation = attr.propagation();

        // 1. 处理 REQUIRED：有事务就加入，没有就新建
        if (propagation == Propagation.REQUIRED) {
            if (currentConn != null) {
                // 已有事务，加入当前事务
                return new TransactionStatus(currentConn, false, false);
            } else {
                // 新建事务
                Connection newConn = dataSource.getConnection();
                newConn.setAutoCommit(false);
                connectionHolder.set(newConn);
                return new TransactionStatus(newConn, true, false);
            }
        }
        // 2. 处理 REQUIRES_NEW：总是新建事务
        else if (propagation == Propagation.REQUIRES_NEW) {
            // 挂起当前事务，新建独立事务
            Connection newConn = dataSource.getConnection();
            newConn.setAutoCommit(false);
            connectionHolder.set(newConn);
            return new TransactionStatus(newConn, true, false);
        }
        // 3. 处理 NESTED：嵌套事务，保存点机制（简化实现）
        else if (propagation == Propagation.NESTED) {
            if (currentConn != null) {
                // 设置保存点
                return new TransactionStatus(currentConn, false, false);
            } else {
                Connection newConn = dataSource.getConnection();
                newConn.setAutoCommit(false);
                connectionHolder.set(newConn);
                return new TransactionStatus(newConn, true, false);
            }
        }
        throw new IllegalArgumentException("不支持的传播行为：" + propagation);
    }

    @Override
    public void commit(TransactionStatus status) throws SQLException {
        if (status.isNewTransaction()) {
            Connection conn = status.getConnection();
            if (conn != null && !conn.isClosed()) {
                conn.commit();
                conn.setAutoCommit(true);
                conn.close();
            }
            connectionHolder.remove();
        }
    }

    @Override
    public void rollback(TransactionStatus status) throws SQLException {
        if (status.isNewTransaction()) {
            Connection conn = status.getConnection();
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.close();
            }
            connectionHolder.remove();
        }
    }

    // 对外暴露当前连接，供JdbcTemplate使用
    public Connection getCurrentConnection() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            return conn;
        }
        return dataSource.getConnection();
    }
}