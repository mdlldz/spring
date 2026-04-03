import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.lang.reflect.Method;
import java.util.*;

public class ApplicationContext {
    private Map<String, Object> singletonObjects = new HashMap<>();

    public ApplicationContext(String xmlFile) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(getClass().getClassLoader().getResourceAsStream(xmlFile));
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();

            for (Element el : elements) {
                if (el.getName().equals("bean")) {
                    createBean(el);
                } else if (el.getName().equals("list") && el.getNamespacePrefix().equals("util")) {
                    createUtilList(el);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUtilList(Element el) throws Exception {
        String id = el.attributeValue("id");
        List<Object> list = new ArrayList<>();
        for (Element valueEl : el.elements("value")) {
            list.add(valueEl.getText());
        }
        singletonObjects.put(id, list);
    }

    private Object createBean(Element beanElement) throws Exception {
        String id = beanElement.attributeValue("id");
        String className = beanElement.attributeValue("class");
        Class<?> clazz = Class.forName(className);
        Object bean = clazz.newInstance();

        for (Element prop : beanElement.elements("property")) {
            String name = prop.attributeValue("name");
            Method setter = findSetter(clazz, name);
            Class<?> paramType = setter.getParameterTypes()[0];
            Object value = parseValue(prop, paramType);
            setter.invoke(bean, value);
        }

        if (id != null) {
            singletonObjects.put(id, bean);
        }
        return bean;
    }

    private Object parseValue(Element prop, Class<?> targetType) throws Exception {
        String ref = prop.attributeValue("ref");
        if (ref != null) return getBean(ref);

        if (prop.element("list") != null) return parseList(prop.element("list"));
        if (prop.element("map") != null) return parseMap(prop.element("map"));
        if (prop.element("set") != null) return parseSet(prop.element("set"));
        if (prop.element("array") != null) return parseArray(prop.element("array"), targetType);
        if (prop.element("props") != null) return parseProps(prop.element("props"));
        if (prop.element("bean") != null) return createBean(prop.element("bean"));

        String val = prop.attributeValue("value");
        if (val != null) return convert(val, targetType);
        return null;
    }

    private List<Object> parseList(Element el) throws Exception {
        List<Object> list = new ArrayList<>();
        for (Element e : el.elements()) {
            if (e.getName().equals("ref")) list.add(getBean(e.attributeValue("bean")));
            else if (e.getName().equals("bean")) list.add(createBean(e));
            else if (e.getName().equals("value")) list.add(e.getText());
        }
        return list;
    }

    private Map<Object, Object> parseMap(Element el) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        for (Element entry : el.elements("entry")) {
            Object key = null;
            Element keyEl = entry.element("key");
            if (keyEl != null) {
                Element v = keyEl.element("value");
                key = v != null ? v.getText() : keyEl.getText();
            } else {
                key = entry.attributeValue("key");
            }

            Object value = null;
            Element refEl = entry.element("ref");
            if (refEl != null) {
                value = getBean(refEl.attributeValue("bean"));
            } else {
                String vr = entry.attributeValue("value-ref");
                String vv = entry.attributeValue("value");
                if (vr != null) value = getBean(vr);
                else if (vv != null) value = vv;
                else if (entry.element("bean") != null) value = createBean(entry.element("bean"));
            }
            map.put(key, value);
        }
        return map;
    }

    private Set<Object> parseSet(Element el) throws Exception {
        Set<Object> set = new HashSet<>();
        for (Element e : el.elements()) {
            if (e.getName().equals("ref")) set.add(getBean(e.attributeValue("bean")));
            else if (e.getName().equals("bean")) set.add(createBean(e));
            else if (e.getName().equals("value")) set.add(e.getText());
        }
        return set;
    }

    private Object parseArray(Element el, Class<?> targetType) throws Exception {
        List<Object> list = new ArrayList<>();
        for (Element e : el.elements()) {
            if (e.getName().equals("ref")) list.add(getBean(e.attributeValue("bean")));
            else if (e.getName().equals("bean")) list.add(createBean(e));
            else if (e.getName().equals("value")) list.add(e.getText());
        }

        Class<?> componentType = targetType.getComponentType();
        Object array = java.lang.reflect.Array.newInstance(componentType, list.size());
        for (int i = 0; i < list.size(); i++) {
            java.lang.reflect.Array.set(array, i, list.get(i));
        }
        return array;
    }

    private Properties parseProps(Element propsEl) {
        Properties p = new Properties();
        for (Element prop : propsEl.elements("prop")) {
            p.setProperty(prop.attributeValue("key"), prop.getText());
        }
        return p;
    }

    private Object convert(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == long.class || type == Long.class) return Long.parseLong(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        return value;
    }

    private Method findSetter(Class<?> clazz, String fieldName) {
        String setter = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(setter) && m.getParameterCount() == 1) {
                return m;
            }
        }
        throw new RuntimeException("找不到set方法：" + setter);
    }

    public Object getBean(String id) {
        return singletonObjects.get(id);
    }

    public <T> T getBean(String id, Class<T> clazz) {
        Object bean = singletonObjects.get(id);
        if (bean == null) throw new RuntimeException("bean不存在: " + id);
        return clazz.cast(bean);
    }
}