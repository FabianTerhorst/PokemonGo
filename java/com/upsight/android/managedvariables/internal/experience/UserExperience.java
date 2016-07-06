package com.upsight.android.managedvariables.internal.experience;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.analytics.internal.action.Actionable.ActionMapFinishedEvent;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.experience.UpsightUserExperience.Handler;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.ScheduleSyncNotificationEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserExperience extends UpsightUserExperience {
    private static final Handler DEFAULT_HANDLER = new DefaultHandler();
    private Bus mBus;
    private Handler mHandler = DEFAULT_HANDLER;
    private Map<String, List<String>> mSyncNotifications = new HashMap();

    private static class DefaultHandler implements Handler {
        private DefaultHandler() {
        }

        public boolean onReceive() {
            return true;
        }

        public void onSynchronize(List<String> list) {
        }
    }

    UserExperience(Bus bus) {
        this.mBus = bus;
        this.mBus.register(this);
    }

    public synchronized void registerHandler(Handler handler) {
        if (handler != null) {
            this.mHandler = handler;
        } else {
            this.mHandler = DEFAULT_HANDLER;
        }
    }

    public synchronized Handler getHandler() {
        return this.mHandler;
    }

    @Subscribe
    public synchronized void handleScheduleSyncNotificationEvent(ScheduleSyncNotificationEvent event) {
        this.mSyncNotifications.put(event.mId, event.mTags);
    }

    @Subscribe
    public synchronized void handleActionMapFinishedEvent(ActionMapFinishedEvent event) {
        List<String> pendingNotificationTags = (List) this.mSyncNotifications.remove(event.mId);
        if (pendingNotificationTags != null) {
            this.mHandler.onSynchronize(pendingNotificationTags);
        }
    }
}
