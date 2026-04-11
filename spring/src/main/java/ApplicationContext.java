import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationContext {

    private final Map<String, Object> singletonObjects = new HashMap<>();
    private final List<Runnable> destroyHooks = new ArrayList<>();
    private final Properties properties = new Properties();
    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([^}]+)}");
    private static final Pattern SPEL = Pattern.compile("#\\{([^}]+)}");

    public ApplicationContext(String xmlFile) {
        try {
            SAXReader reader = new SAXReader();
            InputStream is = getClass().getClassLoader().getResourceAsStream(xmlFile);
            if (is == null) throw new RuntimeException("XML 文件不存在");
            Document doc = reader.read(is);
            Element root = doc.getRootElement();

            loadPropertyPlaceholders(root);

            // 扫描注解包
            scanComponentPackages(root);

            List<Element> beanElements = root.elements("bean");
            for (Element beanEl : beanElements) createBean(beanEl);

            // 新增自动注入 @Autowired
            autowireAllBeans();

            for (Element beanEl : beanElements) autowireByType(beanEl);

            Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("IOC 初始化失败", e);
        }
    }

    // ==========================================
    //  新增：扫描 @Component 注解
    // ==========================================
    // 修复：能正确识别 context:component-scan 标签
    private void scanComponentPackages(Element root) throws Exception {
        for (Element el : root.elements()) {
            // 同时匹配带命名空间和不带的
            if ("component-scan".equals(el.getName()) || "context:component-scan".equals(el.getQualifiedName())) {
                String basePackage = el.attributeValue("base-package");
                if (basePackage != null) {
                    System.out.println("开始扫描包：" + basePackage);
                    scanPackage(basePackage);
                }
            }
        }
    }

    private void scanPackage(String basePackage) throws Exception {
        String path = basePackage.replace(".", "/");
        Enumeration<URL> urls = getClass().getClassLoader().getResources(path);

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            File dir = new File(url.toURI());
            scanClassInDir(dir, basePackage);
        }
    }

    private void scanClassInDir(File dir, String packageName) throws Exception {
        if (!dir.exists()) return;
        File[] files = dir.listFiles((f) -> f.isDirectory() || f.getName().endsWith(".class"));
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                scanClassInDir(f, packageName + "." + f.getName());
            } else {
                String className = packageName + "." + f.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);

                // 这里改成通用判断
                if (isComponent(clazz)) {
                    Object bean = clazz.getDeclaredConstructor().newInstance();
                    String id = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);
                    singletonObjects.put(id, bean);
                    System.out.println("已注入Bean：" + id + " -> " + clazz.getName());
                }
            }
        }
    }

    // 增加这个工具方法（支持全部 Spring 注解）
    private boolean isComponent(Class<?> clazz) {
        for (java.lang.annotation.Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(org.springframework.stereotype.Component.class)) {
                return true;
            }
        }
        return false;
    }

    // ==========================================
    //  新增：@Autowired 自动注入
    // ==========================================
    private void autowireAllBeans() {
        for (Object bean : singletonObjects.values()) {
            Class<?> clazz = bean.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(org.springframework.beans.factory.annotation.Autowired.class)) {
                    Class<?> fieldType = field.getType();
                    Object injectBean = getBean(fieldType);

                    field.setAccessible(true);
                    try {
                        field.set(bean, injectBean);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    // ==========================================
    // 新增SpEL + ${} 解析
    // ==========================================
    private Object resolveSpel(String expression) {
        try {
            expression = expression.trim();
            if (expression.startsWith("'") && expression.endsWith("'"))
                return expression.substring(1, expression.length() - 1);

            if (singletonObjects.containsKey(expression))
                return singletonObjects.get(expression);

            if (expression.contains(".")) {
                String[] parts = expression.split("\\.", 2);
                Object bean = singletonObjects.get(parts[0]);
                if (bean == null) return null;
                Method getter = findGetter(bean.getClass(), parts[1]);
                if (getter != null) return getter.invoke(bean);
            }

            if (expression.matches("[0-9.]+[+*/-][0-9.]+")) {
                return new javax.script.ScriptEngineManager()
                        .getEngineByName("js")
                        .eval(expression);
            }
        } catch (Exception ignored) {}
        return expression;
    }

    private String resolveValue(String value) {
        if (value == null) return null;

        Matcher m = PLACEHOLDER.matcher(value);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(1).trim();
            String val = properties.getProperty(key, m.group(0));
            m.appendReplacement(sb, Matcher.quoteReplacement(val));
        }
        m.appendTail(sb);
        value = sb.toString();

        Matcher mSpel = SPEL.matcher(value);
        StringBuffer sbSpel = new StringBuffer();
        while (mSpel.find()) {
            Object val = resolveSpel(mSpel.group(1));
            mSpel.appendReplacement(sbSpel, Matcher.quoteReplacement(String.valueOf(val)));
        }
        mSpel.appendTail(sbSpel);
        return sbSpel.toString();
    }

    // ==========================================
    // 新增自动装配 byType
    // ==========================================
    private void autowireByType(Element beanEl) {
        String id = beanEl.attributeValue("id");
        String autowire = beanEl.attributeValue("autowire");
        Object bean = singletonObjects.get(id);
        if (bean == null || !"byType".equals(autowire)) return;

        for (Method m : bean.getClass().getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterCount() == 1) {
                Class<?> paramType = m.getParameterTypes()[0];
                for (Object candidate : singletonObjects.values()) {
                    if (paramType.isAssignableFrom(candidate.getClass())) {
                        try {
                            m.invoke(bean, candidate);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    // ==========================================
    // 加载配置文件
    // ==========================================
    private void loadPropertyPlaceholders(Element root) {
        for (Element el : root.elements()) {
            if (el.getName().contains("property-placeholder")) {
                String loc = el.attributeValue("location");
                if (loc != null) {
                    try (InputStream is = getClass().getClassLoader().getResourceAsStream(loc.replace("classpath:", ""))) {
                        if (is != null) properties.load(is);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    // ==========================================
    // 创建 XML Bean
    // ==========================================
    private void createBean(Element beanEl) throws Exception {
        String id = beanEl.attributeValue("id");
        String className = beanEl.attributeValue("class");
        if (className == null || id == null) return;

        Class<?> clazz = Class.forName(className);
        Object bean = clazz.newInstance();
        singletonObjects.put(id, bean);

        for (Element prop : beanEl.elements("property")) {
            String name = prop.attributeValue("name");
            String val = prop.attributeValue("value");
            if (name == null || val == null) continue;

            String resolved = resolveValue(val);
            Method setter = findSetter(clazz, name);
            if (setter != null) {
                try {
                    Object v = convert(resolved, setter.getParameterTypes()[0]);
                    setter.invoke(bean, v);
                } catch (Exception ignored) {}
            }
        }

        invokeInit(bean, beanEl.attributeValue("init-method"));
        registerDestroy(bean, beanEl.attributeValue("destroy-method"));
    }

    // ==========================================
    // 工具方法
    // ==========================================
    private Object convert(String value, Class<?> type) {
        try {
            if (type == int.class || type == Integer.class) return Integer.parseInt(value);
            if (type == double.class || type == Double.class) return Double.parseDouble(value);
            if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        } catch (Exception ignored) {}
        return value;
    }

    private Method findSetter(Class<?> clazz, String name) {
        String s = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(s) && m.getParameterCount() == 1) return m;
        }
        return null;
    }

    private Method findGetter(Class<?> clazz, String name) {
        String s = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(s) && m.getParameterCount() == 0) return m;
        }
        return null;
    }

    private void invokeInit(Object bean, String method) {
        if (method == null) return;
        try {
            bean.getClass().getMethod(method).invoke(bean);
        } catch (Exception ignored) {}
    }

    private void registerDestroy(Object bean, String method) {
        if (method == null) return;
        try {
            Method m = bean.getClass().getMethod(method);
            destroyHooks.add(() -> {
                try { m.invoke(bean); } catch (Exception ignored) {}
            });
        } catch (Exception ignored) {}
    }

    // ==========================================
    // getBean 方法
    // ==========================================
    public <T> T getBean(String id, Class<T> clazz) {
        Object bean = singletonObjects.get(id);
        if (bean == null) throw new RuntimeException("找不到Bean：" + id);
        return clazz.cast(bean);
    }

    public <T> T getBean(Class<T> clazz) {
        for (Object bean : singletonObjects.values()) {
            if (clazz.isAssignableFrom(bean.getClass())) {
                return clazz.cast(bean);
            }
        }
        throw new RuntimeException("找不到类型为 " + clazz.getName() + " 的Bean");
    }

    public void close() {
        destroyHooks.forEach(h -> {try {h.run();}catch (Exception ignored){}});
    }
}