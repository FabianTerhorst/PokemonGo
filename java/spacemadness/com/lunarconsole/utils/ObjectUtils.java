package spacemadness.com.lunarconsole.utils;

public class ObjectUtils {
    public static boolean areEqual(Object o1, Object o2) {
        return (o1 != null && o1.equals(o2)) || (o1 == null && o2 == null);
    }

    public static <T> T notNullOrDefault(T obj, T defaultObj) {
        if (defaultObj != null) {
            return obj != null ? obj : defaultObj;
        } else {
            throw new NullPointerException("Default object is null");
        }
    }

    public static String toString(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    public static <T> T as(Object obj, Class<? extends T> cls) {
        return cls.isInstance(obj) ? obj : null;
    }
}
