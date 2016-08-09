package com.upsight.mediation.mraid.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MRAIDHtmlProcessor {
    private static final String TAG = "MRAIDHtmlProcessor";

    public static String processRawHtml(String rawHtml) {
        StringBuffer processedHtml = new StringBuffer(rawHtml);
        Matcher matcher = Pattern.compile("<script\\s+[^>]*\\bsrc\\s*=\\s*([\\\"\\'])mraid\\.js\\1[^>]*>\\s*</script>\\n*", 2).matcher(processedHtml);
        if (matcher.find()) {
            processedHtml.delete(matcher.start(), matcher.end());
        }
        boolean hasHtmlTag = rawHtml.indexOf("<html") != -1;
        boolean hasHeadTag = rawHtml.indexOf("<head") != -1;
        boolean hasBodyTag = rawHtml.indexOf("<body") != -1;
        if (!hasHtmlTag || hasBodyTag) {
            String ls = System.getProperty("line.separator");
            if (!hasHeadTag) {
                processedHtml.insert(0, "<head>" + ls + "</head>" + ls);
            }
            if (!hasHtmlTag) {
                processedHtml.insert(0, "<html>" + ls);
                processedHtml.append("</html>");
            }
            String metaTag = "<meta name='viewport' content='width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no' />";
            String styleTag = "<style>" + ls + "body { margin:0; padding:0; }" + ls + "*:not(input) { -webkit-touch-callout:none; -webkit-user-select:none; -webkit-text-size-adjust:none; }" + ls + "</style>";
            matcher = Pattern.compile("<head[^>]*>", 2).matcher(processedHtml);
            for (int idx = 0; matcher.find(idx); idx = matcher.end()) {
                processedHtml.insert(matcher.end(), ls + metaTag + ls + styleTag);
            }
            return processedHtml.toString();
        }
        MRAIDLog.i(TAG, "have html tag but no body tag. can't randomly insert a body tag");
        return null;
    }
}
