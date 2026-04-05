import java.util.HashMap;
import java.util.Map;

public class MyInstanceFactory {
    private Map<String,Monster> monster_map;
    //通过普通代码块进行初始化
    {
        monster_map = new HashMap<>();
        monster_map.put("monster01",new Monster(300,"牛魔王_","芭蕉扇"));
        monster_map.put("monster02",new Monster(400,"狐狸精_","美人计"));
    }
    public Monster getMonster(String key){
        return monster_map.get(key);
    }
}
