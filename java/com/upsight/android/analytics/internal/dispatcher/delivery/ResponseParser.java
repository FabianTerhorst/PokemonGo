package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

class ResponseParser {
    private ObjectMapper mMapper;

    public static class Response {
        public final String error;
        public final Collection<EndpointResponse> responses;

        public Response(Collection<EndpointResponse> responses, String error) {
            this.responses = responses;
            this.error = error;
        }
    }

    public static class ResponseElementJson {
        @JsonProperty("content")
        public JsonNode content;
        @JsonProperty("type")
        public String type;
    }

    public static class ResponseJson {
        @JsonProperty("error")
        public String error;
        @JsonProperty("response")
        public List<ResponseElementJson> response;
    }

    @Inject
    public ResponseParser(@Named("config-mapper") ObjectMapper mapper) {
        this.mMapper = mapper;
    }

    public synchronized Response parse(String resposneJson) throws IOException {
        ResponseJson responses;
        Collection<EndpointResponse> resps;
        responses = (ResponseJson) this.mMapper.readValue(resposneJson, ResponseJson.class);
        resps = new LinkedList();
        if (responses.response != null) {
            for (ResponseElementJson rj : responses.response) {
                resps.add(EndpointResponse.create(rj.type, this.mMapper.writeValueAsString(rj.content)));
            }
        }
        return new Response(resps, responses.error);
    }
}
