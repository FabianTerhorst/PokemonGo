package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightCoreComponent;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionContext;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.io.IOUtils;
import rx.Scheduler;
import spacemadness.com.lunarconsole.BuildConfig;

@Module
public class UxmModule {
    public static final String GSON_UXM_SCHEMA = "gsonUxmSchema";
    public static final String JSON_PARSER_UXM_SCHEMA = "jsonParserUxmSchema";
    public static final String STRING_RAW_UXM_SCHEMA = "stringRawUxmSchema";

    @Singleton
    @Provides
    @Named("gsonUxmSchema")
    Gson provideUxmSchemaGson(UpsightContext upsight) {
        return upsight.getCoreComponent().gson();
    }

    @Singleton
    @Provides
    @Named("jsonParserUxmSchema")
    JsonParser provideUxmSchemaJsonParser(UpsightContext upsight) {
        return upsight.getCoreComponent().jsonParser();
    }

    @Singleton
    @Provides
    @Named("stringRawUxmSchema")
    String provideUxmSchemaRawString(UpsightContext upsight, @Named("resUxmSchema") Integer uxmSchemaRes) {
        UpsightLogger logger = upsight.getLogger();
        String schemaString = BuildConfig.FLAVOR;
        try {
            InputStream is = upsight.getResources().openRawResource(uxmSchemaRes.intValue());
            if (is != null) {
                return IOUtils.toString(is);
            }
            logger.e(Upsight.LOG_TAG, "Failed to find UXM schema file", new Object[0]);
            return schemaString;
        } catch (IOException e) {
            logger.e(Upsight.LOG_TAG, e, "Failed to read UXM schema file", new Object[0]);
            return schemaString;
        }
    }

    @Singleton
    @Provides
    UxmSchema provideUxmSchema(UpsightContext upsight, @Named("gsonUxmSchema") Gson uxmSchemaGson, @Named("jsonParserUxmSchema") JsonParser uxmSchemaJsonParser, @Named("stringRawUxmSchema") String uxmSchemaString) {
        UpsightLogger logger = upsight.getLogger();
        UxmSchema schema = null;
        if (!TextUtils.isEmpty(uxmSchemaString)) {
            try {
                schema = UxmSchema.create(uxmSchemaString, uxmSchemaGson, uxmSchemaJsonParser, logger);
            } catch (IllegalArgumentException e) {
                logger.e(Upsight.LOG_TAG, e, "Failed to parse UXM schema", new Object[0]);
            }
        }
        if (schema != null) {
            return schema;
        }
        schema = new UxmSchema(logger);
        logger.d(Upsight.LOG_TAG, "Empty UXM schema used", new Object[0]);
        return schema;
    }

    @Singleton
    @Provides
    ManagedVariableManager provideManagedVariableManager(UpsightContext upsight, @Named("main") Scheduler scheduler, UxmSchema uxmSchema) {
        return new ManagedVariableManager(scheduler, upsight.getDataStore(), uxmSchema);
    }

    @Singleton
    @Provides
    UxmBlockProvider provideUxmBlockProvider(UpsightContext upsight, @Named("stringRawUxmSchema") String uxmSchemaRawString, UxmSchema uxmSchema) {
        return new UxmBlockProvider(upsight, uxmSchemaRawString, uxmSchema);
    }

    @Singleton
    @Provides
    UxmContentFactory provideUxmContentFactory(UpsightContext upsight, @Named("main") Scheduler scheduler, UpsightUserExperience userExperience) {
        UpsightCoreComponent coreComponent = upsight.getCoreComponent();
        return new UxmContentFactory(new UxmContentActionContext(upsight, coreComponent.bus(), coreComponent.gson(), ((UpsightAnalyticsComponent) ((UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME)).getComponent()).clock(), scheduler.createWorker(), upsight.getLogger()), userExperience);
    }
}
