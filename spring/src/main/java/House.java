public class House {
    private String name;
    public House(){
        System.out.println("House()构造器");
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        System.out.println("House setName()...");
        this.name = name;
    }
//下面的方法由程序员根据自己的业务逻辑编写，名字也不是固定的
    public void init() {
        System.out.println("House init()..");
    }

    public void destory() {
        System.out.println("House destory()..");
    }

    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                '}';
    }
}
