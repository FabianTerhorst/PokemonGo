package com.upsight.android.internal.logger;

import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("com.upsight.message.log")
public final class LogMessage {
    @UpsightStorableIdentifier
    public String id;
    private Level level;
    private String message;
    private String tag;
    private String throwableString;

    LogMessage(String tag, Level level, String message, String throwableString) {
        this.tag = tag;
        this.level = level;
        this.message = message;
        this.throwableString = throwableString;
    }

    LogMessage(LogMessage source) {
        this(source.tag, source.level, source.message, source.throwableString);
    }

    LogMessage() {
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThrowableString() {
        return this.throwableString;
    }

    public void setThrowableString(String throwableString) {
        this.throwableString = throwableString;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogMessage that = (LogMessage) o;
        if (this.id != null) {
            if (this.id.equals(that.id)) {
                return true;
            }
        } else if (that.id == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
