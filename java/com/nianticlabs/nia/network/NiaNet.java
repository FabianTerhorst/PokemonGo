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
    private static void doSyncRequest(long r22, int r24, java.lang.String r25, int r26, java.lang.String r27, java.nio.ByteBuffer r28, int r29, int r30) {
        /*
        r3 = pendingRequestIds;
        monitor-enter(r3);
        r2 = pendingRequestIds;	 Catch:{ all -> 0x00a9 }
        r7 = java.lang.Integer.valueOf(r24);	 Catch:{ all -> 0x00a9 }
        r2 = r2.contains(r7);	 Catch:{ all -> 0x00a9 }
        if (r2 != 0) goto L_0x0015;
    L_0x000f:
        r20 = 1;
    L_0x0011:
        if (r20 == 0) goto L_0x0018;
    L_0x0013:
        monitor-exit(r3);	 Catch:{ all -> 0x00a9 }
    L_0x0014:
        return;
    L_0x0015:
        r20 = 0;
        goto L_0x0011;
    L_0x0018:
        r2 = pendingRequestIds;	 Catch:{ all -> 0x00a9 }
        r7 = java.lang.Integer.valueOf(r24);	 Catch:{ all -> 0x00a9 }
        r2.remove(r7);	 Catch:{ all -> 0x00a9 }
        monitor-exit(r3);	 Catch:{ all -> 0x00a9 }
        r18 = 0;
        r4 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r5 = 0;
        r8 = 0;
        r2 = new java.net.URL;	 Catch:{ IOException -> 0x00db }
        r0 = r25;
        r2.<init>(r0);	 Catch:{ IOException -> 0x00db }
        r2 = r2.openConnection();	 Catch:{ IOException -> 0x00db }
        r2 = (java.net.HttpURLConnection) r2;	 Catch:{ IOException -> 0x00db }
        r0 = r2;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ IOException -> 0x00db }
        r18 = r0;
        r0 = r18;
        r1 = r27;
        setHeaders(r0, r1);	 Catch:{ IOException -> 0x00db }
        r2 = 15000; // 0x3a98 float:2.102E-41 double:7.411E-320;
        r0 = r18;
        r0.setConnectTimeout(r2);	 Catch:{ IOException -> 0x00db }
        r2 = "Connection";
        r3 = "Keep-Alive";
        r0 = r18;
        r0.setRequestProperty(r2, r3);	 Catch:{ IOException -> 0x00db }
        r2 = 0;
        java.net.HttpURLConnection.setFollowRedirects(r2);	 Catch:{ IOException -> 0x00db }
        r2 = getMethodString(r26);	 Catch:{ IOException -> 0x00db }
        r0 = r18;
        r0.setRequestMethod(r2);	 Catch:{ IOException -> 0x00db }
        if (r28 == 0) goto L_0x0086;
    L_0x0060:
        if (r30 <= 0) goto L_0x0086;
    L_0x0062:
        r2 = 1;
        r0 = r18;
        r0.setDoOutput(r2);	 Catch:{ IOException -> 0x00db }
        r21 = r18.getOutputStream();	 Catch:{ IOException -> 0x00db }
        r2 = r28.hasArray();	 Catch:{ all -> 0x00d6 }
        if (r2 == 0) goto L_0x00ac;
    L_0x0072:
        r2 = r28.array();	 Catch:{ all -> 0x00d6 }
        r3 = r28.arrayOffset();	 Catch:{ all -> 0x00d6 }
        r3 = r3 + r29;
        r0 = r21;
        r1 = r30;
        r0.write(r2, r3, r1);	 Catch:{ all -> 0x00d6 }
    L_0x0083:
        r21.close();	 Catch:{ IOException -> 0x00db }
    L_0x0086:
        r4 = r18.getResponseCode();	 Catch:{ IOException -> 0x00db }
        r5 = joinHeaders(r18);	 Catch:{ IOException -> 0x00db }
        r8 = readDataSteam(r18);	 Catch:{ IOException -> 0x00db }
        if (r18 == 0) goto L_0x0097;
    L_0x0094:
        r18.disconnect();
    L_0x0097:
        if (r8 <= 0) goto L_0x0106;
    L_0x0099:
        r2 = readBuffer;
        r6 = r2.get();
        r6 = (java.nio.ByteBuffer) r6;
        r7 = 0;
        r2 = r22;
        nativeCallback(r2, r4, r5, r6, r7, r8);
        goto L_0x0014;
    L_0x00a9:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00a9 }
        throw r2;
    L_0x00ac:
        r2 = threadChunk;	 Catch:{ all -> 0x00d6 }
        r17 = r2.get();	 Catch:{ all -> 0x00d6 }
        r17 = (byte[]) r17;	 Catch:{ all -> 0x00d6 }
    L_0x00b4:
        r2 = r28.hasRemaining();	 Catch:{ all -> 0x00d6 }
        if (r2 == 0) goto L_0x0083;
    L_0x00ba:
        r2 = r28.remaining();	 Catch:{ all -> 0x00d6 }
        r0 = r17;
        r3 = r0.length;	 Catch:{ all -> 0x00d6 }
        r9 = java.lang.Math.min(r2, r3);	 Catch:{ all -> 0x00d6 }
        r2 = 0;
        r0 = r28;
        r1 = r17;
        r0.get(r1, r2, r9);	 Catch:{ all -> 0x00d6 }
        r2 = 0;
        r0 = r21;
        r1 = r17;
        r0.write(r1, r2, r9);	 Catch:{ all -> 0x00d6 }
        goto L_0x00b4;
    L_0x00d6:
        r2 = move-exception;
        r21.close();	 Catch:{ IOException -> 0x00db }
        throw r2;	 Catch:{ IOException -> 0x00db }
    L_0x00db:
        r19 = move-exception;
        r2 = "NiaNet";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ff }
        r3.<init>();	 Catch:{ all -> 0x00ff }
        r7 = "Network op failed: ";
        r3 = r3.append(r7);	 Catch:{ all -> 0x00ff }
        r7 = r19.getMessage();	 Catch:{ all -> 0x00ff }
        r3 = r3.append(r7);	 Catch:{ all -> 0x00ff }
        r3 = r3.toString();	 Catch:{ all -> 0x00ff }
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x00ff }
        r8 = 0;
        if (r18 == 0) goto L_0x0097;
    L_0x00fb:
        r18.disconnect();
        goto L_0x0097;
    L_0x00ff:
        r2 = move-exception;
        if (r18 == 0) goto L_0x0105;
    L_0x0102:
        r18.disconnect();
    L_0x0105:
        throw r2;
    L_0x0106:
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r10 = r22;
        r12 = r4;
        r13 = r5;
        nativeCallback(r10, r12, r13, r14, r15, r16);
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
