package com.nianticproject.holoholo.sfida.service;

import android.os.Handler;
import android.util.Log;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants.CertificationState;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants.SfidaVersion;
import com.nianticproject.holoholo.sfida.service.SfidaWatchDog.OnTimeoutListener;
import java.util.UUID;
import spacemadness.com.lunarconsole.R;

public class Certificator {
    private static final int DELAY_TIME = 1000;
    public final String TAG = Certificator.class.getSimpleName();
    private volatile CertificationState certificationState = CertificationState.NO_CERTIFICATION;
    Handler delayHandler = new Handler();
    public SfidaService sfidaService;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$CertificationState = new int[CertificationState.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion = new int[SfidaVersion.values().length];

        static {
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.ALPHA_NO_SEC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.ALPHA_SEC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.BETA1.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.BETA4.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$CertificationState[CertificationState.ENABLE_SECURITY_SERVICE_NOTIFY.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$CertificationState[CertificationState.DUMMY_CERTIFICATION_CHALLENGE_1.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$CertificationState[CertificationState.DUMMY_CERTIFICATION_CHALLENGE_2.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$CertificationState[CertificationState.DUMMY_CERTIFICATION_CHALLENGE_3.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$CertificationState[CertificationState.CERTIFICATION_COMPLETE.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    private class CertificateRunnable implements Runnable {
        Handler delayHandler;
        CertificationState executeCertificationState;

        public CertificateRunnable(Handler handler, CertificationState certificationState) {
            this.delayHandler = handler;
            this.executeCertificationState = certificationState;
        }

        public void run() {
            Certificator.this.setCertificationState(this.executeCertificationState);
            switch (AnonymousClass1.$SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$CertificationState[this.executeCertificationState.ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    onReceivedSecurityServiceNotify();
                    return;
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                    onReceivedDummyCertificationChallenge1();
                    return;
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    onReceivedDummyCertificationChallenge2();
                    return;
                case 4:
                    onReceivedDummyCertificationChallenge3();
                    return;
                case 5:
                    onReceivedCertificationComplete();
                    return;
                default:
                    return;
            }
        }

        private void onReceivedSecurityServiceNotify() {
            switch (AnonymousClass1.$SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaConstants.SFIDA_VERSION.ordinal()]) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    Certificator.this.sfidaService.enableDeviceControlServiceNotify();
                    return;
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                case 4:
                    if (!Certificator.this.sfidaService.enableSecurityServiceNotify(new OnTimeoutListener() {
                        public void onTimeout(UUID uuid) {
                            if (Certificator.this.getCertificationState() == CertificationState.ENABLE_SECURITY_SERVICE_NOTIFY) {
                                Certificator.this.executeCertificateSequence(CertificationState.ENABLE_SECURITY_SERVICE_NOTIFY);
                            } else {
                                Log.d(Certificator.this.TAG, "Ignoring timeout event.");
                            }
                        }

                        public void reachedRetryCountMax() {
                            Log.d(Certificator.this.TAG, "reachedRetryCountMax()");
                            Certificator.this.sfidaService.disconnectBluetooth();
                        }
                    })) {
                        Certificator.this.sfidaService.disconnectBluetooth();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private void onReceivedDummyCertificationChallenge1() {
            Log.d(Certificator.this.TAG, "Dummy Certification Challenge 1");
            if (Certificator.this.sfidaService.getIsReceivedNotifyCallback()) {
                Certificator.this.sfidaService.sendCertificateMessage(SfidaMessage.getSecurityResponseForDebug());
            } else {
                retry();
            }
        }

        private void onReceivedDummyCertificationChallenge2() {
            Log.d(Certificator.this.TAG, "Dummy Certification Challenge 2");
            if (Certificator.this.sfidaService.getIsReceivedWriteCallback()) {
                switch (AnonymousClass1.$SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaConstants.SFIDA_VERSION.ordinal()]) {
                    case R.styleable.LoadingImageView_circleCrop /*2*/:
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Certificator.this.sfidaService.onCertificationComplete();
                            }
                        }, 500);
                        return;
                    case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                    case 4:
                        Certificator.this.sfidaService.sendCertificateMessage(SfidaMessage.getSecurityResponseForDebug2());
                        return;
                    default:
                        return;
                }
            }
            retry();
        }

        private void onReceivedDummyCertificationChallenge3() {
            Log.d(Certificator.this.TAG, "Dummy Certification Challenge 3");
            if (Certificator.this.sfidaService.getIsReceivedWriteCallback()) {
                switch (AnonymousClass1.$SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaConstants.SFIDA_VERSION.ordinal()]) {
                    case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                        Certificator.this.sfidaService.sendCertificateMessage(SfidaMessage.getSecurityResponseForDebug3());
                        this.delayHandler.postDelayed(new Runnable() {
                            public void run() {
                                Certificator.this.sfidaService.onCertificationComplete();
                            }
                        }, 500);
                        return;
                    case 4:
                        Certificator.this.sfidaService.sendCertificateMessage(SfidaMessage.getSecurityResponseForDebug3());
                        return;
                    default:
                        return;
                }
            }
            retry();
        }

        private void onReceivedCertificationComplete() {
            Log.d(Certificator.this.TAG, "Certification Complete!");
            Certificator.this.sfidaService.onCertificationComplete();
        }

        private void retry() {
            if (this.delayHandler != null) {
                Log.d(Certificator.this.TAG, "Callback was not received. Retry after.");
                this.delayHandler.postDelayed(this, 1000);
            }
        }
    }

    public Certificator(SfidaService sfidaService) {
        this.sfidaService = sfidaService;
    }

    public void setCertificationState(CertificationState certificationState) {
        Log.d(this.TAG, "CertificationState [" + this.certificationState + "] \u2192 [" + certificationState + "]");
        this.certificationState = certificationState;
    }

    public CertificationState getCertificationState() {
        return this.certificationState;
    }

    public void startCertification() {
        if (this.delayHandler != null) {
            this.delayHandler.postDelayed(new CertificateRunnable(this.delayHandler, CertificationState.ENABLE_SECURITY_SERVICE_NOTIFY), 1000);
        }
    }

    public void onSfidaUpdated(String receivedData) {
        if (SfidaMessage.SFIDA_RESPONSE_CERTIFICATION_NOTIFY.equals(receivedData)) {
            executeCertificateSequence(CertificationState.DUMMY_CERTIFICATION_CHALLENGE_1);
        } else if (SfidaMessage.SFIDA_RESPONSE_CERTIFICATION_CHALLENGE_1.equals(receivedData)) {
            executeCertificateSequence(CertificationState.DUMMY_CERTIFICATION_CHALLENGE_2);
        } else if (SfidaMessage.SFIDA_RESPONSE_CERTIFICATION_CHALLENGE_2.equals(receivedData)) {
            executeCertificateSequence(CertificationState.DUMMY_CERTIFICATION_CHALLENGE_3);
        } else if (SfidaMessage.SFIDA_RESPONSE_CERTIFICATION_COMPLETE.equals(receivedData)) {
            executeCertificateSequence(CertificationState.CERTIFICATION_COMPLETE);
        } else if (receivedData == null) {
            Log.d(this.TAG, "onSfidaUpdated() rawString was null.");
        } else {
            Log.d(this.TAG, "onSfidaUpdated() Unhandled raw data : " + receivedData);
        }
    }

    private void executeCertificateSequence(CertificationState certificationState) {
        if (this.delayHandler != null) {
            this.delayHandler.postDelayed(new CertificateRunnable(this.delayHandler, certificationState), 1000);
        }
    }
}
