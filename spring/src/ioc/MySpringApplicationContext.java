package ioc;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MySpringApplicationContext {

    // 配置的包路径
    private final String basePackage;

    // Bean定义信息
    private final Map<String, Class<?>> beanDefinitionMap = new ConcurrentHashMap<>();

    // 单例池
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public MySpringApplicationContext(String basePackage) {
        this.basePackage = basePackage;
        refresh();
    }

    // 容器启动
    private void refresh() {
        // 1. 包扫描
        List<Class<?>> classList = scanPackage(basePackage);

        // 2. 构建BeanDefinition
        buildBeanDefinitions(classList);

        // 3. 创建单例Bean
        preInstantiateSingletons();
    }

    // 包扫描
    private List<Class<?>> scanPackage(String basePackage) {
        List<Class<?>> classList = new ArrayList<>();
        String path = basePackage.replace(".", "/");
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) return classList;

        File root = new File(resource.getFile());
        scanFile(root, basePackage, classList);
        return classList;
    }

    private void scanFile(File file, String packageName, List<Class<?>> classList) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (File f : files) {
                if (f.isDirectory()) {
                    scanFile(f, packageName + "." + f.getName(), classList);
                } else {
                    doScanClass(f, packageName, classList);
                }
            }
        } else {
            doScanClass(file, packageName, classList);
        }
    }

    private void doScanClass(File file, String packageName, List<Class<?>> classList) {
        if (file.getName().endsWith(".class")) {
            String className = packageName + "." + file.getName().replace(".class", "");
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Component.class)) {
                    classList.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // 构建BeanDefinition
    private void buildBeanDefinitions(List<Class<?>> classList) {
        for (Class<?> clazz : classList) {
            Component component = clazz.getAnnotation(Component.class);
            String beanName = component.value().isEmpty()
                    ? lowerFirst(clazz.getSimpleName())
                    : component.value();
            beanDefinitionMap.put(beanName, clazz);
        }
    }

    // 预实例化单例Bean
    private void preInstantiateSingletons() {
        for (String beanName : beanDefinitionMap.keySet()) {
            getBean(beanName);
        }
    }

    // 获取Bean
    public Object getBean(String beanName) {
        // 先从单例池拿
        Object bean = singletonObjects.get(beanName);
        if (bean != null) {
            return bean;
        }
        // 没有就创建
        Class<?> clazz = beanDefinitionMap.get(beanName);
        if (clazz == null) {
            throw new RuntimeException("无此Bean：" + beanName);
        }
        return createBean(beanName, clazz);
    }

    // 创建Bean（实例化 → 依赖注入）
    private Object createBean(String beanName, Class<?> clazz) {
        try {
            // 实例化
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // 依赖注入 @Autowired
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> fieldType = field.getType();
                    String fieldBeanName = lowerFirst(fieldType.getSimpleName());
                    Object dependBean = getBean(fieldBeanName);
                    field.setAccessible(true);
                    field.set(instance, dependBean);
                }
            }

            // 放入单例池
            singletonObjects.put(beanName, instance);
            return instance;

        } catch (Exception e) {
            throw new RuntimeException("创建Bean失败：" + beanName, e);
        }
    }

    private String lowerFirst(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}