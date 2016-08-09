package com.upsight.mediation.vast.Postroll;

import android.view.ViewGroup;

public interface Postroll {

    public interface Listener {
        void closeClicked();

        void infoClicked(boolean z);

        void onOpenMRaidUrl(String str);

        void replayedClicked();
    }

    public enum Type {
        MRAID,
        DEFAULT
    }

    void hide();

    void init();

    boolean isReady();

    void show(ViewGroup viewGroup);
}
