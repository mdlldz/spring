public class SpELBean {
    private String name;
    private Monster monster;
    private String monsterNamprivate;
    String crySound;
    private String bookName;private Double result;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public String getMonsterNamprivate() {
        return monsterNamprivate;
    }

    public void setMonsterNamprivate(String monsterNamprivate) {
        this.monsterNamprivate = monsterNamprivate;
    }

    public String getCrySound() {
        return crySound;
    }

    public void setCrySound(String crySound) {
        this.crySound = crySound;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SpELBean{" +
                "name='" + name + '\'' +
                ", monster=" + monster +
                ", monsterNamprivate='" + monsterNamprivate + '\'' +
                ", crySound='" + crySound + '\'' +
                ", bookName='" + bookName + '\'' +
                ", result=" + result +
                '}';
    }
}
