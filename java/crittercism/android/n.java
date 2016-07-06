package crittercism.android;

import java.util.List;
import java.util.Map;

public abstract class n {
    Map a;

    public n(Map map) {
        this.a = map;
    }

    private String c(String str) {
        List list = (List) this.a.get(str);
        if (list != null) {
            return (String) list.get(list.size() - 1);
        }
        return null;
    }

    public final long a(String str) {
        long j = Long.MAX_VALUE;
        String c = c(str);
        if (c != null) {
            try {
                j = Long.parseLong(c);
            } catch (NumberFormatException e) {
            }
        }
        return j;
    }

    public final int b(String str) {
        int i = -1;
        String c = c(str);
        if (c != null) {
            try {
                i = Integer.parseInt(c);
            } catch (NumberFormatException e) {
            }
        }
        return i;
    }

    public final String toString() {
        return this.a.toString();
    }
}
