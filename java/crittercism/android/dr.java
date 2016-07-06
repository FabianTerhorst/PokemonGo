package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;
import java.util.UUID;

public final class dr {
    private SharedPreferences a;
    private SharedPreferences b;
    private Context c;

    public dr(Context context) {
        if (context == null) {
            throw new NullPointerException("context was null");
        }
        this.c = context;
        this.a = context.getSharedPreferences("com.crittercism.usersettings", 0);
        this.b = context.getSharedPreferences("com.crittercism.prefs", 0);
        if (this.a == null) {
            throw new NullPointerException("prefs were null");
        } else if (this.b == null) {
            throw new NullPointerException("legacy prefs were null");
        }
    }

    private String b() {
        String str = null;
        try {
            String string = Secure.getString(this.c.getContentResolver(), "android_id");
            if (!(string == null || string.length() <= 0 || string.equals("9774d56d682e549c"))) {
                UUID nameUUIDFromBytes = UUID.nameUUIDFromBytes(string.getBytes("utf8"));
                if (nameUUIDFromBytes != null) {
                    str = nameUUIDFromBytes.toString();
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.a(th);
        }
        if (str == null || str.length() == 0) {
            try {
                str = UUID.randomUUID().toString();
            } catch (ThreadDeath e2) {
                throw e2;
            } catch (Throwable th2) {
                dx.a(th2);
            }
        }
        return str;
    }

    public final String a() {
        String string = this.a.getString("hashedDeviceID", null);
        if (string == null) {
            string = this.b.getString("com.crittercism.prefs.did", null);
            if (string != null && a(string)) {
                Editor edit = this.b.edit();
                edit.remove("com.crittercism.prefs.did");
                edit.commit();
            }
        }
        if (string != null) {
            return string;
        }
        string = b();
        a(string);
        return string;
    }

    private boolean a(String str) {
        Editor edit = this.a.edit();
        edit.putString("hashedDeviceID", str);
        return edit.commit();
    }
}
