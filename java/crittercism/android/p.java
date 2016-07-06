package crittercism.android;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public final class p extends n {
    public p(Map map) {
        super(map);
        Map treeMap = new TreeMap(new Comparator(this) {
            final /* synthetic */ p a;

            {
                this.a = r1;
            }

            public final /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
                String str = (String) x0;
                String str2 = (String) x1;
                if (str == str2) {
                    return 0;
                }
                if (str == null) {
                    return -1;
                }
                return str2 == null ? 1 : String.CASE_INSENSITIVE_ORDER.compare(str, str2);
            }
        });
        treeMap.putAll(map);
        this.a = treeMap;
    }
}
