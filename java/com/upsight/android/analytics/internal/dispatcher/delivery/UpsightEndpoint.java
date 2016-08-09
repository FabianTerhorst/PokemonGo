package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.upsight.android.Upsight;
import com.upsight.android.internal.util.GzipHelper;
import com.upsight.android.logger.UpsightLogger;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

class UpsightEndpoint {
    private static final String CONNECTION_CLOSE = "close";
    private static final int CONNECTION_TIMEOUT_MS = 30000;
    private static final String CONTENT_ENCODING_GZIP = "gzip";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private static final String EMPTY_STRING = "";
    static final String HTTP_HEADER_REF_ID = "X-US-Ref-Id";
    static final String HTTP_HEADER_US_DIGEST = "X-US-DIGEST";
    public static final String LOG_TEXT_POSTING = "POSTING:       ";
    public static final String LOG_TEXT_RECEIVING = "RECEIVING:     ";
    public static final String LOG_TEXT_REQUEST_BODY = "\nREQUEST BODY:  ";
    public static final String LOG_TEXT_RESPONSE_BODY = "\nRESPONSE BODY: ";
    public static final String LOG_TEXT_RESPONSE_BODY_NONE = "[NONE]";
    public static final String LOG_TEXT_STATUS_CODE = "\nSTATUS CODE:   ";
    public static final String LOG_TEXT_TO = "\nTO:            ";
    private static final String POST_METHOD_NAME = "POST";
    public static final String SIGNED_MESSAGE_SEPARATOR = ":";
    private static final String USER_AGENT_ANDROID = ("Android-" + VERSION.SDK_INT);
    private static final boolean USE_GZIP = false;
    private String mEndpointAddress;
    private Gson mGson;
    private JsonParser mJsonParser;
    private UpsightLogger mLogger;
    private Gson mRequestLoggingGson;
    private SignatureVerifier mSignatureVerifier;

    public static class Response {
        public final String body;
        public final int statusCode;

        public Response(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        public boolean isOk() {
            return this.statusCode == 200;
        }
    }

    public UpsightEndpoint(String endpointAddress, SignatureVerifier signatureVerifier, Gson gson, JsonParser jsonParser, Gson requestLoggingGson, UpsightLogger logger) {
        this.mEndpointAddress = endpointAddress;
        this.mSignatureVerifier = signatureVerifier;
        this.mGson = gson;
        this.mJsonParser = jsonParser;
        this.mRequestLoggingGson = requestLoggingGson;
        this.mLogger = logger;
    }

    public Response send(UpsightRequest request) throws IOException {
        String refId = UUID.randomUUID().toString();
        HttpURLConnection urlConnection = null;
        try {
            String requestBody = this.mGson.toJson(request);
            this.mLogger.d(Upsight.LOG_TAG, LOG_TEXT_POSTING + refId + LOG_TEXT_TO + this.mEndpointAddress + LOG_TEXT_REQUEST_BODY + this.mRequestLoggingGson.toJson(request), new Object[0]);
            byte[] body = getRequestBodyBytes(requestBody, false);
            urlConnection = (HttpURLConnection) new URL(this.mEndpointAddress).openConnection();
            urlConnection.setRequestMethod(POST_METHOD_NAME);
            urlConnection.setRequestProperty(HTTP_HEADER_REF_ID, refId);
            urlConnection.setRequestProperty("Content-Type", CONTENT_TYPE_APPLICATION_JSON);
            urlConnection.setRequestProperty("User-Agent", USER_AGENT_ANDROID);
            urlConnection.setRequestProperty("Connection", CONNECTION_CLOSE);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT_MS);
            urlConnection.setReadTimeout(CONNECTION_TIMEOUT_MS);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(body.length);
            IOUtils.write(body, urlConnection.getOutputStream());
            String respBody = null;
            int statusCode = urlConnection.getResponseCode();
            StringBuilder sb = new StringBuilder().append(LOG_TEXT_RECEIVING).append(refId).append(LOG_TEXT_STATUS_CODE).append(statusCode);
            if (statusCode == 200) {
                String logRespBody;
                respBody = getVerifiedResponse(urlConnection);
                if (TextUtils.isEmpty(respBody)) {
                    logRespBody = LOG_TEXT_RESPONSE_BODY_NONE;
                } else {
                    logRespBody = this.mRequestLoggingGson.toJson(this.mJsonParser.parse(respBody));
                }
                sb.append(LOG_TEXT_RESPONSE_BODY).append(logRespBody);
            }
            this.mLogger.d(Upsight.LOG_TAG, sb.toString(), new Object[0]);
            Response response = new Response(statusCode, respBody);
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private byte[] getRequestBodyBytes(String requestBody, boolean useGzip) throws IOException {
        return useGzip ? GzipHelper.compress(requestBody) : requestBody.getBytes();
    }

    private String getVerifiedResponse(HttpURLConnection urlConnection) throws IOException {
        String respBody = EMPTY_STRING;
        String refId = urlConnection.getRequestProperty(HTTP_HEADER_REF_ID);
        String signature = urlConnection.getHeaderField(HTTP_HEADER_US_DIGEST);
        if (TextUtils.isEmpty(refId) || TextUtils.isEmpty(signature)) {
            return respBody;
        }
        InputStream is = urlConnection.getInputStream();
        if (is == null) {
            return respBody;
        }
        String unverifiedRespBody = IOUtils.toString(is);
        if (TextUtils.isEmpty(unverifiedRespBody)) {
            return respBody;
        }
        try {
            if (this.mSignatureVerifier.verify((unverifiedRespBody + SIGNED_MESSAGE_SEPARATOR + refId).getBytes(), Base64.decode(signature, 8))) {
                return unverifiedRespBody;
            }
            return respBody;
        } catch (IllegalArgumentException e) {
            this.mLogger.e(Upsight.LOG_TAG, e, "Message signature is not valid Base64. X-US-DIGEST: " + signature, new Object[0]);
            return respBody;
        }
    }
}
