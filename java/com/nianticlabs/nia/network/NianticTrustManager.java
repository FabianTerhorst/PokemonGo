package com.nianticlabs.nia.network;

import android.content.Context;
import com.nianticlabs.nia.contextservice.ContextService;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class NianticTrustManager extends ContextService implements X509TrustManager {
    private native void nativeCheckClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException;

    private native void nativeCheckServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException;

    private native X509Certificate[] nativeGetAcceptedIssuers();

    public NianticTrustManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        synchronized (this.callbackLock) {
            nativeCheckServerTrusted(chain, authType);
        }
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        synchronized (this.callbackLock) {
            nativeCheckServerTrusted(chain, authType);
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] nativeGetAcceptedIssuers;
        synchronized (this.callbackLock) {
            nativeGetAcceptedIssuers = nativeGetAcceptedIssuers();
        }
        return nativeGetAcceptedIssuers;
    }

    public static X509TrustManager getTrustManager(KeyStore keystore) {
        return getTrustManager(TrustManagerFactory.getDefaultAlgorithm(), keystore);
    }

    public static X509TrustManager getTrustManager(String algorithm, KeyStore keystore) {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
            factory.init(keystore);
            for (TrustManager tm : factory.getTrustManagers()) {
                if (tm != null && (tm instanceof X509TrustManager)) {
                    return (X509TrustManager) tm;
                }
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyStoreException e2) {
        }
        return null;
    }
}
