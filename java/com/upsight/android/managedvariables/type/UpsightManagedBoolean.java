package com.upsight.android.managedvariables.type;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightManagedVariablesExtension;
import com.upsight.android.managedvariables.type.UpsightManagedVariable.Listener;
import com.upsight.android.persistence.UpsightSubscription;

public abstract class UpsightManagedBoolean extends UpsightManagedVariable<Boolean> {
    protected UpsightManagedBoolean(String tag, Boolean defaultValue, Boolean value) {
        super(tag, defaultValue, value);
    }

    public static UpsightManagedBoolean fetch(UpsightContext upsight, String tag) {
        UpsightManagedVariablesExtension extension = (UpsightManagedVariablesExtension) upsight.getUpsightExtension(UpsightManagedVariablesExtension.EXTENSION_NAME);
        if (extension != null) {
            return (UpsightManagedBoolean) extension.getApi().fetch(UpsightManagedBoolean.class, tag);
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.managedvariables must be registered in your Android Manifest", new Object[0]);
        return null;
    }

    public static UpsightSubscription fetch(UpsightContext upsight, String tag, Listener<UpsightManagedBoolean> listener) {
        UpsightManagedVariablesExtension extension = (UpsightManagedVariablesExtension) upsight.getUpsightExtension(UpsightManagedVariablesExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().fetch(UpsightManagedBoolean.class, tag, listener);
        }
        upsight.getLogger().e(Upsight.LOG_TAG, "com.upsight.extension.managedvariables must be registered in your Android Manifest", new Object[0]);
        return new NoOpSubscription();
    }
}
