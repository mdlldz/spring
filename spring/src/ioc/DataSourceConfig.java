package ioc;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Component
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/test?useSSL=false");
        ds.setUser("root");
        ds.setPassword("123456");
        return ds;
    }
}