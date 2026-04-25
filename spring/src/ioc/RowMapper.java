package ioc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    T getRow(ResultSet rs) throws SQLException;
}