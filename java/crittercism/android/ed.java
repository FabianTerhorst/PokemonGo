package crittercism.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class ed {
    public static final ed a = new ed();
    private ee b = new a();
    private ThreadLocal c = new ThreadLocal();

    class a implements ee {
        final /* synthetic */ ed a;

        private a(ed edVar) {
            this.a = edVar;
        }

        public final Date a() {
            return new Date();
        }
    }

    private ed() {
    }

    private SimpleDateFormat b() {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) this.c.get();
        if (simpleDateFormat != null) {
            return simpleDateFormat;
        }
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        simpleDateFormat.setLenient(false);
        this.c.set(simpleDateFormat);
        return simpleDateFormat;
    }

    public final String a() {
        return a(this.b.a());
    }

    public final String a(Date date) {
        return b().format(date);
    }

    public final long a(String str) {
        return b().parse(str).getTime();
    }
}
