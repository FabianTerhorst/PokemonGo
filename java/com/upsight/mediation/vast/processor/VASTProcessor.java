package com.upsight.mediation.vast.processor;

import android.content.Context;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.mediation.ads.adapters.VastAdAdapter;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.model.VAST_DOC_ELEMENTS;
import com.upsight.mediation.vast.util.XmlTools;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public final class VASTProcessor {
    private static final boolean IS_VALIDATION_ON = false;
    private static final int MAX_VAST_LEVELS = 5;
    private static final String TAG = "VASTProcessor";
    private final VASTPlayer mVastPlayer;
    private VASTMediaPicker mediaPicker;
    private StringBuilder mergedVastDocs = new StringBuilder(500);
    private VASTModel vastModel;

    public VASTProcessor(VASTMediaPicker mediaPicker, VASTPlayer vastPlayer) {
        this.mediaPicker = mediaPicker;
        this.mVastPlayer = vastPlayer;
    }

    public VASTModel getModel() {
        return this.vastModel;
    }

    public int process(Context c, String xmlData, boolean shouldValidateSchema, int downloadTimeout) {
        FuseLog.d(TAG, "process");
        this.vastModel = null;
        try {
            InputStream is = new ByteArrayInputStream(xmlData.getBytes(Charset.defaultCharset().name()));
            int error = processUri(is, 0);
            try {
                is.close();
            } catch (IOException e) {
            }
            InputStream inputStream;
            if (error != 0) {
                inputStream = is;
                return error;
            }
            Document mainDoc = wrapMergedVastDocWithVasts();
            this.vastModel = new VASTModel(mainDoc);
            if (mainDoc == null) {
                inputStream = is;
                return 100;
            } else if (shouldValidateSchema && !VASTModelPostValidator.validate(this.vastModel)) {
                inputStream = is;
                return VASTPlayer.ERROR_SCHEMA_VALIDATION;
            } else if (!VASTModelPostValidator.pickMediaFile(this.vastModel, this.mediaPicker)) {
                inputStream = is;
                return VASTPlayer.ERROR_NO_COMPATIBLE_MEDIA_FILE;
            } else if (this.vastModel.getPickedMediaFileDeliveryType().equals("progressive")) {
                inputStream = is;
                return this.vastModel.cache(c, this.mVastPlayer, downloadTimeout);
            } else {
                if (this.vastModel.getPickedMediaFileDeliveryType().equals("streaming")) {
                    this.mVastPlayer.setLoaded(true);
                }
                inputStream = is;
                return 0;
            }
        } catch (UnsupportedEncodingException e2) {
            FuseLog.w(TAG, e2.getMessage(), e2);
            return 100;
        }
    }

    private Document wrapMergedVastDocWithVasts() {
        return XmlTools.stringToDocument(this.mergedVastDocs.toString());
    }

    private int processUri(InputStream is, int depth) {
        if (depth >= MAX_VAST_LEVELS) {
            return VASTPlayer.ERROR_EXCEEDED_WRAPPER_LIMIT;
        }
        Document doc = createDoc(is);
        if (doc == null) {
            return 100;
        }
        String version = doc.getFirstChild().getNodeName().equals(VastAdAdapter.NAME) ? doc.getFirstChild().getAttributes().getNamedItem("version").getNodeValue().toString() : doc.getChildNodes().item(1).getAttributes().getNamedItem("version").getNodeValue().toString();
        if (!version.equals("2.0") && !version.equals("3.0")) {
            return VASTPlayer.ERROR_UNSUPPORTED_VERSION;
        }
        NodeList uriToNextDoc = doc.getElementsByTagName(VAST_DOC_ELEMENTS.vastAdTagURI.getValue());
        if (uriToNextDoc == null || uriToNextDoc.getLength() == 0) {
            merge(doc);
            return 0;
        }
        FuseLog.v(TAG, "Doc is a wrapper. ");
        String nextUri = XmlTools.getElementValue(uriToNextDoc.item(0));
        FuseLog.v(TAG, "Wrapper URL: " + nextUri);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(nextUri).openConnection();
            connection.setConnectTimeout(Default.HTTP_REQUEST_TIMEOUT_MS);
            connection.setReadTimeout(Default.HTTP_REQUEST_TIMEOUT_MS);
            if (connection.getResponseCode() != 200) {
                return VASTPlayer.ERROR_NO_VAST_IN_WRAPPER;
            }
            InputStream nextInputStream = connection.getInputStream();
            int error = processUri(nextInputStream, depth + 1);
            try {
                nextInputStream.close();
                return error;
            } catch (IOException e) {
                return error;
            }
        } catch (MalformedURLException e2) {
            return VASTPlayer.ERROR_NO_VAST_IN_WRAPPER;
        } catch (SocketTimeoutException e3) {
            return VASTPlayer.ERROR_WRAPPER_TIMEOUT;
        } catch (IOException e4) {
            FuseLog.w(TAG, e4.getMessage(), e4);
            return VASTPlayer.ERROR_GENERAL_WRAPPER;
        }
    }

    private Document createDoc(InputStream is) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            return null;
        }
    }

    private void merge(Document newDoc) {
        this.mergedVastDocs.append(XmlTools.xmlDocumentToString(newDoc.getElementsByTagName(VastAdAdapter.NAME).item(0)));
    }
}
