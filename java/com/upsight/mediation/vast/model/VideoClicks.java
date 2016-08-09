package com.upsight.mediation.vast.model;

import java.util.ArrayList;
import java.util.List;
import spacemadness.com.lunarconsole.BuildConfig;

public class VideoClicks {
    private String clickThrough;
    private List<String> clickTracking;
    private List<String> customClick;

    public String getClickThrough() {
        return this.clickThrough;
    }

    public void setClickThrough(String clickThrough) {
        this.clickThrough = clickThrough;
    }

    public List<String> getClickTracking() {
        if (this.clickTracking == null) {
            this.clickTracking = new ArrayList();
        }
        return this.clickTracking;
    }

    public List<String> getCustomClick() {
        if (this.customClick == null) {
            this.customClick = new ArrayList();
        }
        return this.customClick;
    }

    public String toString() {
        return "VideoClicks [clickThrough=" + this.clickThrough + ", clickTracking=[" + listToString(this.clickTracking) + "], customClick=[" + listToString(this.customClick) + "] ]";
    }

    private String listToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        if (list == null) {
            return BuildConfig.FLAVOR;
        }
        for (int x = 0; x < list.size(); x++) {
            sb.append(((String) list.get(x)).toString());
        }
        return sb.toString();
    }
}
