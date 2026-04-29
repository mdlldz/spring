package ioc;

import java.sql.Connection;

public class TransactionStatus {
    private final Connection connection;
    private final boolean isNewTransaction;
    private final boolean isRollbackOnly;

    public TransactionStatus(Connection connection, boolean isNewTransaction, boolean isRollbackOnly) {
        this.connection = connection;
        this.isNewTransaction = isNewTransaction;
        this.isRollbackOnly = isRollbackOnly;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isNewTransaction() {
        return isNewTransaction;
    }

    public boolean isRollbackOnly() {
        return isRollbackOnly;
    }
}