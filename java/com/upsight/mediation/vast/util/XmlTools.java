package com.upsight.mediation.vast.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlTools {
    private static String TAG = "XmlTools";

    public static void logXmlDocument(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "no");
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(new StringWriter()));
        } catch (Exception e) {
        }
    }

    public static String xmlDocumentToString(Document doc) {
        String xml = null;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "no");
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            xml = sw.toString();
        } catch (Exception e) {
        }
        return xml;
    }

    public static String xmlDocumentToString(Node node) {
        String xml = null;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(sw));
            xml = sw.toString();
        } catch (Exception e) {
        }
        return xml;
    }

    public static Document stringToDocument(String doc) {
        Document document = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(doc));
            document = db.parse(is);
        } catch (Exception e) {
        }
        return document;
    }

    public static String stringFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int length = inputStream.read(buffer);
            if (length == -1) {
                return new String(baos.toByteArray(), "UTF-8");
            }
            baos.write(buffer, 0, length);
        }
    }

    public static String getElementValue(Node node) {
        NodeList childNodes = node.getChildNodes();
        String value = null;
        for (int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
            value = ((CharacterData) childNodes.item(childIndex)).getData().trim();
            if (value.length() != 0) {
                return value;
            }
        }
        return value;
    }
}
