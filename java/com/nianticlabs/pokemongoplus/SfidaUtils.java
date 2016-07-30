package com.nianticlabs.pokemongoplus;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import java.lang.reflect.Method;
import spacemadness.com.lunarconsole.BuildConfig;

public class SfidaUtils {
    private static final String TAG = SfidaUtils.class.getSimpleName();

    public static BluetoothManager getBluetoothManager(Context context) {
        return (BluetoothManager) context.getSystemService("bluetooth");
    }

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

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
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

    public static boolean checkBluetoothLeSupported(Context context) {
        if (context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return true;
        }
        return false;
    }
}
