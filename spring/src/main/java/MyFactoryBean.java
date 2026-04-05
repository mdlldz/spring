import org.springframework.beans.factory.FactoryBean;
import java.util.HashMap;
import java.util.Map;

public class MyFactoryBean implements FactoryBean<Monster> {
    private String key;
    private Map<String, Monster> monster_map;

    {
        monster_map = new HashMap<>();
        monster_map.put("monster01", new Monster(300, "牛魔王_", "芭蕉扇"));
        monster_map.put("monster02", new Monster(400, "狐狸精_", "美人计"));
        monster_map.put("monster04", new Monster(600, "白骨精_", "离间计"));
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Class<Monster> getObjectType() {
        return Monster.class;
    }

    @Override
    public Monster getObject() throws Exception {
        return monster_map.get(key);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}