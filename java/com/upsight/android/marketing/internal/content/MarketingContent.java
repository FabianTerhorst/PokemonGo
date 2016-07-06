package com.upsight.android.marketing.internal.content;

import android.view.View;
import com.squareup.otto.Bus;
import com.upsight.android.analytics.internal.action.ActionMap;
import com.upsight.android.analytics.internal.action.Actionable;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketingContent extends Actionable {
    public static final String TRIGGER_APP_BACKGROUNDED = "app_backgrounded";
    public static final String TRIGGER_CONTENT_DISMISSED = "content_dismissed";
    public static final String TRIGGER_CONTENT_DISPLAYED = "content_displayed";
    public static final String TRIGGER_CONTENT_RECEIVED = "content_received";
    public static final String UPSIGHT_CONTENT_PROVIDER = "upsight";
    private AvailabilityEvent mAvailabilityEvent;
    private MarketingContentModel mContentModel = null;
    private View mContentView = null;
    private Map<String, String> mExtras = new HashMap();
    private boolean mIsConsumed = false;
    private boolean mIsLoaded = false;

    public static abstract class AvailabilityEvent {
        private final String mId;

        private AvailabilityEvent(String id) {
            this.mId = id;
        }

        public String getId() {
            return this.mId;
        }
    }

    public static class ContentLoadedEvent {
        private final String mId;

        private ContentLoadedEvent(String id) {
            this.mId = id;
        }

        public String getId() {
            return this.mId;
        }
    }

    public static class ScopedAvailabilityEvent extends AvailabilityEvent {
        private final String[] mScopes;

        public ScopedAvailabilityEvent(String id, String[] scopes) {
            super(id);
            this.mScopes = scopes;
        }

        public List<String> getScopes() {
            return Arrays.asList(this.mScopes);
        }
    }

    public static class ScopelessAvailabilityEvent extends AvailabilityEvent {
        private final String mParentId;

        public ScopelessAvailabilityEvent(String id, String parentId) {
            super(id);
            this.mParentId = parentId;
        }

        public String getParentId() {
            return this.mParentId;
        }
    }

    public static MarketingContent create(String id, ActionMap<MarketingContent, MarketingContentActionContext> actionMap) {
        return new MarketingContent(id, actionMap);
    }

    private MarketingContent(String id, ActionMap<MarketingContent, MarketingContentActionContext> actionMap) {
        super(id, actionMap);
    }

    public String getContentProvider() {
        return UPSIGHT_CONTENT_PROVIDER;
    }

    public void setContentModel(MarketingContentModel contentModel) {
        this.mContentModel = contentModel;
    }

    public MarketingContentModel getContentModel() {
        return this.mContentModel;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public void markLoaded(Bus bus) {
        this.mIsLoaded = true;
        bus.post(new ContentLoadedEvent(getId()));
        notifyAvailability(bus);
    }

    public void markPresentable(AvailabilityEvent event, Bus bus) {
        this.mAvailabilityEvent = event;
        notifyAvailability(bus);
    }

    public void markConsumed() {
        this.mIsConsumed = true;
    }

    private void notifyAvailability(Bus bus) {
        if (isAvailable()) {
            bus.post(this.mAvailabilityEvent);
        }
    }

    boolean isLoaded() {
        return (this.mContentModel == null || this.mContentView == null || !this.mIsLoaded) ? false : true;
    }

    public boolean isAvailable() {
        return (!isLoaded() || this.mAvailabilityEvent == null || this.mIsConsumed) ? false : true;
    }

    public String getExtra(String key) {
        return (String) this.mExtras.get(key);
    }

    public void putExtra(String key, String value) {
        this.mExtras.put(key, value);
    }
}
