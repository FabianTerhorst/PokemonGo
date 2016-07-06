package com.upsight.android.internal.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipHelper {
    public static byte[] compress(String data) throws IOException {
        if (data == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(new BufferedOutputStream(out));
        try {
            gzip.write(data.getBytes());
            return out.toByteArray();
        } finally {
            gzip.close();
        }
    }

    public static String decompress(byte[] data) throws IOException {
        if (data == null) {
            return null;
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis));
        StringBuilder res = new StringBuilder();
        while (true) {
            try {
                String line = bf.readLine();
                if (line == null) {
                    break;
                }
                res.append(line);
            } finally {
                gis.close();
            }
        }
        return res.toString();
    }
}
