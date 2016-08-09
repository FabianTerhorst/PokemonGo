package com.upsight.android.internal;

import android.content.Context;
import android.util.Log;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.upsight.android.internal.logger.LogWriter;
import com.upsight.android.internal.persistence.storable.StorableIdFactory;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import dagger.Module;
import dagger.Provides;
import java.util.UUID;
import javax.inject.Singleton;
import spacemadness.com.lunarconsole.R;

@Module
public final class ContextModule {
    private final Context mApplicationContext;

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$logger$UpsightLogger$Level = new int[Level.values().length];

        static {
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.VERBOSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.DEBUG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.INFO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.WARN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public ContextModule(Context context) {
        this.mApplicationContext = context.getApplicationContext();
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return this.mApplicationContext;
    }

    @Singleton
    @Provides
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Singleton
    @Provides
    LogWriter provideLogWriter() {
        return new LogWriter() {
            public void write(String tag, Level level, String message) {
                if (message.length() > UpsightLogger.MAX_LENGTH) {
                    int chunkCount = message.length() / UpsightLogger.MAX_LENGTH;
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = (i + 1) * UpsightLogger.MAX_LENGTH;
                        if (max >= message.length()) {
                            logMessage(tag, level, message.substring(i * UpsightLogger.MAX_LENGTH));
                        } else {
                            logMessage(tag, level, message.substring(i * UpsightLogger.MAX_LENGTH, max));
                        }
                    }
                    return;
                }
                logMessage(tag, level, message);
            }

            private void logMessage(String tag, Level level, String message) {
                switch (AnonymousClass3.$SwitchMap$com$upsight$android$logger$UpsightLogger$Level[level.ordinal()]) {
                    case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                        Log.v(tag, message);
                        return;
                    case R.styleable.LoadingImageView_circleCrop /*2*/:
                        Log.d(tag, message);
                        return;
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                        Log.i(tag, message);
                        return;
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                        Log.w(tag, message);
                        return;
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                        Log.e(tag, message);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    @Singleton
    @Provides
    StorableIdFactory provideTypeIdGenerator() {
        return new StorableIdFactory() {
            public String createObjectID() {
                return UUID.randomUUID().toString();
            }
        };
    }
}
