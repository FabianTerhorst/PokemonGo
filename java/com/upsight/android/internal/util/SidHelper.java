package com.upsight.android.internal.util;

import android.content.Context;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public final class SidHelper {
    private static final int BYTE_BUFFER_CAPACITY = 8;
    public static final String PREFERENCE_KEY_SID = "sid";

    public static synchronized String getSid(Context context) {
        String sid;
        synchronized (SidHelper.class) {
            sid = PreferencesHelper.getString(context, PREFERENCE_KEY_SID, null);
            if (sid == null) {
                UUID uuid = UUID.randomUUID();
                sid = new BigInteger(1, longToBytes(uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits())).toString();
                PreferencesHelper.putString(context, PREFERENCE_KEY_SID, sid);
            }
        }
        return sid;
    }

    private static byte[] longToBytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(BYTE_BUFFER_CAPACITY);
        buffer.putLong(value);
        return buffer.array();
    }
}
