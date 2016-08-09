package com.nianticlabs.nia.contextservice;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.R;

public class GoogleApiManager {
    private static final boolean ENABLE_VERBOSE_LOGS = true;
    private static final String TAG = "GoogleApiManager";
    private AppState appState;
    private final ConnectionCallbacks connectionCallbacks;
    private final OnConnectionFailedListener connectionFailedListener;
    private final Builder googleApiBuilder;
    private GoogleApiClient googleApiClient;
    private Listener listener;
    private State state;

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$AppState = new int[AppState.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$State = new int[State.values().length];

        static {
            try {
                $SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$AppState[AppState.STOP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$State[State.STOPPED.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$State[State.STOP_FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$State[State.START_FAILED.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$State[State.STARTED.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private enum AppState {
        START,
        STOP,
        PAUSE,
        RESUME
    }

    public interface Listener {
        void onConnected();

        void onConnectionFailed(ConnectionResult connectionResult);

        void onDisconnected();
    }

    private enum State {
        STARTING,
        START_FAILED,
        STARTED,
        STOPPING,
        STOP_FAILED,
        STOPPED
    }

    public GoogleApiManager(Context context) {
        this.state = State.STOPPED;
        this.appState = AppState.STOP;
        this.listener = null;
        this.googleApiClient = null;
        this.connectionCallbacks = new ConnectionCallbacks() {
            public void onConnected(Bundle bundle) {
                ContextService.assertOnServiceThread();
                Log.v(GoogleApiManager.TAG, "onConnected");
                GoogleApiManager.this.requestStateStarted();
            }

            public void onConnectionSuspended(int cause) {
                ContextService.assertOnServiceThread();
                Log.v(GoogleApiManager.TAG, "onConnectionSuspended");
                if (Log.isLoggable(GoogleApiManager.TAG, 3)) {
                    StringBuilder sb = new StringBuilder("Connection to Google Play Services suspended. ");
                    switch (cause) {
                        case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            sb.append("CAUSE_SERVICE_DISCONNECTED");
                            break;
                        case R.styleable.LoadingImageView_circleCrop /*2*/:
                            sb.append("CAUSE_NETWORK_LOST");
                            break;
                        default:
                            sb.append("Unknown (");
                            sb.append(cause);
                            sb.append(")");
                            break;
                    }
                    Log.d(GoogleApiManager.TAG, sb.toString());
                }
                Log.v(GoogleApiManager.TAG, "State " + GoogleApiManager.this.state.name() + " -> STOPPED");
                GoogleApiManager.this.state = State.STOPPING;
                GoogleApiManager.this.requestStateStopped();
            }
        };
        this.connectionFailedListener = new OnConnectionFailedListener() {
            public void onConnectionFailed(ConnectionResult connectionResult) {
                ContextService.assertOnServiceThread();
                GoogleApiManager.this.requestStateStartFailed(connectionResult);
            }
        };
        this.googleApiClient = this.googleApiClient;
        this.googleApiBuilder = new Builder(context).addConnectionCallbacks(this.connectionCallbacks).addOnConnectionFailedListener(this.connectionFailedListener).setHandler(ContextService.getServiceHandler());
    }

    public Builder builder() {
        if (this.googleApiClient == null) {
            return this.googleApiBuilder;
        }
        throw new IllegalStateException("Calling builder() after already built");
    }

    public void build() {
        if (this.googleApiClient != null) {
            throw new IllegalStateException("Calling build() after already built");
        }
        this.googleApiClient = this.googleApiBuilder.build();
    }

    public GoogleApiClient getClient() {
        return this.googleApiClient;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void onStart() {
        Log.v(TAG, "onStart " + this.appState.name() + " " + this.state.name());
        ContextService.assertOnServiceThread();
        this.appState = AppState.START;
        requestStateStarting();
    }

    public void onStop() {
        Log.v(TAG, "onStop " + this.appState.name() + " " + this.state.name());
        ContextService.assertOnServiceThread();
        this.appState = AppState.STOP;
        requestStateStopping();
    }

    public void onResume() {
        Log.v(TAG, "onResume " + this.appState.name() + " " + this.state.name());
        ContextService.assertOnServiceThread();
        this.appState = AppState.RESUME;
    }

    public void onPause() {
        Log.v(TAG, "onPause " + this.appState.name() + " " + this.state.name());
        ContextService.assertOnServiceThread();
        this.appState = AppState.PAUSE;
    }

    private void requestStateStarting() {
        Log.v(TAG, "requestStateStarting " + this.appState.name() + " " + this.state.name());
        if (this.appState != AppState.STOP) {
            switch (AnonymousClass3.$SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$State[this.state.ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    Log.v(TAG, "State " + this.state.name() + " -> STARTING");
                    this.state = State.STARTING;
                    this.googleApiClient.connect();
                    return;
                default:
                    return;
            }
        }
    }

    private void requestStateStartFailed(ConnectionResult connectionResult) {
        Log.v(TAG, "requestStateStartFailed " + this.appState.name() + " " + this.state.name());
        if (this.state == State.STARTING) {
            Log.v(TAG, "State " + this.state.name() + " -> START_FAILED");
            this.state = State.START_FAILED;
            if (this.listener != null) {
                this.listener.onConnectionFailed(connectionResult);
            }
        }
    }

    private void requestStateStarted() {
        Log.v(TAG, "requestStateStarted " + this.appState.name() + " " + this.state.name());
        if (this.state == State.STARTING) {
            Log.v(TAG, "State " + this.state.name() + " -> STARTED");
            this.state = State.STARTED;
            if (this.listener != null) {
                this.listener.onConnected();
            }
            switch (AnonymousClass3.$SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$AppState[this.appState.ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    requestStateStopping();
                    return;
                default:
                    return;
            }
        }
    }

    private void requestStateStopping() {
        Log.v(TAG, "requestStateStopping " + this.appState.name() + " " + this.state.name());
        switch (AnonymousClass3.$SwitchMap$com$nianticlabs$nia$contextservice$GoogleApiManager$State[this.state.ordinal()]) {
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                Log.v(TAG, "State " + this.state.name() + " -> STOPPING");
                this.state = State.STOPPING;
                this.googleApiClient.disconnect();
                requestStateStopped();
                return;
            default:
                return;
        }
    }

    private void requestStateStopped() {
        Log.v(TAG, "requestStateStopped " + this.appState.name() + " " + this.state.name());
        if (this.state == State.STOPPING) {
            Log.v(TAG, "State " + this.state.name() + " -> STOPPED");
            this.state = State.STOPPED;
            if (this.listener != null) {
                this.listener.onDisconnected();
            }
        }
    }
}
