package spacemadness.com.lunarconsole.utils;

import android.util.Log;
import java.util.List;
import spacemadness.com.lunarconsole.BuildConfig;

public class StringUtils {
    public static int length(String str) {
        return str != null ? str.length() : 0;
    }

    public static boolean contains(String str, CharSequence cs) {
        return (str == null || cs == null || !str.contains(cs)) ? false : true;
    }

    public static boolean containsIgnoreCase(String str, String cs) {
        return str != null && cs != null && str.length() >= cs.length() && str.toLowerCase().contains(cs.toLowerCase());
    }

    public static boolean hasPrefix(String str, String prefix) {
        return (str == null || prefix == null || !str.startsWith(prefix)) ? false : true;
    }

    public static boolean IsNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String nullOrNonEmpty(String str) {
        return IsNullOrEmpty(str) ? null : str;
    }

    public static String NonNullOrEmpty(String str) {
        return str != null ? str : BuildConfig.FLAVOR;
    }

    public static <T> String Join(List<T> list) {
        return Join((List) list, ",");
    }

    public static <T> String Join(List<T> list, String separator) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (T e : list) {
            builder.append(e);
            i++;
            if (i < list.size()) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static <T> String Join(T[] array) {
        return Join((Object[]) array, ",");
    }

    public static <T> String Join(T[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(boolean[] array) {
        return Join(array, ",");
    }

    public static String Join(boolean[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(byte[] array) {
        return Join(array, ",");
    }

    public static String Join(byte[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(short[] array) {
        return Join(array, ",");
    }

    public static String Join(short[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(char[] array) {
        return Join(array, ",");
    }

    public static String Join(char[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(int[] array) {
        return Join(array, ",");
    }

    public static String Join(int[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(long[] array) {
        return Join(array, ",");
    }

    public static String Join(long[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(float[] array) {
        return Join(array, ",");
    }

    public static String Join(float[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String Join(double[] array) {
        return Join(array, ",");
    }

    public static String Join(double[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String TryFormat(String format, Object... args) {
        if (!(format == null || args == null || args.length <= 0)) {
            try {
                format = String.format(format, args);
            } catch (Exception e) {
                Log.e("Lunar", "Error while formatting String: " + e.getMessage());
            }
        }
        return format;
    }
}
