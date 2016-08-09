package com.upsight.android.marketing.internal.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;

public final class MarketingContentModel {
    @SerializedName("content_id")
    @Expose
    String contentId;
    @SerializedName("context")
    @Expose
    JsonElement context;
    @SerializedName("presentation")
    @Expose
    Presentation presentation;
    @SerializedName("url")
    @Expose
    String templateUrl;

    public static class Presentation {
        public static final String STYLE_DIALOG = "dialog";
        public static final String STYLE_FULLSCREEN = "fullscreen";
        @SerializedName("layout")
        @Expose
        DialogLayout layout;
        @SerializedName("style")
        @Expose
        String style;

        public static class DialogLayout {
            @SerializedName("landscape")
            @Expose
            public Dimensions landscape;
            @SerializedName("portrait")
            @Expose
            public Dimensions portrait;

            public static class Dimensions {
                @SerializedName("h")
                @Expose
                public int h;
                @SerializedName("w")
                @Expose
                public int w;
                @SerializedName("x")
                @Expose
                public int x;
                @SerializedName("y")
                @Expose
                public int y;
            }
        }
    }

    static MarketingContentModel from(JsonElement json, Gson gson) throws IOException {
        try {
            return (MarketingContentModel) gson.fromJson(json, MarketingContentModel.class);
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
    }

    MarketingContentModel() {
    }

    public String getContentID() {
        return this.contentId;
    }

    public String getTemplateUrl() {
        return this.templateUrl;
    }

    public JsonElement getContext() {
        return this.context;
    }

    public String getPresentationStyle() {
        return this.presentation != null ? this.presentation.style : null;
    }

    public DialogLayout getDialogLayouts() {
        return this.presentation != null ? this.presentation.layout : null;
    }
}
