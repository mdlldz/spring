import java.util.HashMap;
import java.util.Map;

public class MyStaticFactory {
    private static Map<String,Monster> monsterMap;
    //使用static代码块进行优化
    static {
         monsterMap  = new HashMap<>();
         monsterMap.put("monster01",new Monster(100,"牛魔王","芭蕉扇"));
         monsterMap.put("monster02",new Monster(100,"狐狸精","美人计"));
     }
     public static Monster getMonster(String key){
        return monsterMap.get(key);
     }

}
