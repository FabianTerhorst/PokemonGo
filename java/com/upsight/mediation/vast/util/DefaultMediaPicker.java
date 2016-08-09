package com.upsight.mediation.vast.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.upsight.mediation.vast.model.VASTMediaFile;
import com.upsight.mediation.vast.processor.VASTMediaPicker;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DefaultMediaPicker implements VASTMediaPicker {
    private static final String TAG = "DefaultMediaPicker";
    private static final int maxPixels = 5000;
    String SUPPORTED_VIDEO_TYPE_REGEX = "video/.*(?i)(mp4|3gpp|mp2t|webm|matroska)";
    private Context context;
    private int deviceArea;
    private int deviceHeight;
    private int deviceWidth;

    private class AreaComparator implements Comparator<VASTMediaFile> {
        private AreaComparator() {
        }

        public int compare(VASTMediaFile obj1, VASTMediaFile obj2) {
            int obj2Area = obj2.getWidth().intValue() * obj2.getHeight().intValue();
            int obj1Diff = Math.abs((obj1.getWidth().intValue() * obj1.getHeight().intValue()) - DefaultMediaPicker.this.deviceArea);
            int obj2Diff = Math.abs(obj2Area - DefaultMediaPicker.this.deviceArea);
            if (obj1Diff < obj2Diff) {
                return -1;
            }
            if (obj1Diff > obj2Diff) {
                return 1;
            }
            return 0;
        }
    }

    public DefaultMediaPicker(Context context) {
        this.context = context;
        setDeviceWidthHeight();
    }

    public DefaultMediaPicker(int width, int height) {
        setDeviceWidthHeight(width, height);
    }

    public VASTMediaFile pickVideo(List<VASTMediaFile> mediaFiles) {
        if (mediaFiles == null || prefilterMediaFiles(mediaFiles) == 0) {
            return null;
        }
        Collections.sort(mediaFiles, new AreaComparator());
        return getBestMatch(mediaFiles);
    }

    private int prefilterMediaFiles(List<VASTMediaFile> mediaFiles) {
        Iterator<VASTMediaFile> iter = mediaFiles.iterator();
        while (iter.hasNext()) {
            VASTMediaFile mediaFile = (VASTMediaFile) iter.next();
            if (TextUtils.isEmpty(mediaFile.getType())) {
                iter.remove();
            } else {
                BigInteger height = mediaFile.getHeight();
                if (height == null) {
                    iter.remove();
                } else {
                    int videoHeight = height.intValue();
                    if (videoHeight <= 0 || videoHeight >= maxPixels) {
                        iter.remove();
                    } else {
                        BigInteger width = mediaFile.getWidth();
                        if (width == null) {
                            iter.remove();
                        } else {
                            int videoWidth = width.intValue();
                            if (videoWidth <= 0 || videoWidth >= maxPixels) {
                                iter.remove();
                            } else if (TextUtils.isEmpty(mediaFile.getValue())) {
                                iter.remove();
                            }
                        }
                    }
                }
            }
        }
        return mediaFiles.size();
    }

    private void setDeviceWidthHeight() {
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
        this.deviceWidth = metrics.widthPixels;
        this.deviceHeight = metrics.heightPixels;
        this.deviceArea = this.deviceWidth * this.deviceHeight;
    }

    private void setDeviceWidthHeight(int width, int height) {
        this.deviceWidth = width;
        this.deviceHeight = height;
        this.deviceArea = this.deviceWidth * this.deviceHeight;
    }

    private boolean isMediaFileCompatible(VASTMediaFile media) {
        return media.getType().matches(this.SUPPORTED_VIDEO_TYPE_REGEX);
    }

    private VASTMediaFile getBestMatch(List<VASTMediaFile> list) {
        for (VASTMediaFile media : list) {
            if (isMediaFileCompatible(media)) {
                return media;
            }
        }
        return null;
    }
}
