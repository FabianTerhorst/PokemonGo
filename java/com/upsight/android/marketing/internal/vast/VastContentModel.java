package com.upsight.android.marketing.internal.vast;

import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.session.SessionManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class VastContentModel {
    @SerializedName("adapter_id")
    @Expose
    Integer adapterId;
    @SerializedName("is_rewarded")
    @Expose
    Boolean isRewarded;
    @SerializedName("max_vast_file_size")
    @Expose
    String maxVastFileSize;
    @SerializedName("settings")
    @Expose
    Settings settings;
    @SerializedName("should_validate_schema")
    @Expose
    Boolean shouldValidateSchema;

    public static class Settings {
        @SerializedName("beacons")
        @Expose
        Beacons beacons;
        @SerializedName("campaign_id")
        @Expose
        Integer campaignId;
        @SerializedName("cb_ms")
        @Expose
        Integer cbMs;
        @SerializedName("cta")
        @Expose
        String cta;
        @SerializedName("endcard_script")
        @Expose
        String endcardScript;
        @SerializedName("id")
        @Expose
        Integer id;
        @SerializedName("is_endcard_script_encoded")
        @Expose
        Boolean isEndcardScriptEncoded;
        @SerializedName("postroll")
        @Expose
        Integer postroll;
        @SerializedName("script")
        @Expose
        String script;
        @SerializedName("t")
        @Expose
        Integer t;

        public static class Beacons {
            @SerializedName("click")
            @Expose
            String click;
            @SerializedName("impression")
            @Expose
            String impression;
        }
    }

    static VastContentModel from(JsonElement json, Gson gson) throws IOException {
        try {
            return (VastContentModel) gson.fromJson(json, VastContentModel.class);
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }

    VastContentModel() {
    }

    public Integer getAdapterId() {
        return this.adapterId;
    }

    public Boolean isRewarded() {
        return this.isRewarded;
    }

    public String getMaxVastFileSize() {
        return this.maxVastFileSize;
    }

    public Boolean shouldValidateSchema() {
        return this.shouldValidateSchema;
    }

    public HashMap<String, String> getSettings() {
        return new HashMap<String, String>() {
            {
                put("beacon-click", VastContentModel.this.settings.beacons.click);
                put("beacon-impression", VastContentModel.this.settings.beacons.impression);
                put(SessionManager.SESSION_CAMPAIGN_ID, Integer.toString(VastContentModel.this.settings.campaignId.intValue()));
                put("cb_ms", Integer.toString(VastContentModel.this.settings.cbMs.intValue()));
                put("cta", VastContentModel.this.settings.cta);
                put(TriggerIfContentAvailable.ID, Integer.toString(VastContentModel.this.settings.id.intValue()));
                put("postroll", Integer.toString(VastContentModel.this.settings.postroll.intValue()));
                put("script", VastContentModel.this.settings.script);
                put("t", Integer.toString(VastContentModel.this.settings.t.intValue()));
                VastContentModel.this.appendEndcard(this);
            }
        };
    }

    private void appendEndcard(Map<String, String> parameters) {
        if (!TextUtils.isEmpty(this.settings.endcardScript)) {
            if (this.settings.isEndcardScriptEncoded == null || !this.settings.isEndcardScriptEncoded.booleanValue()) {
                parameters.put("endcard_script", this.settings.endcardScript);
                return;
            }
            try {
                parameters.put("endcard_script", Arrays.toString(Base64.decode(this.settings.endcardScript, 0)));
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
