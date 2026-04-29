package ioc;

public enum Isolation {
    DEFAULT(-1),
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);

    private final int level;

    Isolation(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}