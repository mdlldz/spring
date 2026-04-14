package annotation;

import org.dom4j.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

//类似Spring原生ioc容器
public class MdlSpringApplicationContext {
    private Class configClass;
    //ioc存放通过放射创建的对象(基于注解方式)
    private ConcurrentHashMap<String,Object> ioc =  new ConcurrentHashMap<>();

    public MdlSpringApplicationContext(Class configClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.configClass = configClass;
        System.out.println("this.configClass=" + this.configClass);

        //1.先得到MdlSpringConfig配置的@ComponentScan(value = "")
        ComponentScan componentScan =
                (ComponentScan)this.configClass.getAnnotation(ComponentScan.class);

        //2.通过componentScan的value得到要扫描的包
        String path = componentScan.value();
        System.out.println("要扫描的包=" + path );

        //得到要扫描的包下的所有资源(类.class)
        //1.得到类的加载器
        ClassLoader classLoader = MdlSpringApplicationContext.class.getClassLoader();

        //2.通过类的加载器得到要扫描的包的资源 url，类似一个路径
        path = path.replace(".","/");
        URL resource = classLoader.getResource(path);
        System.out.println("resource=" + resource);

        //3.将要加载的资源（.class）路径下的文件进行遍历
        if (resource != null) {
            File file = new File(resource.getFile());
            System.out.println("目标文件绝对路径：" + file.getAbsolutePath());

            if(file.isDirectory()){
                File[] files = file.listFiles();

                if (files != null) {
                    System.out.println("扫描到的文件数量：" + files.length);

                    for(File f : files){
                        System.out.println("===============");
                        System.out.println(f.getAbsolutePath());
                        System.out.println("文件名：" + f.getName());

                        String fileAbsolutepath = f.getAbsolutePath();

                        if(fileAbsolutepath.endsWith(".class")) {
                            //1.找到类名
                            String className =
                                    fileAbsolutepath.substring(fileAbsolutepath.lastIndexOf("\\") + 1, fileAbsolutepath.indexOf(".class"));
                            System.out.println("className = " + className);

                            //2.获取类的完整路径
                            String classFullName = path.replace("/", ".") + "." + className;
                            System.out.println("classFullName=" + classFullName);

                            try {
                                Class<?> aClass = classLoader.loadClass(classFullName);

                                if (aClass.isAnnotationPresent(Component.class) ||
                                        aClass.isAnnotationPresent(Controller.class) ||
                                        aClass.isAnnotationPresent(Service.class) ||
                                        aClass.isAnnotationPresent(Repository.class)) {
                                    if(aClass.isAnnotationPresent(Component.class)) {
                                        // 获取到该注解
                                        Component component = aClass.getDeclaredAnnotation(Component.class);
                                        String id = component.value();
                                        // 判断id不为空串时，使用自定义id作为bean名称
                                        if(!"".equals(id)) {
                                            className = id;//替换
                                        }
                                    }
                                    Object instance = aClass.newInstance();
                                    String beanName = className.substring(0,1).toLowerCase() + className.substring(1);
                                    ioc.put(beanName, instance);
                                    System.out.println("已加入容器：" + beanName);
                                    //放入容器,将类名的首字母小写作为id

                                    System.out.println("已注入容器：" + className);
                                }

                            } catch(Exception e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    System.out.println("component目录为空，无.class文件");
                }
            } else {
                System.out.println("当前路径不是目录：" + file.getAbsolutePath());
            }
        } else {
            System.out.println("resource为null，扫描路径不存在");
        }
    }
    //编写方法返回容器中对象
    public Object getBean(String name){
        return ioc.get(name);
    }
}