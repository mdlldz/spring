package ioc;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser() {
        // 正常插入
        jdbcTemplate.update("insert into user(name) values(?)", "test");
        // 模拟异常，触发回滚
        throw new RuntimeException("测试事务回滚");
    }
}