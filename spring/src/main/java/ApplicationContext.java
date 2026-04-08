import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简易IOC容器实现
 * 支持XML解析、属性注入、占位符解析、初始化与销毁方法
 */
public class ApplicationContext {

    /**
     * 单例Bean容器，存储Bean名称与实例的映射关系
     */
    private final Map<String, Object> singletonObjects = new HashMap<>();

    /**
     * 销毁方法钩子集合，容器关闭时执行所有Bean的销毁方法
     */
    private final List<Runnable> destroyHooks = new ArrayList<>();

    /**
     * 配置属性集合，存储从properties文件加载的配置项
     */
    private final Properties properties = new Properties();

    /**
     * 正则表达式，匹配${key}格式的占位符
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * 构造方法，初始化IOC容器
     * @param xmlFile 配置文件路径
     */
    public ApplicationContext(String xmlFile) {
        try {
            // 读取XML配置文件
            SAXReader reader = new SAXReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream(xmlFile);
            if (is == null) {
                throw new RuntimeException("XML文件不存在: " + xmlFile);
            }
            Document doc = reader.read(is);
            Element root = doc.getRootElement();

            // 加载外部配置文件
            loadPropertyPlaceholders(root);

            // 实例化所有Bean
            for (Element beanEl : root.elements("bean")) {
                createBean(beanEl);
            }

            // 注册JVM关闭钩子，容器关闭时执行销毁方法
            Runtime.getRuntime().addShutdownHook(new Thread(this::close));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("IOC容器初始化失败", e);
        }
    }

    /**
     * 解析配置文件中的property-placeholder标签，加载外部properties文件
     * @param root XML根节点
     */
    private void loadPropertyPlaceholders(Element root) {
        for (Element el : root.elements()) {
            String name = el.getQName().getName();
            if ("property-placeholder".equalsIgnoreCase(name) || "context:property-placeholder".equalsIgnoreCase(name)) {
                String location = el.attributeValue("location");
                if (location != null && !location.trim().isEmpty()) {
                    for (String loc : location.trim().split("\\s*,\\s*")) {
                        loadProperties(loc.trim());
                    }
                }
            }
        }
    }

    /**
     * 加载指定路径的properties配置文件
     * @param location 配置文件路径
     */
    private void loadProperties(String location) {
        if (location.startsWith("classpath:")) {
            location = location.substring("classpath:".length());
        }

        InputStream is = getClass().getClassLoader().getResourceAsStream(location);
        if (is == null) {
            System.err.println("警告：未找到配置文件 " + location);
            return;
        }
        try {
            properties.load(is);
            System.out.println("成功加载配置：" + location);
        } catch (Exception e) {
            throw new RuntimeException("加载配置失败", e);
        }
    }

    /**
     * 解析字符串中的${key}占位符
     * @param value 包含占位符的字符串
     * @return 解析后的字符串
     */
    private String resolvePlaceholder(String value) {
        if (value == null) {
            return null;
        }
        Matcher m = PLACEHOLDER_PATTERN.matcher(value);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(1).trim();
            String propValue = properties.getProperty(key);
            if (propValue == null) {
                m.appendReplacement(sb, Matcher.quoteReplacement(m.group(0)));
            } else {
                String resolved = resolvePlaceholder(propValue);
                m.appendReplacement(sb, Matcher.quoteReplacement(resolved));
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 根据XML配置创建Bean实例，完成属性注入、初始化方法调用
     * @param beanEl bean对应的XML节点
     * @throws Exception 创建过程可能抛出的异常
     */
    private void createBean(Element beanEl) throws Exception {
        String id = beanEl.attributeValue("id");
        String className = beanEl.attributeValue("class");

        // ==============================
        // 【关键修复】class 属性为空，直接跳过，不抛异常
        // ==============================
        if (className == null || className.trim().isEmpty()) {
            System.err.println("警告：Bean " + id + " 未配置class属性，已跳过");
            return;
        }

        String initMethod = beanEl.attributeValue("init-method");
        String destroyMethod = beanEl.attributeValue("destroy-method");

        Class<?> clazz = Class.forName(className);
        Object bean = clazz.getDeclaredConstructor().newInstance();

        // 处理属性注入
        for (Element prop : beanEl.elements("property")) {
            String name = prop.attributeValue("name");
            String rawValue = prop.attributeValue("value");
            if (name == null || rawValue == null) {
                continue;
            }

            String resolvedValue = resolvePlaceholder(rawValue);
            Method setter = findSetter(clazz, name);
            if (setter == null) {
                continue;
            }

            Object converted = convert(resolvedValue, setter.getParameterTypes()[0]);
            try {
                setter.invoke(bean, converted);
            } catch (Exception ignored) {
            }
        }

        // 执行初始化方法
        if (initMethod != null && !initMethod.isEmpty()) {
            try {
                Method m = clazz.getDeclaredMethod(initMethod);
                m.invoke(bean);
            } catch (NoSuchMethodException ignored) {
            }
        }

        // 注册销毁方法
        if (destroyMethod != null && !destroyMethod.isEmpty()) {
            try {
                Method m = clazz.getDeclaredMethod(destroyMethod);
                final Object target = bean;
                destroyHooks.add(() -> {
                    try {
                        m.invoke(target);
                    } catch (Exception ignored) {
                    }
                });
            } catch (NoSuchMethodException ignored) {
            }
        }

        singletonObjects.put(id, bean);
    }

    /**
     * 根据属性名称查找对应的setter方法
     * @param clazz 目标类
     * @param propertyName 属性名称
     * @return 对应的setter方法，未找到返回null
     */
    private Method findSetter(Class<?> clazz, String propertyName) {
        String setterName = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(setterName) && m.getParameterCount() == 1) {
                return m;
            }
        }
        return null;
    }

    /**
     * 将字符串转换为指定类型
     * @param value 字符串值
     * @param type 目标类型
     * @return 转换后的值
     */
    private Object convert(String value, Class<?> type) {
        try {
            if (type == int.class || type == Integer.class) {
                return Integer.parseInt(value);
            }
            if (type == long.class || type == Long.class) {
                return Long.parseLong(value);
            }
            if (type == boolean.class || type == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
            if (type == double.class || type == Double.class) {
                return Double.parseDouble(value);
            }
            if (type == float.class || type == Float.class) {
                return Float.parseFloat(value);
            }
        } catch (Exception ignored) {
        }
        return value;
    }

    /**
     * 根据Bean名称获取实例
     * @param id Bean名称
     * @return Bean实例
     */
    public Object getBean(String id) {
        return singletonObjects.get(id);
    }

    /**
     * 根据Bean名称和类型获取实例
     * @param id Bean名称
     * @param clazz 目标类型
     * @return 类型转换后的Bean实例
     */
    public <T> T getBean(String id, Class<T> clazz) {
        return clazz.cast(getBean(id));
    }

    /**
     * 关闭容器，执行所有Bean的销毁方法，清空容器
     */
    public void close() {
        for (Runnable hook : destroyHooks) {
            try {
                hook.run();
            } catch (Exception ignored) {
            }
        }
        singletonObjects.clear();
    }
}