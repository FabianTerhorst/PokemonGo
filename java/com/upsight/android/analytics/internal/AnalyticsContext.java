package com.upsight.android.analytics.internal;

import android.content.ContextWrapper;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.R;
import java.io.IOException;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;

public class AnalyticsContext extends ContextWrapper {
    private static final String LOG_TAG = AnalyticsContext.class.getSimpleName();
    private final UpsightContext mUpsight;

    @Inject
    public AnalyticsContext(UpsightContext upsight) {
        super(upsight);
        this.mUpsight = upsight;
    }

    public String getDefaultDispatcherConfiguration() {
        try {
            return IOUtils.toString(getResources().openRawResource(R.raw.dispatcher_config));
        } catch (IOException e) {
            this.mUpsight.getLogger().e(LOG_TAG, "Could not read default configuration.", e);
            return null;
        }
    }
}
