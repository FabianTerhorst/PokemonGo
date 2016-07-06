package crittercism.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import crittercism.android.ce.a;

public final class bd extends BroadcastReceiver {
    private az a;
    private String b;
    private b c;

    public bd(Context context, az azVar) {
        this.a = azVar;
        d dVar = new d(context);
        this.b = dVar.b();
        this.c = dVar.a();
    }

    public final void onReceive(Context context, Intent intent) {
        new StringBuilder("CrittercismReceiver: INTENT ACTION = ").append(intent.getAction());
        dx.b();
        d dVar = new d(context);
        b a = dVar.a();
        if (!(this.c == a || a == b.UNKNOWN)) {
            if (a == b.NOT_CONNECTED) {
                this.a.a(new ce(a.INTERNET_DOWN));
            } else if (this.c == b.NOT_CONNECTED || this.c == b.UNKNOWN) {
                this.a.a(new ce(a.INTERNET_UP));
            }
            this.c = a;
        }
        String b = dVar.b();
        if (!b.equals(this.b)) {
            if (this.b.equals("unknown") || this.b.equals("disconnected")) {
                if (!(b.equals("unknown") || b.equals("disconnected"))) {
                    this.a.a(new ce(a.CONN_TYPE_GAINED, b));
                }
            } else if (b.equals("disconnected")) {
                this.a.a(new ce(a.CONN_TYPE_LOST, this.b));
            } else if (!b.equals("unknown")) {
                this.a.a(new ce(a.CONN_TYPE_SWITCHED, this.b, b));
            }
            this.b = b;
        }
    }
}
