package com.upsight.android.managedvariables.experience;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightManagedVariablesExtension;
import java.util.List;

public abstract class UpsightUserExperience {

    public interface Handler {
        boolean onReceive();

        void onSynchronize(List<String> list);
    }

    public abstract Handler getHandler();

    public abstract void registerHandler(Handler handler);

    public static void registerHandler(UpsightContext upsight, Handler handler) {
        UpsightManagedVariablesExtension extension = (UpsightManagedVariablesExtension) upsight.getUpsightExtension(UpsightManagedVariablesExtension.EXTENSION_NAME);
        if (extension != null) {
            extension.getApi().registerUserExperienceHandler(handler);
        } else {
            upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.managedvariables must be registered in your Android Manifest", new Object[0]);
        }
    }
}
