package crittercism.android;

import java.util.HashMap;
import java.util.Map;

public final class bn {
    private static final Map a;
    private String b;
    private String c;
    private String d;
    private String e;

    public static class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    static {
        Map hashMap = new HashMap();
        a = hashMap;
        hashMap.put("00555300", "crittercism.com");
        a.put("00555304", "crit-ci.com");
        a.put("00555305", "crit-staging.com");
        a.put("00444503", "eu.crittercism.com");
    }

    public bn(String str) {
        if (str == null) {
            throw new a("Given null appId");
        } else if (!str.matches("[0-9a-fA-F]+")) {
            throw new a("Invalid appId: '" + str + "'. AppId must be hexadecimal characters");
        } else if (str.length() == 24 || str.length() == 40) {
            Object obj = null;
            if (str.length() == 24) {
                obj = "00555300";
            } else if (str.length() == 40) {
                obj = str.substring(str.length() - 8);
            }
            String str2 = (String) a.get(obj);
            if (str2 == null) {
                throw new a("Invalid appId: '" + str + "'. Invalid app locator code");
            }
            this.b = System.getProperty("com.crittercism.apmUrl", "https://apm." + str2);
            this.c = System.getProperty("com.crittercism.apiUrl", "https://api." + str2);
            this.d = System.getProperty("com.crittercism.txnUrl", "https://txn.ingest." + str2);
            this.e = System.getProperty("com.crittercism.appLoadUrl", "https://appload.ingest." + str2);
        } else {
            throw new a("Invalid appId: '" + str + "'. AppId must be either 24 or 40 characters");
        }
    }

    public final String a() {
        return this.c;
    }

    public final String b() {
        return this.b;
    }

    public final String c() {
        return this.e;
    }

    public final String d() {
        return this.d;
    }
}
