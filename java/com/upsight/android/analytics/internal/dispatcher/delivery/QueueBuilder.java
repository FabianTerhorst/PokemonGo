package com.upsight.android.analytics.internal.dispatcher.delivery;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.dispatcher.delivery.Batcher.Config;
import com.upsight.android.analytics.internal.dispatcher.delivery.Batcher.Factory;
import com.upsight.android.analytics.internal.dispatcher.schema.Schema;
import com.upsight.android.analytics.internal.dispatcher.util.Selector;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Provider;
import rx.Scheduler;
import spacemadness.com.lunarconsole.BuildConfig;

public class QueueBuilder {
    private static final String CHARSET_UTF_8 = "UTF-8";
    public static final String MACRO_APP_TOKEN = "{app_token}";
    public static final String MACRO_APP_VERSION = "{app_version}";
    public static final String MACRO_HOST = "{host}";
    public static final String MACRO_PROTOCOL = "{protocol}";
    public static final String MACRO_PROTOCOL_VERSION = "{version}";
    public static final String MACRO_SDK_VERSION = "{sdk_version}";
    private static final String PROTOCOL_VERSION = "v1";
    private Clock mClock;
    private Map<String, String> mEndpointMacros;
    private Gson mGson;
    private JsonParser mJsonParser;
    private UpsightLogger mLogger;
    private Gson mResponseLoggingGson;
    private Provider<ResponseParser> mResponseParserProvider;
    private Scheduler mRetryExecutor;
    private Scheduler mSendExecutor;
    private SignatureVerifier mSignatureVerifier;
    private UpsightContext mUpsight;

    private class BatcherFactory implements Factory {
        private Config mConfig;

        public BatcherFactory(Config config) {
            this.mConfig = config;
        }

        public Batcher create(Schema schema, BatchSender batchSender) {
            return new Batcher(this.mConfig, schema, batchSender, QueueBuilder.this.mRetryExecutor);
        }
    }

    QueueBuilder(UpsightContext upsight, Gson gson, Gson responseLoggingGson, JsonParser jsonParser, Clock clock, UpsightLogger logger, Scheduler retryExecutor, Scheduler sendExecutor, SignatureVerifier signatureVerifier, Provider<ResponseParser> responseParserProvider) {
        this.mUpsight = upsight;
        this.mGson = gson;
        this.mResponseLoggingGson = responseLoggingGson;
        this.mJsonParser = jsonParser;
        this.mClock = clock;
        this.mLogger = logger;
        this.mRetryExecutor = retryExecutor;
        this.mSendExecutor = sendExecutor;
        this.mSignatureVerifier = signatureVerifier;
        this.mResponseParserProvider = responseParserProvider;
        createEndpointMacroMap();
    }

    public Queue build(String name, QueueConfig config, Selector<Schema> schemaSelectorByName, Selector<Schema> schemaSelectorByType) {
        BatchSender sender = new BatchSender(this.mUpsight, config.getBatchSenderConfig(), this.mRetryExecutor, this.mSendExecutor, new UpsightEndpoint(prepareEndpoint(config.getEndpointAddress()), this.mSignatureVerifier, this.mGson, this.mJsonParser, this.mResponseLoggingGson, this.mLogger), (ResponseParser) this.mResponseParserProvider.get(), this.mJsonParser, this.mClock, this.mLogger);
        return new Queue(name, schemaSelectorByName, schemaSelectorByType, new BatcherFactory(config.getBatcherConfig()), sender);
    }

    String prepareEndpoint(String baseUrl) {
        for (Entry<String, String> replacement : this.mEndpointMacros.entrySet()) {
            baseUrl = baseUrl.replace((CharSequence) replacement.getKey(), (CharSequence) replacement.getValue());
        }
        return baseUrl;
    }

    private void createEndpointMacroMap() {
        this.mEndpointMacros = new HashMap();
        this.mEndpointMacros.put(MACRO_PROTOCOL_VERSION, PROTOCOL_VERSION);
        this.mEndpointMacros.put(MACRO_APP_TOKEN, this.mUpsight.getApplicationToken());
        this.mEndpointMacros.put(MACRO_APP_VERSION, getAppVersionName(this.mUpsight));
        this.mEndpointMacros.put(MACRO_SDK_VERSION, this.mUpsight.getSdkVersion());
    }

    private String getAppVersionName(Context context) {
        String versionName = BuildConfig.FLAVOR;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (!(info == null || info.versionName == null)) {
                versionName = URLEncoder.encode(info.versionName, CHARSET_UTF_8);
            }
        } catch (NameNotFoundException e) {
            this.mLogger.e(QueueBuilder.class.getSimpleName(), "Could not get package info", e);
        } catch (UnsupportedEncodingException e2) {
            this.mLogger.e(QueueBuilder.class.getSimpleName(), "UTF-8 encoding not supported", e2);
        }
        return versionName;
    }
}
