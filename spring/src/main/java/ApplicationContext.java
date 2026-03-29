import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public ApplicationContext(String iocBeanXmlFile) {
        try {
            String path = this.getClass().getResource("/").getPath();
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(path + iocBeanXmlFile);
            Element root = document.getRootElement();
            List<Element> beanList = root.elements("bean");

            for (Element bean : beanList) {
                String id = bean.attributeValue("id");
                String classFullPath = bean.attributeValue("class");
                List<Element> propertyList = bean.elements("property");

                String monsterId = propertyList.get(0).attributeValue("value");
                String name = propertyList.get(1).attributeValue("value");
                String skill = propertyList.get(2).attributeValue("value");
                Class<?> aClass = Class.forName(classFullPath);
               Monster o = (Monster) aClass.newInstance();
                o.setName(monsterId);
                o.setName(name);
                o.setSkill(skill);
                singletonObjects.put(id,o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object getBean(String id){
        return singletonObjects.get(id);
    }
}