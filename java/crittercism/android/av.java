package crittercism.android;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import crittercism.android.bl.a;

public final class av implements ActivityLifecycleCallbacks {
    private int a = 0;
    private boolean b = false;
    private boolean c = false;
    private boolean d = false;
    private Context e;
    private az f;
    private bd g;

    public av(Context context, az azVar) {
        this.e = context;
        this.f = azVar;
        this.g = new bd(context, azVar);
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityResumed(Activity activity) {
        if (activity != null) {
            try {
                if (this.b) {
                    dx.b();
                    this.b = false;
                } else if (this.a == 0) {
                    this.f.a(new bl(a.FOREGROUND));
                    bg.f();
                    if (!this.d) {
                        this.d = true;
                        b a = new d(this.e).a();
                        if (a != b.UNKNOWN) {
                            if (a == b.NOT_CONNECTED) {
                                this.f.a(new ce(ce.a.INTERNET_DOWN));
                            } else {
                                this.f.a(new ce(ce.a.INTERNET_UP));
                            }
                        }
                    }
                } else {
                    this.f.a(new bj(bj.a.ACTIVATED, activity.getClass().getName()));
                }
                this.a++;
                IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                activity.registerReceiver(this.g, intentFilter);
                this.c = true;
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.a(th);
            }
        }
    }

    public final void onActivityPaused(Activity activity) {
        if (activity != null) {
            try {
                if (this.c) {
                    activity.unregisterReceiver(this.g);
                    this.c = false;
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.a(th);
            }
        }
    }

    public final void onActivityStopped(Activity activity) {
        if (activity != null) {
            try {
                this.a--;
                if (activity.isChangingConfigurations()) {
                    dx.b();
                    this.b = true;
                } else if (this.a == 0) {
                    this.f.a(new bl(a.BACKGROUND));
                    bg.a(this.f);
                } else {
                    this.f.a(new bj(bj.a.DEACTIVATED, activity.getClass().getName()));
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.a(th);
            }
        }
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public final void onActivityDestroyed(Activity activity) {
    }
}
