package crittercism.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.nianticlabs.pokemongoplus.ble.BluetoothGattSupport;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.R;

public final class d {
    private ConnectivityManager a;

    public d(Context context) {
        if (context == null) {
            dx.b("Given a null Context.");
            return;
        }
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0) {
            this.a = (ConnectivityManager) context.getSystemService("connectivity");
        } else {
            dx.b("Add android.permission.ACCESS_NETWORK_STATE to AndroidManifest.xml to get more detailed OPTMZ data");
        }
    }

    public final b a() {
        if (this.a == null) {
            return b.UNKNOWN;
        }
        NetworkInfo activeNetworkInfo = this.a.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return b.NOT_CONNECTED;
        }
        return b.a(activeNetworkInfo.getType());
    }

    public final String b() {
        if (this.a == null) {
            return "unknown";
        }
        NetworkInfo activeNetworkInfo = this.a.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return "disconnected";
        }
        int type = activeNetworkInfo.getType();
        if (type == 0) {
            switch (activeNetworkInfo.getSubtype()) {
                case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                case R.styleable.LoadingImageView_circleCrop /*2*/:
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                case 7:
                case 11:
                    return "2G";
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return "3G";
                case 13:
                    return "LTE";
            }
        } else if (type == 1) {
            return "wifi";
        }
        return "unknown";
    }
}
