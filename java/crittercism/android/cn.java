package crittercism.android;

public final class cn {
    public int a = co.Android.ordinal();
    public int b = cm.OK.ordinal();

    public cn(Throwable th) {
        if (th != null) {
            this.a = co.a(th);
            if (this.a == co.Android.ordinal()) {
                this.b = cm.a(th).a();
            } else {
                this.b = Integer.parseInt(th.getMessage());
            }
        }
    }
}
