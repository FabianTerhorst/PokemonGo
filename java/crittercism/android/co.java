package crittercism.android;

import com.crittercism.error.CRXMLHttpRequestException;

public enum co {
    Generic,
    NSURLConnection,
    ASI,
    Android,
    XMLHttpRequest;

    public static int a(Throwable th) {
        int ordinal = Android.ordinal();
        if (th instanceof CRXMLHttpRequestException) {
            return XMLHttpRequest.ordinal();
        }
        return ordinal;
    }
}
