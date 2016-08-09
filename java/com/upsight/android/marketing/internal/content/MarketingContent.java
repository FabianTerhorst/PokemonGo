package com.upsight.android.marketing.internal.content;

import android.view.View;
import com.squareup.otto.Bus;
import com.upsight.android.analytics.internal.action.ActionMap;
import com.upsight.android.analytics.internal.action.Actionable;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.internal.billboard.Billboard;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MarketingContent<T> extends Actionable {
    public static final String TRIGGER_APP_BACKGROUNDED = "app_backgrounded";
    public static final String TRIGGER_CONTENT_DISMISSED = "content_dismissed";
    public static final String TRIGGER_CONTENT_DISMISSED_WITH_REWARD = "content_dismissed_with_reward";
    public static final String TRIGGER_CONTENT_DISPLAYED = "content_displayed";
    public static final String TRIGGER_CONTENT_RECEIVED = "content_received";
    private AvailabilityEvent mAvailabilityEvent;
    private Billboard mBillboard = null;
    private UpsightContentMediator mContentMediator = null;
    private T mContentModel = null;
    private View mContentView = null;
    private Queue<Object> mEventQueue = new LinkedBlockingQueue();
    private Map<String, String> mExtras = new HashMap();
    private boolean mIsLoaded = false;
    private boolean mIsRewardGranted = false;
    private PendingDialog mPendingDialog;

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

    public static class PendingDialog {
        public static final String TEXT = "text";
        public static final String TRIGGER = "trigger";
        public final String mButtons;
        public final String mDismissTrigger;
        public final String mId;
        public final String mMessage;
        public final String mTitle;

        public PendingDialog(String id, String title, String message, String buttons, String dismissTrigger) {
            this.mId = id;
            this.mTitle = title;
            this.mMessage = message;
            this.mButtons = buttons;
            this.mDismissTrigger = dismissTrigger;
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

    public static class SubcontentAvailabilityEvent extends AvailabilityEvent {
        public SubcontentAvailabilityEvent(String id) {
            super(id);
        }
    }

    public static class SubdialogAvailabilityEvent extends AvailabilityEvent {
        private final PendingDialog mPendingDialog;

        public SubdialogAvailabilityEvent(String id, PendingDialog pendingDialog) {
            super(id);
            this.mPendingDialog = pendingDialog;
        }

        public PendingDialog getPendingDialog() {
            return this.mPendingDialog;
        }
    }

    public static MarketingContent create(String id, ActionMap<MarketingContent, MarketingContentActionContext> actionMap) {
        return new MarketingContent(id, actionMap);
    }

    private MarketingContent(String id, ActionMap<MarketingContent, MarketingContentActionContext> actionMap) {
        super(id, actionMap);
    }

    public void setContentMediator(UpsightContentMediator contentMediator) {
        this.mContentMediator = contentMediator;
    }

    public UpsightContentMediator getContentMediator() {
        return this.mContentMediator;
    }

    public void setContentModel(T contentModel) {
        this.mContentModel = contentModel;
    }

    public T getContentModel() {
        return this.mContentModel;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public void addPendingDialog(PendingDialog pendingDialog) {
        this.mPendingDialog = pendingDialog;
    }

    public boolean hasPendingDialog() {
        return this.mPendingDialog != null;
    }

    public PendingDialog popPendingDialog() {
        PendingDialog pendingDialog = this.mPendingDialog;
        this.mPendingDialog = null;
        return pendingDialog;
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

    public void markRewardGranted() {
        this.mIsRewardGranted = true;
    }

    public boolean isRewardGranted() {
        return this.mIsRewardGranted;
    }

    private void notifyAvailability(Bus bus) {
        if (isAvailable()) {
            bus.post(this.mAvailabilityEvent);
        }
    }

    boolean isLoaded() {
        return this.mContentModel != null && this.mIsLoaded;
    }

    public boolean isAvailable() {
        return isLoaded() && this.mAvailabilityEvent != null && getBoundBillboard() == null;
    }

    public String getExtra(String key) {
        return (String) this.mExtras.get(key);
    }

    public void putExtra(String key, String value) {
        this.mExtras.put(key, value);
    }

    public void bindBillboard(Billboard billboard) {
        this.mBillboard = billboard;
    }

    public Billboard getBoundBillboard() {
        return this.mBillboard;
    }

    public Queue<Object> getEventQueue() {
        return this.mEventQueue;
    }
}
