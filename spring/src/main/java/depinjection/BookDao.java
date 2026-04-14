package depinjection;

public class BookDao extends BaseDao<Book>{
    @Override
    public void save() {
        System.out.println("BookDao的save()...");
    }
}
