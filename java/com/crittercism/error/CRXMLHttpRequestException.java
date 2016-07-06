package com.crittercism.error;

public class CRXMLHttpRequestException extends Exception {
    private static final long serialVersionUID = 1515011187293165939L;

    public CRXMLHttpRequestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CRXMLHttpRequestException(String detailMessage) {
        this(detailMessage, null);
    }

    public CRXMLHttpRequestException(Throwable throwable) {
        super(throwable);
    }
}
