package com.upsight.android.marketing.internal.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public final class MarketingContentModel {
    @JsonProperty("content_id")
    String contentId;
    @JsonProperty("context")
    JsonNode context;
    @JsonProperty("presentation")
    Presentation presentation;
    @JsonProperty("url")
    String templateUrl;

    public static class Presentation {
        public static final String STYLE_DIALOG = "dialog";
        public static final String STYLE_FULLSCREEN = "fullscreen";
        @JsonProperty("layout")
        DialogLayout layout;
        @JsonProperty("style")
        String style;

        public static class DialogLayout {
            @JsonProperty("landscape")
            public Dimensions landscape;
            @JsonProperty("portrait")
            public Dimensions portrait;

            public static class Dimensions {
                @JsonProperty("h")
                public int h;
                @JsonProperty("w")
                public int w;
                @JsonProperty("x")
                public int x;
                @JsonProperty("y")
                public int y;
            }
        }
    }

    static MarketingContentModel from(JsonNode json, ObjectMapper mapper) throws IOException {
        return (MarketingContentModel) mapper.treeToValue(json, MarketingContentModel.class);
    }

    MarketingContentModel() {
    }

    public String getContentID() {
        return this.contentId;
    }

    public String getTemplateUrl() {
        return this.templateUrl;
    }

    public JsonNode getContext() {
        return this.context;
    }

    public String getPresentationStyle() {
        return this.presentation.style;
    }

    public DialogLayout getDialogLayouts() {
        return this.presentation.layout;
    }
}
