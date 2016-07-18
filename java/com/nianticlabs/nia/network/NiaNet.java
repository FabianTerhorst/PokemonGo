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

    private static void doSyncRequest(long object, int request_id, String url, int method, String headers, ByteBuffer body, int bodyOffset, int bodyCount) {
        synchronized (pendingRequestIds) {
            if (!pendingRequestIds.contains(request_id)) {
                return;
            }
            int responseSize = 0;
            pendingRequestIds.remove(request_id);
            HttpURLConnection conn = null;
            int responseCode = HTTP_BAD_REQUEST;
            String responseHeaders = null;
            OutputStream os = null;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                setHeaders(conn, headers);
                conn.setConnectTimeout(NETWORK_TIMEOUT_MS);
                conn.setRequestProperty("Connection", "Keep-Alive");
                HttpURLConnection.setFollowRedirects(false);
                conn.setRequestMethod(getMethodString(method));
                if (body != null && bodyCount > 0) {
                    conn.setDoOutput(true);
                    os = conn.getOutputStream();
                    if (body.hasArray()) {
                        os.write(body.array(), body.arrayOffset() + bodyOffset, bodyCount);
                    } else {
                        byte[] chunk = threadChunk.get();
                        while (body.hasRemaining()) {
                            int bytesToRead = Math.min(body.remaining(), chunk.length);
                            body.get(chunk, METHOD_GET, bytesToRead);
                            os.write(chunk, METHOD_GET, bytesToRead);
                        }
                    }
                    os.close();
                }
                responseCode = conn.getResponseCode();
                responseHeaders = joinHeaders(conn);
                responseSize = readDataSteam(conn);
                conn.disconnect();
            } catch (IOException e) {
                try {
                    Log.e(TAG, "Network op failed: " + e.getMessage());
                    responseSize = 0;
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Throwable th) {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } catch (Throwable th2) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (responseSize > 0) {
                nativeCallback(object, responseCode, responseHeaders, readBuffer.get(), 0, responseSize);
            } else {
                nativeCallback(object, responseCode, responseHeaders, null, 0, 0);
            }
        }
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
