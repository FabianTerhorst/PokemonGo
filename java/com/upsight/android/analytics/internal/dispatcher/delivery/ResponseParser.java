package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

class ResponseParser {
    private Gson mGson;

    public static class Response {
        public final String error;
        public final Collection<EndpointResponse> responses;

        public Response(Collection<EndpointResponse> responses, String error) {
            this.responses = responses;
            this.error = error;
        }
    }

    public static class ResponseElementJson {
        @SerializedName("content")
        @Expose
        public JsonElement content;
        @SerializedName("type")
        @Expose
        public String type;
    }

    public static class ResponseJson {
        @SerializedName("error")
        @Expose
        public String error;
        @SerializedName("response")
        @Expose
        public List<ResponseElementJson> response;
    }

    @Inject
    public ResponseParser(@Named("config-gson") Gson gson) {
        this.mGson = gson;
    }

    public synchronized Response parse(String resposneJson) throws IOException {
        ResponseJson responses;
        Collection<EndpointResponse> resps;
        try {
            responses = (ResponseJson) this.mGson.fromJson(resposneJson, ResponseJson.class);
            resps = new LinkedList();
            if (responses.response != null) {
                for (ResponseElementJson rj : responses.response) {
                    resps.add(EndpointResponse.create(rj.type, rj.content.toString()));
                }
            }
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }
        return new Response(resps, responses.error);
    }
}
