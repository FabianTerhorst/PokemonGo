package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;

public final class dt {
    public SharedPreferences a;

    public dt(Context context) {
        this.a = context.getSharedPreferences("com.crittercism.ratemyapp", 0);
    }

    protected dt() {
    }

    public final int a() {
        return this.a.getInt("numAppLoads", 0);
    }

    public final void a(boolean z) {
        this.a.edit().putBoolean("rateMyAppEnabled", z).commit();
    }

    public final String b() {
        return this.a.getString("rateAppMessage", "Would you mind taking a second to rate my app?  I would really appreciate it!");
    }

    public final String c() {
        return this.a.getString("rateAppTitle", "Rate My App");
    }

    public final void d() {
        this.a.edit().putBoolean("hasRatedApp", true).commit();
    }
}
