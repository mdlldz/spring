package ioc;

public class TransactionContextHolder {
    private static final ThreadLocal<TransactionStatus> holder = new ThreadLocal<>();

    public static void set(TransactionStatus status) {
        holder.set(status);
    }

    public static TransactionStatus get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}