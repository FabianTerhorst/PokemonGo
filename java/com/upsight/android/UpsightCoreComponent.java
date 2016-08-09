package com.upsight.android;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.squareup.otto.Bus;
import com.upsight.android.persistence.UpsightDataStore;
import javax.inject.Named;
import rx.Scheduler;

public interface UpsightCoreComponent {

    public interface Factory {
        UpsightCoreComponent create(Context context);
    }

    Context applicationContext();

    @Named("background")
    UpsightDataStore backgroundDataStore();

    Bus bus();

    Gson gson();

    JsonParser jsonParser();

    @Named("callback")
    Scheduler observeOnScheduler();

    @Named("execution")
    Scheduler subscribeOnScheduler();

    UpsightContext upsightContext();
}
