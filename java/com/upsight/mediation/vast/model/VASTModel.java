package com.upsight.mediation.vast.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.util.XmlTools;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spacemadness.com.lunarconsole.BuildConfig;

public class VASTModel implements Serializable {
    public static final long DOWNLOAD_TIMEOUT_LIMIT = 30000;
    private static String TAG = "VASTModel";
    private static final String adSystemXPATH = "/VAST/Ad/InLine/AdSystem";
    private static final String adTitleXPATH = "/VAST/Ad/InLine/AdTitle";
    private static final String combinedTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String durationXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/Duration";
    private static final String errorUrlXPATH = "//Error";
    private static final String impressionXPATH = "/VAST/Ad/InLine/Impression";
    private static final String inlineLinearTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String inlineNonLinearTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String mediaFileXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/MediaFiles/MediaFile";
    private static final long serialVersionUID = 4318368258447283733L;
    private static final String skipOffsetXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear[@skipoffset]";
    private static final String vastXPATH = "//VAST";
    private static final String videoClicksXPATH = "//VideoClicks";
    private static final String wrapperLinearTrackingXPATH = "/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String wrapperNonLinearTrackingXPATH = "/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private String mediaFileDeliveryType = null;
    private String mediaFileLocation = null;
    private transient Document vastsDocument;

    private class DownloadTask extends AsyncTask<String, Void, Integer> {
        private final Context context;
        private final long downloadTimeout;
        private final VASTPlayer mVastPlayer;

