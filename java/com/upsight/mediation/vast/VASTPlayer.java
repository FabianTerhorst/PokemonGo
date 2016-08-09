package com.upsight.mediation.vast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.activity.VASTActivity;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.processor.VASTProcessor;
import com.upsight.mediation.vast.util.DefaultMediaPicker;
import com.upsight.mediation.vast.util.NetworkTools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class VASTPlayer {
    public static final int ERROR_EXCEEDED_WRAPPER_LIMIT = 302;
    public static final int ERROR_FILE_NOT_FOUND = 401;
    public static final int ERROR_GENERAL_LINEAR = 400;
    public static final int ERROR_GENERAL_WRAPPER = 300;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_NO_COMPATIBLE_MEDIA_FILE = 403;
    public static final int ERROR_NO_NETWORK = 1;
    public static final int ERROR_NO_VAST_IN_WRAPPER = 303;
    public static final int ERROR_SCHEMA_VALIDATION = 101;
    public static final int ERROR_UNDEFINED = 900;
    public static final int ERROR_UNSUPPORTED_VERSION = 102;
    public static final int ERROR_VIDEO_PLAYBACK = 405;
    public static final int ERROR_VIDEO_TIMEOUT = 402;
    public static final int ERROR_WRAPPER_TIMEOUT = 301;
    public static final int ERROR_XML_PARSE = 100;
    private static final String TAG = "VASTPlayer";
    public static final String VERSION = "1.3";
    public static VASTPlayer currentPlayer;
    private String actionText;
    private Context context;
    private final int downloadTimeout;
    private final String endCardHtml;
    private boolean isRewarded;
    public VASTPlayerListener listener;
    private boolean loaded = false;
    private String maxVideoFileSize;
    private boolean postroll;
    private boolean shouldValidateSchema;
    private long skipOffset;
    private VASTModel vastModel;

    public interface VASTPlayerListener {
        void vastClick();

        void vastComplete();

        void vastDismiss();

        void vastDisplay();

        void vastError(int i);

        void vastProgress(int i);

        void vastReady();

        void vastReplay();

        void vastRewardedVideoComplete();

        void vastSkip();
    }

    public VASTPlayer(Context context, VASTPlayerListener listener, boolean postroll, String endCardHtml, long skipOffset, boolean isRewarded, String maxVideoFileSize, boolean shouldValidateSchema, String actionText, int downloadTimeout) {
        this.context = context;
        this.listener = listener;
        this.postroll = postroll;
        this.skipOffset = skipOffset;
        this.isRewarded = isRewarded;
        this.maxVideoFileSize = maxVideoFileSize;
        this.shouldValidateSchema = shouldValidateSchema;
        this.actionText = actionText;
        this.endCardHtml = endCardHtml;
        this.downloadTimeout = downloadTimeout;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
        if (loaded) {
            this.listener.vastReady();
        }
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public long getMaxFileSize() {
        return (long) (Float.parseFloat(this.maxVideoFileSize) * 1000000.0f);
    }

    public void loadVastResponseViaURL(final String urlString) {
        this.vastModel = null;
        if (NetworkTools.connectedToInternet(this.context)) {
            new Thread(new Runnable() {
                public void run() {
                    Throwable th;
                    BufferedReader bufferedReader = null;
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(urlString).openStream()));
                        try {
                            StringBuffer sb = new StringBuffer();
                            while (true) {
                                String line = in.readLine();
                                if (line == null) {
                                    break;
                                }
                                sb.append(line).append(System.getProperty("line.separator"));
                            }
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                }
                            }
                            VASTPlayer.this.loadVastResponseViaXML(sb.toString());
                            bufferedReader = in;
                        } catch (Exception e2) {
                            bufferedReader = in;
                            try {
                                VASTPlayer.this.sendError(VASTPlayer.ERROR_XML_PARSE);
                                if (bufferedReader != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (IOException e3) {
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (bufferedReader != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (IOException e4) {
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            bufferedReader = in;
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            throw th;
                        }
                    } catch (Exception e5) {
                        VASTPlayer.this.sendError(VASTPlayer.ERROR_XML_PARSE);
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    }
                }
            }).start();
        } else {
            sendError(ERROR_NO_NETWORK);
        }
    }

    public void loadVastResponseViaXML(final String xmlData) {
        this.vastModel = null;
        if (NetworkTools.connectedToInternet(this.context)) {
            new Thread(new Runnable() {
                public void run() {
                    VASTProcessor processor = new VASTProcessor(new DefaultMediaPicker(VASTPlayer.this.context), VASTPlayer.this);
                    int error = processor.process(VASTPlayer.this.context, xmlData, VASTPlayer.this.shouldValidateSchema, VASTPlayer.this.downloadTimeout);
                    if (error == 0) {
                        VASTPlayer.this.vastModel = processor.getModel();
                    } else {
                        VASTPlayer.this.sendError(error);
                    }
                }
            }).start();
        } else {
            sendError(ERROR_NO_NETWORK);
        }
    }

    public void play() {
        if (this.vastModel != null) {
            currentPlayer = this;
            Intent vastPlayerIntent = new Intent(this.context, VASTActivity.class);
            vastPlayerIntent.putExtra("com.nexage.android.vast.player.vastModel", this.vastModel);
            vastPlayerIntent.putExtra("postroll", this.postroll);
            vastPlayerIntent.putExtra("endCardHtml", this.endCardHtml);
            String xmlSkipOffset = this.vastModel.getSkipOffset();
            String[] components = xmlSkipOffset != null ? xmlSkipOffset.split(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR) : new String[ERROR_NONE];
            if (components.length == 3) {
                try {
                    long timeoutMs = (long) (((3600000 * Integer.parseInt(components[ERROR_NONE])) + (60000 * Integer.parseInt(components[ERROR_NO_NETWORK]))) + (Integer.parseInt(components[2]) * 1000));
                    FuseLog.v(TAG, "Overriding server sent skip offset with VAST offset from XML: " + timeoutMs);
                    this.skipOffset = timeoutMs;
                } catch (NumberFormatException e) {
                    FuseLog.i(TAG, "Could not parse skip offset from xml: " + xmlSkipOffset + ", using cb_ms instead");
                }
            }
            vastPlayerIntent.putExtra("skipOffset", this.skipOffset);
            vastPlayerIntent.putExtra("rewarded", this.isRewarded);
            vastPlayerIntent.putExtra("actionText", this.actionText);
            this.context.startActivity(vastPlayerIntent);
            return;
        }
        FuseLog.d(TAG, "vastModel is null; nothing to play");
    }

    private void sendError(final int error) {
        if (this.listener != null) {
            ((Activity) this.context).runOnUiThread(new Runnable() {
                public void run() {
                    VASTPlayer.this.listener.vastError(error);
                }
            });
        }
    }
}
