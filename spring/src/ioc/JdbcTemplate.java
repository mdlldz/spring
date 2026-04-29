import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class JdbcTemplate {
    @Autowired
    private DataSourceTransactionManager transactionManager;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false";
    private static final String USER = "root";
    private static final String PWD = "123456";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 修改这里，优先获取事务连接
    private Connection getConn() throws SQLException{
        if (transactionManager != null) {
            return transactionManager.getCurrentConnection();
        }
        return DriverManager.getConnection(URL,USER,PWD);
    }

    // 你原有的 update/query 方法 完全不动
    public int update(String sql,Object...params){
        try (Connection conn = getConn();
             PreparedStatement pstm = conn.prepareStatement(sql)){
            for (int i = 0; i < params.length; i++) {
                pstm.setObject(i+1,params[i]);
            }
            return pstm.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper,Object...params){
        List<T> list = new ArrayList<>();
        try (Connection conn = getConn();
             PreparedStatement pstm = conn.prepareStatement(sql)){
            for (int i = 0; i < params.length; i++) {
                pstm.setObject(i+1,params[i]);
            }
            ResultSet rs = pstm.executeQuery();
            while (rs.next()){
                list.add(rowMapper.getRow(rs));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }
}