        public DownloadTask(VASTPlayer vastPlayer, Context context, int downloadTimeout) {
            this.mVastPlayer = vastPlayer;
            this.context = context;
            this.downloadTimeout = downloadTimeout > 0 ? (long) downloadTimeout : VASTModel.DOWNLOAD_TIMEOUT_LIMIT;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.Integer doInBackground(java.lang.String... r27) {
            /*
            r26 = this;
            r15 = 0;
            r16 = 0;
            r3 = 0;
            r9 = 0;
            r18 = java.lang.System.nanoTime();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r20 = new java.net.URL;	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = 0;
            r21 = r27[r21];	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r20.<init>(r21);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = 0;
            r21 = r27[r21];	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = 0;
            r22 = r27[r22];	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r23 = 47;
            r22 = r22.lastIndexOf(r23);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = r22 + 1;
            r23 = 0;
            r23 = r27[r23];	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r23 = r23.length();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r14 = r21.substring(r22, r23);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = r20.openConnection();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r0 = r21;
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r3 = r0;
            r21 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r0 = r21;
            r3.setConnectTimeout(r0);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r3.connect();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r13 = r3.getContentLength();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = r3.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            r0 = r21;
            r1 = r22;
            if (r0 == r1) goto L_0x0093;
        L_0x0051:
            r21 = com.upsight.mediation.vast.model.VASTModel.TAG;	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22.<init>();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r23 = "Server returned HTTP ";
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r23 = r3.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r23 = " ";
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r23 = r3.getResponseMessage();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = r22.append(r23);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = r22.toString();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            com.upsight.mediation.log.FuseLog.d(r21, r22);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            if (r16 == 0) goto L_0x0088;
        L_0x0085:
            r16.close();	 Catch:{ IOException -> 0x0257 }
        L_0x0088:
            if (r15 == 0) goto L_0x008d;
        L_0x008a:
            r15.close();	 Catch:{ IOException -> 0x0257 }
        L_0x008d:
            if (r3 == 0) goto L_0x0092;
        L_0x008f:
            r3.disconnect();
        L_0x0092:
            return r21;
        L_0x0093:
            r2 = new java.io.File;	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r0 = r26;
            r0 = r0.context;	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = r0;
            r21 = r21.getCacheDir();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r22 = "fuse_vast_cache";
            r0 = r21;
            r1 = r22;
            r2.<init>(r0, r1);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = r2.exists();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            if (r21 != 0) goto L_0x00ca;
        L_0x00ae:
            r21 = r2.mkdir();	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            if (r21 != 0) goto L_0x00ca;
        L_0x00b4:
            r21 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            if (r16 == 0) goto L_0x00bf;
        L_0x00bc:
            r16.close();	 Catch:{ IOException -> 0x0254 }
        L_0x00bf:
            if (r15 == 0) goto L_0x00c4;
        L_0x00c1:
            r15.close();	 Catch:{ IOException -> 0x0254 }
        L_0x00c4:
            if (r3 == 0) goto L_0x0092;
        L_0x00c6:
            r3.disconnect();
            goto L_0x0092;
        L_0x00ca:
            r12 = new java.io.File;	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r12.<init>(r2, r14);	 Catch:{ SocketTimeoutException -> 0x0240, Exception -> 0x0200 }
            r21 = r12.exists();	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            if (r21 == 0) goto L_0x0104;
        L_0x00d5:
            r22 = r12.length();	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r0 = (long) r13;	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r24 = r0;
            r21 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
            if (r21 != 0) goto L_0x0104;
        L_0x00e0:
            r0 = r26;
            r0 = com.upsight.mediation.vast.model.VASTModel.this;	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r21 = r0;
            r22 = r12.getAbsolutePath();	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r21.mediaFileLocation = r22;	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r21 = 0;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            if (r16 == 0) goto L_0x00f8;
        L_0x00f5:
            r16.close();	 Catch:{ IOException -> 0x0251 }
        L_0x00f8:
            if (r15 == 0) goto L_0x00fd;
        L_0x00fa:
            r15.close();	 Catch:{ IOException -> 0x0251 }
        L_0x00fd:
            if (r3 == 0) goto L_0x0102;
        L_0x00ff:
            r3.disconnect();
        L_0x0102:
            r9 = r12;
            goto L_0x0092;
        L_0x0104:
            r0 = (long) r13;
            r22 = r0;
            r0 = r26;
            r0 = r0.mVastPlayer;	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r21 = r0;
            r24 = r21.getMaxFileSize();	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r21 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
            if (r21 <= 0) goto L_0x012d;
        L_0x0115:
            r21 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            if (r16 == 0) goto L_0x0120;
        L_0x011d:
            r16.close();	 Catch:{ IOException -> 0x024e }
        L_0x0120:
            if (r15 == 0) goto L_0x0125;
        L_0x0122:
            r15.close();	 Catch:{ IOException -> 0x024e }
        L_0x0125:
            if (r3 == 0) goto L_0x012a;
        L_0x0127:
            r3.disconnect();
        L_0x012a:
            r9 = r12;
            goto L_0x0092;
        L_0x012d:
            r15 = r3.getInputStream();	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r17 = new java.io.FileOutputStream;	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r0 = r17;
            r0.<init>(r12);	 Catch:{ SocketTimeoutException -> 0x0243, Exception -> 0x0235, all -> 0x022b }
            r21 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            r0 = r21;
            r5 = new byte[r0];	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
        L_0x013e:
            r4 = r15.read(r5);	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            r21 = -1;
            r0 = r21;
            if (r4 == r0) goto L_0x01ae;
        L_0x0148:
            r10 = java.lang.System.nanoTime();	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            r22 = r10 - r18;
            r24 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
            r6 = r22 / r24;
            r0 = r26;
            r0 = r0.downloadTimeout;	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            r22 = r0;
            r21 = (r6 > r22 ? 1 : (r6 == r22 ? 0 : -1));
            if (r21 < 0) goto L_0x0166;
        L_0x015d:
            r21 = 1;
            r0 = r26;
            r1 = r21;
            r0.cancel(r1);	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
        L_0x0166:
            r21 = r26.isCancelled();	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            if (r21 == 0) goto L_0x0189;
        L_0x016c:
            r15.close();	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            r21 = 402; // 0x192 float:5.63E-43 double:1.986E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            if (r17 == 0) goto L_0x017a;
        L_0x0177:
            r17.close();	 Catch:{ IOException -> 0x024b }
        L_0x017a:
            if (r15 == 0) goto L_0x017f;
        L_0x017c:
            r15.close();	 Catch:{ IOException -> 0x024b }
        L_0x017f:
            if (r3 == 0) goto L_0x0184;
        L_0x0181:
            r3.disconnect();
        L_0x0184:
            r9 = r12;
            r16 = r17;
            goto L_0x0092;
        L_0x0189:
            r21 = 0;
            r0 = r17;
            r1 = r21;
            r0.write(r5, r1, r4);	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            goto L_0x013e;
        L_0x0193:
            r8 = move-exception;
            r9 = r12;
            r16 = r17;
        L_0x0197:
            r21 = 402; // 0x192 float:5.63E-43 double:1.986E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ all -> 0x0218 }
            if (r16 == 0) goto L_0x01a2;
        L_0x019f:
            r16.close();	 Catch:{ IOException -> 0x023d }
        L_0x01a2:
            if (r15 == 0) goto L_0x01a7;
        L_0x01a4:
            r15.close();	 Catch:{ IOException -> 0x023d }
        L_0x01a7:
            if (r3 == 0) goto L_0x0092;
        L_0x01a9:
            r3.disconnect();
            goto L_0x0092;
        L_0x01ae:
            r21 = r12.exists();	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            if (r21 == 0) goto L_0x01bf;
        L_0x01b4:
            r22 = r12.length();	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            r0 = (long) r13;	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            r24 = r0;
            r21 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
            if (r21 == 0) goto L_0x01d9;
        L_0x01bf:
            r21 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ SocketTimeoutException -> 0x0193, Exception -> 0x0238, all -> 0x022e }
            if (r17 == 0) goto L_0x01ca;
        L_0x01c7:
            r17.close();	 Catch:{ IOException -> 0x0249 }
        L_0x01ca:
            if (r15 == 0) goto L_0x01cf;
        L_0x01cc:
            r15.close();	 Catch:{ IOException -> 0x0249 }
        L_0x01cf:
            if (r3 == 0) goto L_0x01d4;
        L_0x01d1:
            r3.disconnect();
        L_0x01d4:
            r9 = r12;
            r16 = r17;
            goto L_0x0092;
        L_0x01d9:
            if (r17 == 0) goto L_0x01de;
        L_0x01db:
            r17.close();	 Catch:{ IOException -> 0x0247 }
        L_0x01de:
            if (r15 == 0) goto L_0x01e3;
        L_0x01e0:
            r15.close();	 Catch:{ IOException -> 0x0247 }
        L_0x01e3:
            if (r3 == 0) goto L_0x01e8;
        L_0x01e5:
            r3.disconnect();
        L_0x01e8:
            r0 = r26;
            r0 = com.upsight.mediation.vast.model.VASTModel.this;
            r21 = r0;
            r22 = r12.getAbsolutePath();
            r21.mediaFileLocation = r22;
            r21 = 0;
            r21 = java.lang.Integer.valueOf(r21);
            r9 = r12;
            r16 = r17;
            goto L_0x0092;
        L_0x0200:
            r8 = move-exception;
        L_0x0201:
            r21 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r21 = java.lang.Integer.valueOf(r21);	 Catch:{ all -> 0x0218 }
            if (r16 == 0) goto L_0x020c;
        L_0x0209:
            r16.close();	 Catch:{ IOException -> 0x0233 }
        L_0x020c:
            if (r15 == 0) goto L_0x0211;
        L_0x020e:
            r15.close();	 Catch:{ IOException -> 0x0233 }
        L_0x0211:
            if (r3 == 0) goto L_0x0092;
        L_0x0213:
            r3.disconnect();
            goto L_0x0092;
        L_0x0218:
            r21 = move-exception;
        L_0x0219:
            if (r16 == 0) goto L_0x021e;
        L_0x021b:
            r16.close();	 Catch:{ IOException -> 0x0229 }
        L_0x021e:
            if (r15 == 0) goto L_0x0223;
        L_0x0220:
            r15.close();	 Catch:{ IOException -> 0x0229 }
        L_0x0223:
            if (r3 == 0) goto L_0x0228;
        L_0x0225:
            r3.disconnect();
        L_0x0228:
            throw r21;
        L_0x0229:
            r22 = move-exception;
            goto L_0x0223;
        L_0x022b:
            r21 = move-exception;
            r9 = r12;
            goto L_0x0219;
        L_0x022e:
            r21 = move-exception;
            r9 = r12;
            r16 = r17;
            goto L_0x0219;
        L_0x0233:
            r22 = move-exception;
            goto L_0x0211;
        L_0x0235:
            r8 = move-exception;
            r9 = r12;
            goto L_0x0201;
        L_0x0238:
            r8 = move-exception;
            r9 = r12;
            r16 = r17;
            goto L_0x0201;
        L_0x023d:
            r22 = move-exception;
            goto L_0x01a7;
        L_0x0240:
            r8 = move-exception;
            goto L_0x0197;
        L_0x0243:
            r8 = move-exception;
            r9 = r12;
            goto L_0x0197;
        L_0x0247:
            r21 = move-exception;
            goto L_0x01e3;
        L_0x0249:
            r22 = move-exception;
            goto L_0x01cf;
        L_0x024b:
            r22 = move-exception;
            goto L_0x017f;
        L_0x024e:
            r22 = move-exception;
            goto L_0x0125;
        L_0x0251:
            r22 = move-exception;
            goto L_0x00fd;
        L_0x0254:
            r22 = move-exception;
            goto L_0x00c4;
        L_0x0257:
            r22 = move-exception;
            goto L_0x008d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.vast.model.VASTModel.DownloadTask.doInBackground(java.lang.String[]):java.lang.Integer");
        }

        protected void onPostExecute(Integer error) {
            if (error.intValue() != 0) {
                this.mVastPlayer.listener.vastError(error.intValue());
                return;
            }
            FuseLog.v(VASTModel.TAG, "on execute complete");
            this.mVastPlayer.setLoaded(true);
        }

        protected void onCancelled() {
            this.mVastPlayer.listener.vastError(VASTPlayer.ERROR_VIDEO_TIMEOUT);
        }
    }

    public VASTModel(Document vasts) {
        this.vastsDocument = vasts;
    }

    public boolean evaluateAdSystem() {
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().evaluate(adSystemXPATH, this.vastsDocument, XPathConstants.NODE);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    public boolean evaluateAdTitle() {
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().evaluate(adTitleXPATH, this.vastsDocument, XPathConstants.NODE);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    public String getVastVersion() {
        try {
            return ((Node) XPathFactory.newInstance().newXPath().evaluate(vastXPATH, this.vastsDocument, XPathConstants.NODE)).getAttributes().getNamedItem("version").getNodeValue().toString();
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.HashMap<com.upsight.mediation.vast.model.TRACKING_EVENTS_TYPE, java.util.List<com.upsight.mediation.vast.model.VASTTracking>> getTrackingEvents() {
        /*
        r18 = this;
        r12 = new java.util.HashMap;
        r12.<init>();
        r15 = javax.xml.xpath.XPathFactory.newInstance();
        r14 = r15.newXPath();
        r15 = "/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
        r0 = r18;
        r0 = r0.vastsDocument;	 Catch:{ Exception -> 0x0090 }
        r16 = r0;
        r17 = javax.xml.xpath.XPathConstants.NODESET;	 Catch:{ Exception -> 0x0090 }
        r7 = r14.evaluate(r15, r16, r17);	 Catch:{ Exception -> 0x0090 }
        r7 = (org.w3c.dom.NodeList) r7;	 Catch:{ Exception -> 0x0090 }
        r9 = "";
        r5 = 0;
        if (r7 == 0) goto L_0x0092;
    L_0x0022:
        r4 = 0;
    L_0x0023:
        r15 = r7.getLength();	 Catch:{ Exception -> 0x0090 }
        if (r4 >= r15) goto L_0x0092;
    L_0x0029:
        r6 = r7.item(r4);	 Catch:{ Exception -> 0x0090 }
        r1 = r6.getAttributes();	 Catch:{ Exception -> 0x0090 }
        r15 = "event";
        r15 = r1.getNamedItem(r15);	 Catch:{ Exception -> 0x0090 }
        r3 = r15.getNodeValue();	 Catch:{ Exception -> 0x0090 }
        r15 = "progress";
        r15 = r3.equals(r15);	 Catch:{ Exception -> 0x0090 }
        if (r15 == 0) goto L_0x004d;
    L_0x0043:
        r15 = "offset";
        r15 = r1.getNamedItem(r15);	 Catch:{ NullPointerException -> 0x007f }
        r9 = r15.getNodeValue();	 Catch:{ NullPointerException -> 0x007f }
    L_0x004d:
        r5 = com.upsight.mediation.vast.model.TRACKING_EVENTS_TYPE.valueOf(r3);	 Catch:{ IllegalArgumentException -> 0x0082 }
        r11 = com.upsight.mediation.vast.util.XmlTools.getElementValue(r6);	 Catch:{ Exception -> 0x0090 }
        r13 = new com.upsight.mediation.vast.model.VASTTracking;	 Catch:{ Exception -> 0x0090 }
        r13.<init>();	 Catch:{ Exception -> 0x0090 }
        r13.setEvent(r5);	 Catch:{ Exception -> 0x0090 }
        r13.setValue(r11);	 Catch:{ Exception -> 0x0090 }
        r15 = com.upsight.mediation.vast.model.TRACKING_EVENTS_TYPE.progress;	 Catch:{ Exception -> 0x0090 }
        r15 = r5.equals(r15);	 Catch:{ Exception -> 0x0090 }
        if (r15 == 0) goto L_0x006d;
    L_0x0068:
        if (r9 == 0) goto L_0x006d;
    L_0x006a:
        r13.setOffset(r9);	 Catch:{ Exception -> 0x0090 }
    L_0x006d:
        r15 = r12.containsKey(r5);	 Catch:{ Exception -> 0x0090 }
        if (r15 == 0) goto L_0x0084;
    L_0x0073:
        r10 = r12.get(r5);	 Catch:{ Exception -> 0x0090 }
        r10 = (java.util.List) r10;	 Catch:{ Exception -> 0x0090 }
        r10.add(r13);	 Catch:{ Exception -> 0x0090 }
    L_0x007c:
        r4 = r4 + 1;
        goto L_0x0023;
    L_0x007f:
        r8 = move-exception;
        r9 = 0;
        goto L_0x004d;
    L_0x0082:
        r2 = move-exception;
        goto L_0x007c;
    L_0x0084:
        r10 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0090 }
        r10.<init>();	 Catch:{ Exception -> 0x0090 }
        r10.add(r13);	 Catch:{ Exception -> 0x0090 }
        r12.put(r5, r10);	 Catch:{ Exception -> 0x0090 }
        goto L_0x007c;
    L_0x0090:
        r2 = move-exception;
        r12 = 0;
    L_0x0092:
        return r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.vast.model.VASTModel.getTrackingEvents():java.util.HashMap<com.upsight.mediation.vast.model.TRACKING_EVENTS_TYPE, java.util.List<com.upsight.mediation.vast.model.VASTTracking>>");
    }

    public List<VASTMediaFile> getMediaFiles() {
        ArrayList<VASTMediaFile> mediaFiles = new ArrayList();
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(mediaFileXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodes == null) {
                return mediaFiles;
            }
            for (int i = 0; i < nodes.getLength(); i++) {
                BigInteger bigInteger;
                VASTMediaFile mediaFile = new VASTMediaFile();
                Node node = nodes.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node attributeNode = attributes.getNamedItem("apiFramework");
                mediaFile.setApiFramework(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("bitrate");
                if (attributeNode == null) {
                    bigInteger = null;
                } else {
                    bigInteger = new BigInteger(attributeNode.getNodeValue());
                }
                mediaFile.setBitrate(bigInteger);
                attributeNode = attributes.getNamedItem("delivery");
                mediaFile.setDelivery(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("height");
                mediaFile.setHeight(attributeNode == null ? null : new BigInteger(attributeNode.getNodeValue()));
                attributeNode = attributes.getNamedItem(TriggerIfContentAvailable.ID);
                mediaFile.setId(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("maintainAspectRatio");
                mediaFile.setMaintainAspectRatio(attributeNode == null ? null : Boolean.valueOf(attributeNode.getNodeValue()));
                attributeNode = attributes.getNamedItem("scalable");
                mediaFile.setScalable(attributeNode == null ? null : Boolean.valueOf(attributeNode.getNodeValue()));
                attributeNode = attributes.getNamedItem(Keys.TYPE);
                mediaFile.setType(attributeNode == null ? null : attributeNode.getNodeValue());
                attributeNode = attributes.getNamedItem("width");
                mediaFile.setWidth(attributeNode == null ? null : new BigInteger(attributeNode.getNodeValue()));
                mediaFile.setValue(XmlTools.getElementValue(node));
                mediaFiles.add(mediaFile);
            }
            return mediaFiles;
        } catch (Exception e) {
            return null;
        }
    }

    public int cache(final Context c, final VASTPlayer vastPlayer, final int downloadTimeout) {
        ((Activity) c).runOnUiThread(new Runnable() {
            public void run() {
                new DownloadTask(vastPlayer, c, downloadTimeout).execute(new String[]{VASTModel.this.mediaFileLocation});
            }
        });
        return 0;
    }

    public String getDuration() {
        FuseLog.d(TAG, "getDuration");
        String duration = null;
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(durationXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    duration = XmlTools.getElementValue(nodes.item(i));
                }
            }
            return duration;
        } catch (Exception e) {
            return null;
        }
    }

    public String getSkipOffset() {
        try {
            return ((Node) XPathFactory.newInstance().newXPath().evaluate(skipOffsetXPATH, this.vastsDocument, XPathConstants.NODE)).getAttributes().getNamedItem("skipoffset").getNodeValue().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public VideoClicks getVideoClicks() {
        VideoClicks videoClicks = new VideoClicks();
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(videoClicksXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodes == null) {
                return videoClicks;
            }
            for (int i = 0; i < nodes.getLength(); i++) {
                NodeList childNodes = nodes.item(i).getChildNodes();
                for (int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
                    Node child = childNodes.item(childIndex);
                    String nodeName = child.getNodeName();
                    if (nodeName.equalsIgnoreCase("ClickTracking")) {
                        videoClicks.getClickTracking().add(XmlTools.getElementValue(child));
                    } else if (nodeName.equalsIgnoreCase("ClickThrough")) {
                        videoClicks.setClickThrough(XmlTools.getElementValue(child));
                    } else if (nodeName.equalsIgnoreCase("CustomClick")) {
                        videoClicks.getCustomClick().add(XmlTools.getElementValue(child));
                    }
                }
            }
            return videoClicks;
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getImpressions() {
        return getListFromXPath(impressionXPATH);
    }

    public List<String> getErrorUrl() {
        return getListFromXPath(errorUrlXPATH);
    }

    private List<String> getListFromXPath(String xPath) {
        ArrayList<String> list = new ArrayList();
        try {
            NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate(xPath, this.vastsDocument, XPathConstants.NODESET);
            if (nodes == null) {
                return list;
            }
            for (int i = 0; i < nodes.getLength(); i++) {
                String value = XmlTools.getElementValue(nodes.item(i));
                if (value != null || !value.equals(BuildConfig.FLAVOR)) {
                    list.add(value);
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(XmlTools.xmlDocumentToString(this.vastsDocument));
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.vastsDocument = XmlTools.stringToDocument((String) ois.readObject());
    }

    public void setPickedMediaFileLocation(String mediaFileLocation) {
        this.mediaFileLocation = mediaFileLocation;
    }

    public String getPickedMediaFileLocation() {
        return this.mediaFileLocation;
    }

    public void setPickedMediaFileDeliveryType(String deliveryType) {
        this.mediaFileDeliveryType = deliveryType;
    }

    public String getPickedMediaFileDeliveryType() {
        return this.mediaFileDeliveryType;
    }
}
