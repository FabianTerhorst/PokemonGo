package com.upsight.android.analytics.internal;

import android.content.Context;
import com.upsight.android.internal.util.PreferencesHelper;

public final class EventSequenceId {
    private static final long INITIAL_SEQUENCE_ID = 1;
    private static final String PREFERENCES_KEY_SEQ_ID = "seq_id";

    private EventSequenceId() {
    }

    public static synchronized long getAndIncrement(Context context) {
        long currentId;
        synchronized (EventSequenceId.class) {
            currentId = PreferencesHelper.getLong(context, PREFERENCES_KEY_SEQ_ID, INITIAL_SEQUENCE_ID);
            PreferencesHelper.putLong(context, PREFERENCES_KEY_SEQ_ID, currentId + INITIAL_SEQUENCE_ID);
        }
        return currentId;
    }
}
