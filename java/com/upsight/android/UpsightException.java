package com.upsight.android;

import java.util.Locale;

public final class UpsightException extends Exception {
    public UpsightException(Throwable throwable, String format, Object... args) {
        super(String.format(Locale.getDefault(), format, args), throwable);
    }

    public UpsightException(Throwable throwable) {
        super(throwable);
    }

    public UpsightException(String format, Object... args) {
        super(String.format(Locale.getDefault(), format, args));
    }
}
