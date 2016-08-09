package com.upsight.android.analytics.internal.association;

import com.google.gson.JsonObject;

public interface AssociationManager {
    void associate(String str, JsonObject jsonObject);

    void launch();
}
