import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.lang.reflect.Method;
import java.util.*;

public class ApplicationContext {
    private final Map<String, Object> singletonObjects = new HashMap<>();
    private final Map<String, Element> beanDefinitions = new HashMap<>();

    public ApplicationContext(String xmlFile) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(getClass().getClassLoader().getResourceAsStream(xmlFile));
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();

            for (Element el : elements) {
                if ("list".equals(el.getName()) && "util".equals(el.getNamespacePrefix())) {
                    parseUtilList(el);
                } else if ("bean".equals(el.getName())) {
                    String id = el.attributeValue("id");
                    if (id != null) {
                        beanDefinitions.put(id, el);
                    }
                }
            }

            for (Element el : elements) {
                if ("bean".equals(el.getName())) {
                    String abs = el.attributeValue("abstract");
                    String parent = el.attributeValue("parent");
                    if (!"true".equals(abs) && parent == null) {
                        try {
                            createBean(el);
                        } catch (Exception ignored) {}
                    }
                }
            }

            for (Element el : elements) {
                if ("bean".equals(el.getName())) {
                    String abs = el.attributeValue("abstract");
                    String parent = el.attributeValue("parent");
                    if (!"true".equals(abs) && parent != null) {
                        try {
                            createBean(el);
                        } catch (Exception ignored) {}
                    }
                }
            }

        } catch (Exception ignored) {}
    }

    private Object createBean(Element beanEl) throws Exception {
        String id = beanEl.attributeValue("id");
        String className = beanEl.attributeValue("class");
        String factoryMethod = beanEl.attributeValue("factory-method");
        String factoryBean = beanEl.attributeValue("factory-bean");
        String parent = beanEl.attributeValue("parent");
        String abs = beanEl.attributeValue("abstract");
        String scope = beanEl.attributeValue("scope");

        if ("true".equals(abs)) return null;

        Class<?> clazz = Class.forName(className);
        Object bean;

        if (factoryBean != null && factoryMethod != null) {
            Object factory = getBean(factoryBean);
            List<Object> args = new ArrayList<>();
            for (Element arg : beanEl.elements("constructor-arg")) {
                args.add(arg.attributeValue("value"));
            }
            Method method = findMethod(factory.getClass(), factoryMethod, args.size());
            bean = method.invoke(factory, args.toArray());
        } else if (factoryMethod != null) {
            List<Object> args = new ArrayList<>();
            for (Element arg : beanEl.elements("constructor-arg")) {
                args.add(arg.attributeValue("value"));
            }
            Method method = findStaticMethod(clazz, factoryMethod, args.size());
            bean = method.invoke(null, args.toArray());
        } else {
            bean = clazz.newInstance();
            List<Element> props = new ArrayList<>();
            Element current = beanEl;
            while (current != null) {
                props.addAll(0, current.elements("property"));
                String p = current.attributeValue("parent");
                current = p != null ? beanDefinitions.get(p) : null;
            }

            for (Element prop : props) {
                String name = prop.attributeValue("name");
                Method setter = findSetter(clazz, name);
                if (setter == null) continue;
                Class<?> type = setter.getParameterTypes()[0];
                Object value = parseValue(prop, type);
                setter.invoke(bean, value);
            }
        }

        if (id != null) {
            if (bean instanceof org.springframework.beans.factory.FactoryBean) {
                org.springframework.beans.factory.FactoryBean fb = (org.springframework.beans.factory.FactoryBean) bean;
                Object target = fb.getObject();
                singletonObjects.put(id, target);
                singletonObjects.put("&" + id, bean);
            } else {
                if (!"prototype".equals(scope)) {
                    singletonObjects.put(id, bean);
                }
            }
        }
        return bean;
    }

    private Object parseValue(Element prop, Class<?> type) throws Exception {
        String ref = prop.attributeValue("ref");
        if (ref != null) return getBean(ref);

        if (prop.element("list") != null) return parseList(prop.element("list"));
        if (prop.element("map") != null) return parseMap(prop.element("map"));
        if (prop.element("set") != null) return parseSet(prop.element("set"));
        if (prop.element("array") != null) return parseArray(prop.element("array"), type);
        if (prop.element("props") != null) return parseProps(prop.element("props"));
        if (prop.element("bean") != null) return createBean(prop.element("bean"));

        String val = prop.attributeValue("value");
        return convert(val, type);
    }

    private List<Object> parseList(Element el) throws Exception {
        List<Object> list = new ArrayList<>();
        for (Element e : el.elements()) {
            if ("ref".equals(e.getName())) list.add(getBean(e.attributeValue("bean")));
            else if ("bean".equals(e.getName())) list.add(createBean(e));
            else if ("value".equals(e.getName())) list.add(e.getText());
        }
        return list;
    }

    private Map<Object, Object> parseMap(Element el) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        for (Element entry : el.elements("entry")) {
            Element keyEl = entry.element("key");
            Object key = keyEl.element("value").getText();
            Element refEl = entry.element("ref");
            Object value = getBean(refEl.attributeValue("bean"));
            map.put(key, value);
        }
        return map;
    }

    private Set<Object> parseSet(Element el) throws Exception {
        Set<Object> set = new HashSet<>();
        for (Element e : el.elements()) {
            if ("ref".equals(e.getName())) set.add(getBean(e.attributeValue("bean")));
            else if ("bean".equals(e.getName())) set.add(createBean(e));
            else if ("value".equals(e.getName())) set.add(e.getText());
        }
        return set;
    }

    private Object parseArray(Element el, Class<?> type) {
        List<String> list = new ArrayList<>();
        for (Element v : el.elements("value")) list.add(v.getText());
        return list.toArray(new String[0]);
    }

    private Properties parseProps(Element el) {
        Properties p = new Properties();
        for (Element prop : el.elements("prop")) {
            p.setProperty(prop.attributeValue("key"), prop.getText());
        }
        return p;
    }

    private void parseUtilList(Element el) {
        String id = el.attributeValue("id");
        List<String> list = new ArrayList<>();
        for (Element v : el.elements("value")) list.add(v.getText());
        singletonObjects.put(id, list);
    }

    private Object convert(String v, Class<?> type) {
        if (type == int.class || type == Integer.class) return Integer.parseInt(v);
        if (type == long.class || type == Long.class) return Long.parseLong(v);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(v);
        return v;
    }

    private Method findSetter(Class<?> clazz, String name) {
        String setter = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(setter) && m.getParameterCount() == 1) return m;
        }
        return null;
    }

    private Method findStaticMethod(Class<?> clazz, String name, int count) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == count) return m;
        }
        return null;
    }

    private Method findMethod(Class<?> clazz, String name, int count) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == count) return m;
        }
        return null;
    }

    public Object getBean(String id) {
        Element beanEl = beanDefinitions.get(id);
        if (beanEl == null) return null;
        String scope = beanEl.attributeValue("scope");

        if ("prototype".equals(scope)) {
            try {
                return createBean(beanEl);
            } catch (Exception e) {
                return null;
            }
        }
        return singletonObjects.get(id);
    }

    public <T> T getBean(String id, Class<T> clazz) {
        return clazz.cast(getBean(id));
    }
}