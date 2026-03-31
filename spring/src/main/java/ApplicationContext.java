import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private Map<String, Object> singletonObjects = new HashMap<>();
    private List<Element> beanElements;

    public ApplicationContext(String iocBeanXmlFile) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(iocBeanXmlFile));
            beanElements = document.getRootElement().elements("bean");

            for (Element beanElement : beanElements) {
                createBean(beanElement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBean(Element beanElement) throws Exception {
        String id = beanElement.attributeValue("id");
        String className = beanElement.attributeValue("class");
        Class<?> clazz = Class.forName(className);
        Object bean = clazz.newInstance();

        List<Element> properties = beanElement.elements("property");
        for (Element prop : properties) {
            String name = prop.attributeValue("name");
            String value = prop.attributeValue("value");
            String ref = prop.attributeValue("ref");

            Method setter = findSetter(clazz, name);
            Class<?> paramType = setter.getParameterTypes()[0];

            if (ref != null) {
                Object refBean = getBean(ref);
                setter.invoke(bean, refBean);
            } else {
                if (paramType == Integer.class || paramType == int.class) {
                    setter.invoke(bean, Integer.valueOf(value));
                } else {
                    setter.invoke(bean, value);
                }
            }
        }

        if (id != null) {
            singletonObjects.put(id, bean);
        }
    }

    private Method findSetter(Class<?> clazz, String fieldName) throws NoSuchMethodException {
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        throw new NoSuchMethodException(clazz.getName() + "." + setterName);
    }

    public Object getBean(String id) {
        return singletonObjects.get(id);
    }

    public <T> T getBean(Class<T> requiredType) {
        for (Object bean : singletonObjects.values()) {
            if (requiredType.isInstance(bean)) {
                return requiredType.cast(bean);
            }
        }
        throw new RuntimeException();
    }

    public <T> T getBean(String id, Class<T> requiredType) {
        Object bean = singletonObjects.get(id);
        if (bean == null) {
            throw new RuntimeException();
        }
        return requiredType.cast(bean);
    }
}