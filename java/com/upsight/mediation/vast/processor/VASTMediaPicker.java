package com.upsight.mediation.vast.processor;

import com.upsight.mediation.vast.model.VASTMediaFile;
import java.util.List;

public interface VASTMediaPicker {
    VASTMediaFile pickVideo(List<VASTMediaFile> list);
}
