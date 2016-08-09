package com.upsight.mediation.vast.processor;

import android.text.TextUtils;
import com.upsight.mediation.vast.model.VASTMediaFile;
import com.upsight.mediation.vast.model.VASTModel;
import java.util.List;

public class VASTModelPostValidator {
    private static final String TAG = "VASTModelPostValidator";

    public static boolean validate(VASTModel model) {
        if (validateModel(model)) {
            return true;
        }
        return false;
    }

    private static boolean validateModel(VASTModel model) {
        if (!model.evaluateAdTitle()) {
            return false;
        }
        if (!model.evaluateAdSystem()) {
            return false;
        }
        List<String> impressions = model.getImpressions();
        if (impressions == null || impressions.size() == 0) {
            return false;
        }
        List<VASTMediaFile> mediaFiles = model.getMediaFiles();
        if (mediaFiles == null || mediaFiles.size() == 0) {
            return false;
        }
        return true;
    }

    public static boolean pickMediaFile(VASTModel model, VASTMediaPicker mediaPicker) {
        if (mediaPicker == null) {
            return false;
        }
        VASTMediaFile mediaFile = mediaPicker.pickVideo(model.getMediaFiles());
        if (mediaFile == null) {
            return false;
        }
        String url = mediaFile.getValue();
        String deliveryType = mediaFile.getDelivery();
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        model.setPickedMediaFileLocation(url);
        model.setPickedMediaFileDeliveryType(deliveryType);
        return true;
    }
}
