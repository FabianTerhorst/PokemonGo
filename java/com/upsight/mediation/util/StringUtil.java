package com.upsight.mediation.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;
import spacemadness.com.lunarconsole.BuildConfig;

public class StringUtil {
    public static boolean isNullOrEmpty(@Nullable String str) {
        return str == null || str.trim().equals(BuildConfig.FLAVOR) || str.length() == 0;
    }

    @NonNull
    public static String join(@NonNull int[] values, @NonNull String separator) {
        StringBuilder builder = new StringBuilder();
        int size = values.length;
        for (int i = 0; i < size; i++) {
            builder.append(values[i]);
            if (i < size - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    @NonNull
    public static String join(@NonNull List values, @NonNull String separator) {
        StringBuilder builder = new StringBuilder();
        int size = values.size();
        for (int i = 0; i < size; i++) {
            builder.append(values.get(i));
            if (i < size - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    @NonNull
    public static String join(@NonNull String[] values, @NonNull String separator) {
        StringBuilder builder = new StringBuilder();
        int size = values.length;
        for (int i = 0; i < size; i++) {
            builder.append(values[i]);
            if (i < size - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    @NonNull
    public static int[] toIntArray(@NonNull String s, @NonNull String delimRegex) {
        String[] array = s.split(delimRegex);
        int count = array.length;
        int[] intValues = new int[count];
        for (int i = 0; i < count; i++) {
            intValues[i] = Integer.parseInt(array[i]);
        }
        return intValues;
    }
}
