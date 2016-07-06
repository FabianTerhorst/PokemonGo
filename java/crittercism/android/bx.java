package crittercism.android;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.math.BigInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class bx {
    private static at a = null;
    private static Context b = null;
    private static bf c = null;
    private static cb d = null;

    public static class a implements bw {
        private String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public a() {
            String str = null;
            bx.c;
            bx.b;
            if (bx.c.b) {
                str = ((RunningTaskInfo) ((ActivityManager) bx.b.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.flattenToShortString().replace("/", BuildConfig.FLAVOR);
            }
            this.a = str;
        }

        public final String a() {
            return "activity";
        }
    }

    public static class aa implements bw {
        private Float a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public aa() {
            bx.b;
            this.a = Float.valueOf(bx.b.getResources().getDisplayMetrics().ydpi);
        }

        public final String a() {
            return "ydpi";
        }
    }

    public static class b implements bw {
        private Integer a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public b() {
            bx.a;
            this.a = Integer.valueOf(bx.a.b);
        }

        public final String a() {
            return "app_version_code";
        }
    }

    public static class c implements bw {
        private String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public c() {
            bx.a;
            this.a = bx.a.a;
        }

        public final String a() {
            return "app_version";
        }
    }

    public static class d implements bw {
        public final /* synthetic */ Object b() {
            return System.getProperty("os.arch");
        }

        public final String a() {
            return "arch";
        }
    }

    public static class e implements bw {
        private Double a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public e() {
            bx.b;
            double d = 1.0d;
            Intent registerReceiver = bx.b.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int intExtra = registerReceiver.getIntExtra("level", -1);
            double intExtra2 = (double) registerReceiver.getIntExtra("scale", -1);
            if (intExtra >= 0 && intExtra2 > 0.0d) {
                d = ((double) intExtra) / intExtra2;
            }
            this.a = Double.valueOf(d);
        }

        public final String a() {
            return "battery_level";
        }
    }

    public static class f implements bw {
        public String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public f() {
            String networkOperatorName;
            bx.b;
            try {
                networkOperatorName = ((TelephonyManager) bx.b.getSystemService("phone")).getNetworkOperatorName();
            } catch (Exception e) {
                networkOperatorName = Build.BRAND;
            }
            this.a = networkOperatorName;
            new StringBuilder("carrier == ").append(this.a);
            dx.b();
        }

        public final String a() {
            return "carrier";
        }
    }

    static class g implements bw {
        private JSONObject a = null;

        public final /* synthetic */ Object b() {
            return c();
        }

        public g(int i) {
            bx.b;
            bx.c;
            this.a = a(i);
        }

        private static JSONObject a(int i) {
            Object obj = 1;
            if (!bx.c.c) {
                return null;
            }
            if (!ConnectivityManager.isNetworkTypeValid(i)) {
                return null;
            }
            NetworkInfo networkInfo = ((ConnectivityManager) bx.b.getSystemService("connectivity")).getNetworkInfo(i);
            JSONObject jSONObject = new JSONObject();
            if (networkInfo != null) {
                try {
                    jSONObject.put("available", networkInfo.isAvailable());
                    jSONObject.put("connected", networkInfo.isConnected());
                    if (!networkInfo.isConnected()) {
                        jSONObject.put("connecting", networkInfo.isConnectedOrConnecting());
                    }
                    jSONObject.put("failover", networkInfo.isFailover());
                    if (i != 0) {
                        obj = null;
                    }
                    if (obj == null) {
                        return jSONObject;
                    }
                    jSONObject.put("roaming", networkInfo.isRoaming());
                    return jSONObject;
                } catch (JSONException e) {
                    dx.c();
                    return null;
                }
            }
            jSONObject.put("available", false);
            jSONObject.put("connected", false);
            jSONObject.put("connecting", false);
            jSONObject.put("failover", false);
            if (i != 0) {
                obj = null;
            }
            if (obj == null) {
                return jSONObject;
            }
            jSONObject.put("roaming", false);
            return jSONObject;
        }

        public String a() {
            return null;
        }

        public JSONObject c() {
            return this.a;
        }
    }

    public static class h implements bw {
        private Float a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public h() {
            bx.b;
            this.a = Float.valueOf(bx.b.getResources().getDisplayMetrics().density);
        }

        public final String a() {
            return "dpi";
        }
    }

    public static class i implements bw {
        private String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public i() {
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                this.a = BigInteger.valueOf((long) statFs.getAvailableBlocks()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.a = null;
            }
        }

        public final String a() {
            return "disk_space_free";
        }
    }

    public static class j implements bw {
        private String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public j() {
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                this.a = BigInteger.valueOf((long) statFs.getBlockCount()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.a = null;
            }
        }

        public final String a() {
            return "disk_space_total";
        }
    }

    public static class k implements bw {
        public String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public k() {
            bx.b;
            this.a = bx.b.getResources().getConfiguration().locale.getLanguage();
            if (this.a == null || this.a.length() == 0) {
                this.a = "en";
            }
        }

        public final String a() {
            return "locale";
        }
    }

    public static class l implements bw {
        private JSONArray a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public l() {
            bx.c;
            bx.d;
            if (bx.c.a) {
                this.a = bx.d.a();
            }
        }

        public final String a() {
            return "logcat";
        }
    }

    public static class m implements bw {
        private Long a;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public m() {
            this.a = null;
            this.a = Long.valueOf(Runtime.getRuntime().maxMemory());
        }

        public final String a() {
            return "memory_total";
        }
    }

    public static class n implements bw {
        private Integer a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public n() {
            MemoryInfo memoryInfo = new MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            this.a = Integer.valueOf((memoryInfo.otherPss + (memoryInfo.dalvikPss + memoryInfo.nativePss)) * 1024);
        }

        public final String a() {
            return "memory_usage";
        }
    }

    public static class o implements bw {
        public Integer a = Integer.valueOf(0);

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public o() {
            bx.b;
            try {
                String networkOperator = ((TelephonyManager) bx.b.getSystemService("phone")).getNetworkOperator();
                if (networkOperator != null) {
                    this.a = Integer.valueOf(Integer.parseInt(networkOperator.substring(0, 3)));
                }
                new StringBuilder("mobileCountryCode == ").append(this.a);
                dx.b();
            } catch (Exception e) {
            }
        }

        public final String a() {
            return "mobile_country_code";
        }
    }

    public static class p implements bw {
        public Integer a = Integer.valueOf(0);

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public p() {
            bx.b;
            try {
                String networkOperator = ((TelephonyManager) bx.b.getSystemService("phone")).getNetworkOperator();
                if (networkOperator != null) {
                    this.a = Integer.valueOf(Integer.parseInt(networkOperator.substring(3)));
                }
                new StringBuilder("mobileNetworkCode == ").append(this.a);
                dx.b();
            } catch (Exception e) {
            }
        }

        public final String a() {
            return "mobile_network_code";
        }
    }

    public static class q extends g {
        public final /* bridge */ /* synthetic */ JSONObject c() {
            return super.c();
        }

        public q() {
            super(0);
        }

        public final String a() {
            return "mobile_network";
        }
    }

    public static class r implements bw {
        public final /* bridge */ /* synthetic */ Object b() {
            return Build.MODEL;
        }

        public final String a() {
            return Models.CONTENT_DIRECTORY;
        }
    }

    public static class s implements bw {
        public final /* synthetic */ Object b() {
            return new String();
        }

        public final String a() {
            return Twitter.NAME;
        }
    }

    public static class t implements bw {
        private Integer a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public t() {
            bx.b;
            int i = bx.b.getResources().getConfiguration().orientation;
            if (i == 0) {
                Display defaultDisplay = ((WindowManager) bx.b.getSystemService("window")).getDefaultDisplay();
                if (defaultDisplay.getWidth() == defaultDisplay.getHeight()) {
                    i = 3;
                } else if (defaultDisplay.getWidth() > defaultDisplay.getHeight()) {
                    i = 2;
                } else {
                    i = 1;
                }
            }
            this.a = Integer.valueOf(i);
        }

        public final String a() {
            return "orientation";
        }
    }

    public static class u implements bw {
        private String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public u() {
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                this.a = BigInteger.valueOf((long) statFs.getAvailableBlocks()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.a = null;
            }
        }

        public final String a() {
            return "sd_space_free";
        }
    }

    public static class v implements bw {
        private String a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public v() {
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                this.a = BigInteger.valueOf((long) statFs.getBlockCount()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.a = null;
            }
        }

        public final String a() {
            return "sd_space_total";
        }
    }

    public static class w implements bw {
        public final /* bridge */ /* synthetic */ Object b() {
            return "android";
        }

        public final String a() {
            return "system";
        }
    }

    public static class x implements bw {
        public final /* bridge */ /* synthetic */ Object b() {
            return VERSION.RELEASE;
        }

        public final String a() {
            return "system_version";
        }
    }

    public static class y extends g {
        public final /* bridge */ /* synthetic */ JSONObject c() {
            return super.c();
        }

        public y() {
            super(1);
        }

        public final String a() {
            return "wifi";
        }
    }

    public static class z implements bw {
        private Float a = null;

        public final /* bridge */ /* synthetic */ Object b() {
            return this.a;
        }

        public z() {
            bx.b;
            this.a = Float.valueOf(bx.b.getResources().getDisplayMetrics().xdpi);
        }

        public final String a() {
            return "xdpi";
        }
    }

    public static void a(at atVar) {
        a = atVar;
    }

    public static void a(Context context) {
        b = context;
    }

    public static void a(cb cbVar) {
        d = cbVar;
    }

    public static void a(bf bfVar) {
        c = bfVar;
    }
}
