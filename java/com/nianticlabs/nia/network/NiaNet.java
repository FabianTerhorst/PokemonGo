package com.nianticlabs.nia.network;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NiaNet {
    private static final int CHUNK_SIZE = 32768;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_OK = 200;
    private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final int METHOD_DELETE = 4;
    private static final int METHOD_GET = 0;
    private static final int METHOD_HEAD = 1;
    private static final int METHOD_OPTIONS = 5;
    private static final int METHOD_POST = 2;
    private static final int METHOD_PUT = 3;
    private static final int METHOD_TRACE = 6;
    private static final int NETWORK_TIMEOUT_MS = 15000;
    private static final int POOL_THREAD_NUM = 6;
    private static final String TAG = "NiaNet";
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(POOL_THREAD_NUM, 12, 5, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private static Set<Integer> pendingRequestIds = new HashSet();
    static ThreadLocal<ByteBuffer> readBuffer = new ThreadLocal<ByteBuffer>() {
        protected ByteBuffer initialValue() {
            return ByteBuffer.allocateDirect(NiaNet.CHUNK_SIZE);
        }
    };
    private static final ThreadLocal<byte[]> threadChunk = new ThreadLocal<byte[]>() {
        protected byte[] initialValue() {
            return new byte[NiaNet.CHUNK_SIZE];
        }
    };

    private static native void nativeCallback(long j, int i, String str, ByteBuffer byteBuffer, int i2, int i3);

    private static native void nativeSetupConnection(long j, HttpURLConnection httpURLConnection);

    public static void request(long object, int request_id, String url, int method, String headers, ByteBuffer body, int bodyOffset, int bodySize) {
        synchronized (pendingRequestIds) {
            pendingRequestIds.add(Integer.valueOf(request_id));
        }
        final long j = object;
        final int i = request_id;
        final String str = url;
        final int i2 = method;
        final String str2 = headers;
        final ByteBuffer byteBuffer = body;
        final int i3 = bodyOffset;
        final int i4 = bodySize;
        executor.execute(new Runnable() {
            public void run() {
                NiaNet.doSyncRequest(j, i, str, i2, str2, byteBuffer, i3, i4);
            }
        });
    }

    public static void cancel(int request_id) {
        synchronized (pendingRequestIds) {
            pendingRequestIds.remove(Integer.valueOf(request_id));
        }
    }

    private NiaNet() {
    }

    private static int readDataSteam(HttpURLConnection conn) throws IOException {
        InputStream is;
        if (conn.getResponseCode() == HTTP_OK) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }
        if (is == null) {
            return METHOD_GET;
        }
        ByteBuffer buffer = (ByteBuffer) readBuffer.get();
        try {
            byte[] chunk = buffer.array();
            int bufferOffset = buffer.arrayOffset();
            int offset = bufferOffset;
            boolean keepReading = true;
            while (true) {
                int available = is.available();
                if (chunk.length <= available + offset) {
                    ByteBuffer newBuffer = ByteBuffer.allocateDirect(((available + offset) - bufferOffset) * METHOD_POST);
                    int bytesToCopy = offset - bufferOffset;
                    int newBufferOffset = newBuffer.arrayOffset();
                    if (bytesToCopy > 0) {
                        System.arraycopy(chunk, bufferOffset, newBuffer.array(), newBufferOffset, bytesToCopy);
                    }
                    bufferOffset = newBufferOffset;
                    offset = bytesToCopy + newBufferOffset;
                    chunk = newBuffer.array();
                    readBuffer.set(newBuffer);
                }
                int bytesRead = is.read(chunk, offset, chunk.length - offset);
                if (bytesRead >= 0) {
                    offset += bytesRead;
                    continue;
                } else {
                    keepReading = false;
                    continue;
                }
                if (!keepReading) {
                    break;
                }
            }
            int i = offset - bufferOffset;
            return i;
        } finally {
            is.close();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void doSyncRequest(long r24, int r26, java.lang.String r27, int r28, java.lang.String r29, java.nio.ByteBuffer r30, int r31, int r32) {
        /*
        r5 = pendingRequestIds;
        monitor-enter(r5);
        r4 = pendingRequestIds;	 Catch:{ all -> 0x00b0 }
        r9 = java.lang.Integer.valueOf(r26);	 Catch:{ all -> 0x00b0 }
        r4 = r4.contains(r9);	 Catch:{ all -> 0x00b0 }
        if (r4 != 0) goto L_0x0015;
    L_0x000f:
        r22 = 1;
    L_0x0011:
        if (r22 == 0) goto L_0x0018;
    L_0x0013:
        monitor-exit(r5);	 Catch:{ all -> 0x00b0 }
    L_0x0014:
        return;
    L_0x0015:
        r22 = 0;
        goto L_0x0011;
    L_0x0018:
        r4 = pendingRequestIds;	 Catch:{ all -> 0x00b0 }
        r9 = java.lang.Integer.valueOf(r26);	 Catch:{ all -> 0x00b0 }
        r4.remove(r9);	 Catch:{ all -> 0x00b0 }
        monitor-exit(r5);	 Catch:{ all -> 0x00b0 }
        r20 = 0;
        r6 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r7 = 0;
        r10 = 0;
        r4 = new java.net.URL;	 Catch:{ IOException -> 0x00e2 }
        r0 = r27;
        r4.<init>(r0);	 Catch:{ IOException -> 0x00e2 }
        r4 = r4.openConnection();	 Catch:{ IOException -> 0x00e2 }
        r4 = (java.net.HttpURLConnection) r4;	 Catch:{ IOException -> 0x00e2 }
        r0 = r4;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ IOException -> 0x00e2 }
        r20 = r0;
        r0 = r20;
        r1 = r29;
        setHeaders(r0, r1);	 Catch:{ IOException -> 0x00e2 }
        r4 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
        r0 = r20;
        r0.setConnectTimeout(r4);	 Catch:{ IOException -> 0x00e2 }
        r4 = "Connection";
        r5 = "Keep-Alive";
        r0 = r20;
        r0.setRequestProperty(r4, r5);	 Catch:{ IOException -> 0x00e2 }
        r4 = 0;
        java.net.HttpURLConnection.setFollowRedirects(r4);	 Catch:{ IOException -> 0x00e2 }
        r0 = r24;
        r2 = r20;
        nativeSetupConnection(r0, r2);	 Catch:{ IOException -> 0x00e2 }
        r4 = getMethodString(r28);	 Catch:{ IOException -> 0x00e2 }
        r0 = r20;
        r0.setRequestMethod(r4);	 Catch:{ IOException -> 0x00e2 }
        if (r30 == 0) goto L_0x008d;
    L_0x0067:
        if (r32 <= 0) goto L_0x008d;
    L_0x0069:
        r4 = 1;
        r0 = r20;
        r0.setDoOutput(r4);	 Catch:{ IOException -> 0x00e2 }
        r23 = r20.getOutputStream();	 Catch:{ IOException -> 0x00e2 }
        r4 = r30.hasArray();	 Catch:{ all -> 0x00dd }
        if (r4 == 0) goto L_0x00b3;
    L_0x0079:
        r4 = r30.array();	 Catch:{ all -> 0x00dd }
        r5 = r30.arrayOffset();	 Catch:{ all -> 0x00dd }
        r5 = r5 + r31;
        r0 = r23;
        r1 = r32;
        r0.write(r4, r5, r1);	 Catch:{ all -> 0x00dd }
    L_0x008a:
        r23.close();	 Catch:{ IOException -> 0x00e2 }
    L_0x008d:
        r6 = r20.getResponseCode();	 Catch:{ IOException -> 0x00e2 }
        r7 = joinHeaders(r20);	 Catch:{ IOException -> 0x00e2 }
        r10 = readDataSteam(r20);	 Catch:{ IOException -> 0x00e2 }
        if (r20 == 0) goto L_0x009e;
    L_0x009b:
        r20.disconnect();
    L_0x009e:
        if (r10 <= 0) goto L_0x010d;
    L_0x00a0:
        r4 = readBuffer;
        r8 = r4.get();
        r8 = (java.nio.ByteBuffer) r8;
        r9 = 0;
        r4 = r24;
        nativeCallback(r4, r6, r7, r8, r9, r10);
        goto L_0x0014;
    L_0x00b0:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x00b0 }
        throw r4;
    L_0x00b3:
        r4 = threadChunk;	 Catch:{ all -> 0x00dd }
        r19 = r4.get();	 Catch:{ all -> 0x00dd }
        r19 = (byte[]) r19;	 Catch:{ all -> 0x00dd }
    L_0x00bb:
        r4 = r30.hasRemaining();	 Catch:{ all -> 0x00dd }
        if (r4 == 0) goto L_0x008a;
    L_0x00c1:
        r4 = r30.remaining();	 Catch:{ all -> 0x00dd }
        r0 = r19;
        r5 = r0.length;	 Catch:{ all -> 0x00dd }
        r11 = java.lang.Math.min(r4, r5);	 Catch:{ all -> 0x00dd }
        r4 = 0;
        r0 = r30;
        r1 = r19;
        r0.get(r1, r4, r11);	 Catch:{ all -> 0x00dd }
        r4 = 0;
        r0 = r23;
        r1 = r19;
        r0.write(r1, r4, r11);	 Catch:{ all -> 0x00dd }
        goto L_0x00bb;
    L_0x00dd:
        r4 = move-exception;
        r23.close();	 Catch:{ IOException -> 0x00e2 }
        throw r4;	 Catch:{ IOException -> 0x00e2 }
    L_0x00e2:
        r21 = move-exception;
        r4 = "NiaNet";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0106 }
        r5.<init>();	 Catch:{ all -> 0x0106 }
        r9 = "Network op failed: ";
        r5 = r5.append(r9);	 Catch:{ all -> 0x0106 }
        r9 = r21.getMessage();	 Catch:{ all -> 0x0106 }
        r5 = r5.append(r9);	 Catch:{ all -> 0x0106 }
        r5 = r5.toString();	 Catch:{ all -> 0x0106 }
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0106 }
        r10 = 0;
        if (r20 == 0) goto L_0x009e;
    L_0x0102:
        r20.disconnect();
        goto L_0x009e;
    L_0x0106:
        r4 = move-exception;
        if (r20 == 0) goto L_0x010c;
    L_0x0109:
        r20.disconnect();
    L_0x010c:
        throw r4;
    L_0x010d:
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r12 = r24;
        r14 = r6;
        r15 = r7;
        nativeCallback(r12, r14, r15, r16, r17, r18);
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nianticlabs.nia.network.NiaNet.doSyncRequest(long, int, java.lang.String, int, java.lang.String, java.nio.ByteBuffer, int, int):void");
    }

    private static String getMethodString(int method) {
        switch (method) {
            case METHOD_GET /*0*/:
                return "GET";
            case METHOD_HEAD /*1*/:
                return "HEAD";
            case METHOD_POST /*2*/:
                return "POST";
            case METHOD_PUT /*3*/:
                return "PUT";
            case METHOD_DELETE /*4*/:
                return "DELETE";
            default:
                Log.e(TAG, "Unsupported HTTP method " + method + ", using GET.");
                return "GET";
        }
    }

    private static void setHeaders(HttpURLConnection conn, String headers) {
        if (headers != null && !headers.isEmpty()) {
            int start = METHOD_GET;
            do {
                int newLine = headers.indexOf(10, start);
                if (newLine < 0) {
                    newLine = headers.length();
                }
                int colon = headers.indexOf(58, start);
                if (colon < 0) {
                    colon = headers.length();
                }
                String key = headers.substring(start, colon);
                String value = headers.substring(colon + METHOD_HEAD, newLine);
                if (IF_MODIFIED_SINCE.equals(key)) {
                    try {
                        conn.setIfModifiedSince(parseHttpDateTime(value));
                    } catch (ParseException e) {
                        Log.e(TAG, "If-Modified-Since Date/Time parse failed. " + e.getMessage());
                    }
                } else {
                    conn.setRequestProperty(key, value);
                }
                start = newLine + METHOD_HEAD;
            } while (start < headers.length());
        }
    }

    private static String joinHeaders(HttpURLConnection conn) {
        StringBuilder headers = new StringBuilder();
        int i = METHOD_GET;
        while (true) {
            String fieldKey = conn.getHeaderFieldKey(i);
            if (fieldKey == null) {
                break;
            }
            String field = conn.getHeaderField(i);
            if (field == null) {
                break;
            }
            headers.append(fieldKey);
            headers.append(": ");
            headers.append(field);
            headers.append("\n");
            i += METHOD_HEAD;
        }
        if (headers.length() == 0) {
            return null;
        }
        return headers.toString();
    }

    private static long parseHttpDateTime(String s) throws ParseException {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(s).getTime();
    }
}
