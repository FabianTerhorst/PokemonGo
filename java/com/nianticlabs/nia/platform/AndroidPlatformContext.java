package com.nianticlabs.nia.platform;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.util.Base64;
import com.nianticlabs.nia.contextservice.ContextService;
import java.io.File;
import java.util.Locale;

public class AndroidPlatformContext extends ContextService {
    private final SharedPreferences prefs;

    public AndroidPlatformContext(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.prefs = context.getSharedPreferences(context.getPackageName() + ".PREFS", 0);
    }

    public String getDeviceCountryCode() {
        return Locale.getDefault().getCountry();
    }

    public String getDeviceLanguageCode() {
        return Locale.getDefault().getLanguage();
    }

    public String getCacheDirectory() {
        return this.context.getCacheDir().getPath();
    }

    public String concatPath(String path0, String path1) {
        return new File(path0, path1).getPath();
    }

    public boolean makePathRecursive(String path) {
        return new File(path).mkdirs();
    }

    public boolean pathExists(String path) {
        return new File(path).exists();
    }

    public boolean deleteFile(String path) {
        return new File(path).delete();
    }

    public long fileSize(String path) {
        return new File(path).length();
    }

    public byte[] getSetting(String key) {
        String base64Value = this.prefs.getString(key, null);
        if (base64Value != null) {
            return Base64.decode(base64Value.getBytes(), 0);
        }
        return null;
    }

    public boolean setSetting(String key, byte[] value) {
        return this.prefs.edit().putString(key, Base64.encodeToString(value, 0)).commit();
    }

    public boolean deleteSetting(String key) {
        return this.prefs.edit().remove(key).commit();
    }

    public String getDeviceId() {
        return Secure.getString(this.context.getContentResolver(), "android_id");
    }
}
