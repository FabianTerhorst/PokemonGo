package com.nianticproject.holoholo.sfida;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.Toast;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import java.lang.reflect.Method;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.R;

public class SfidaUtils {
    private static final String TAG = SfidaUtils.class.getSimpleName();

    public static String byteArrayToString(byte[] byteArray) {
        String string = BuildConfig.FLAVOR;
        for (byte b : byteArray) {
            string = string + String.valueOf(b);
        }
        return string;
    }

    public static String byteArrayToBitString(byte[] byteArray) {
        String string = BuildConfig.FLAVOR;
        for (byte b : byteArray) {
            for (int count = 0; count < 8; count++) {
                int i;
                StringBuilder append = new StringBuilder().append(string);
                if (((128 >> count) & b) != 0) {
                    i = 1;
                } else {
                    i = 0;
                }
                string = append.append(String.valueOf(i)).toString();
            }
            string = string + " ";
        }
        return string;
    }

    public static boolean refreshDeviceCache(BluetoothGatt gatt) {
        try {
            Method localMethod = gatt.getClass().getMethod("refresh", new Class[0]);
            if (localMethod != null) {
                return ((Boolean) localMethod.invoke(gatt, new Object[0])).booleanValue();
            }
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while refreshing device");
        }
        return false;
    }

    @TargetApi(19)
    public static void createBond(BluetoothDevice device) {
        if (VERSION.SDK_INT >= 19) {
            Log.d(TAG, "createBond() Start Pairing...");
            device.createBond();
            return;
        }
        try {
            Log.d(TAG, "createBond() Start Pairing...");
            device.getClass().getMethod("createBond", (Class[]) null).invoke(device, (Object[]) null);
            Log.d(TAG, "createBond() Pairing finished.");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void removeBond(BluetoothDevice device) {
        try {
            Log.d("removeBond()", "Start remove bond...");
            device.getClass().getMethod("removeBond", (Class[]) null).invoke(device, (Object[]) null);
            Log.d("removeBond()", "remove bond finished.");
        } catch (Exception e) {
            Log.e("removeBond()", e.getMessage());
        }
    }

    public static void toast(final Activity activity, final String text, final int duration) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, text, duration).show();
                }
            });
        }
    }

    public static String getConnectionStateName(int state) {
        switch (state) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                return "STATE_DISCONNECTED";
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                return "STATE_CONNECTING";
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                return "STATE_CONNECTED";
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                return "STATE_DISCONNECTING";
            default:
                return String.valueOf(state);
        }
    }

    public static String getGattStateName(int state) {
        switch (state) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                return "GATT_SUCCESS";
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                return "GATT_READ_NOT_PERMITTED";
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                return "GATT_WRITE_NOT_PERMITTED";
            case 5:
                return "GATT_INSUFFICIENT_AUTHENTICATION";
            case 6:
                return "GATT_REQUEST_NOT_SUPPORTED";
            case 7:
                return "GATT_INVALID_OFFSET";
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                return "GATT_INSUF_AUTHENTICATION";
            case 13:
                return "GATT_INVALID_ATTRIBUTE_LENGTH";
            case 15:
                return "GATT_INSUFFICIENT_ENCRYPTION";
            case BluetoothGattSupport.GATT_INTERNAL_ERROR /*129*/:
                return "GATT_INTERNAL_ERROR";
            case BluetoothGattSupport.GATT_ERROR /*133*/:
                return "GATT_ERROR";
            case 143:
                return "GATT_CONNECTION_CONGESTED";
            case 257:
                return "GATT_FAILURE";
            default:
                return String.valueOf(state);
        }
    }

    public static String getBondStateName(int state) {
        switch (state) {
            case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                return "BOND_NONE";
            case 11:
                return "BOND_BONDING";
            case 12:
                return "BOND_BONDED";
            default:
                return String.valueOf(state);
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
