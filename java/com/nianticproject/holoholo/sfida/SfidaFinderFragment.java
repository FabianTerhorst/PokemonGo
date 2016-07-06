package com.nianticproject.holoholo.sfida;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants;
import com.nianticproject.holoholo.sfida.constants.SfidaConstants.SfidaVersion;
import java.util.List;
import spacemadness.com.lunarconsole.R;

@TargetApi(18)
public class SfidaFinderFragment extends Fragment {
    private static String BLE_NAME = null;
    private static final int REQUEST_ENABLE_BT = 10;
    private static final long SCAN_PERIOD = -559038737;
    public static final String TAG = SfidaFinderFragment.class.getSimpleName();
    private BluetoothAdapter bluetoothAdapter;
    private List<String> bluetoothAddressFilter;
    private boolean isFiltered = false;
    private boolean isScanningSfida = false;
    private LeScanCallback leScanCallback = new LeScanCallback() {
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (SfidaFinderFragment.this.isScanningSfida) {
                Log.d(SfidaFinderFragment.TAG, "onLeScan() scanRecord : " + SfidaUtils.byteArrayToString(scanRecord));
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                int bondState = device.getBondState();
                if (deviceName == null || !deviceName.contains(SfidaFinderFragment.BLE_NAME)) {
                    Log.d(SfidaFinderFragment.TAG, "deviceName : [" + deviceName + "] was not contained GO PLUS name.");
                    return;
                }
                Log.d(SfidaFinderFragment.TAG, "SFIDA found, device bondState : " + SfidaUtils.getBondStateName(bondState));
                if (deviceAddress != null && SfidaFinderFragment.this.isFiltered && !SfidaFinderFragment.this.isFilteredDevice(deviceAddress)) {
                    Log.d(SfidaFinderFragment.TAG, deviceName + " was not filtered.");
                } else if (SfidaFinderFragment.this.onDeviceDiscoveredListener != null) {
                    SfidaFinderFragment.this.onDeviceDiscoveredListener.onDeviceDiscovered(device, SfidaFinderFragment.this.detectButtonPressed(scanRecord));
                }
            }
        }
    };
    private OnDeviceDiscoveredListener onDeviceDiscoveredListener;

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion = new int[SfidaVersion.values().length];

        static {
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.ALPHA_NO_SEC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.ALPHA_SEC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.BETA1.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaVersion.BETA4.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public interface OnDeviceDiscoveredListener {
        void onDeviceDiscovered(BluetoothDevice bluetoothDevice, boolean z);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        switch (AnonymousClass2.$SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[SfidaConstants.SFIDA_VERSION.ordinal()]) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                BLE_NAME = "SFIDA";
                break;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                BLE_NAME = "EBISU";
                break;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                BLE_NAME = "EBISU";
                break;
            case 4:
                BLE_NAME = "Pokemon GO Plus";
                break;
        }
        if (checkBluetoothSettingEnable(activity)) {
            init(activity);
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        scanLeDevice(false);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDetach() {
        super.onDetach();
        this.bluetoothAddressFilter = null;
    }

    public static SfidaFinderFragment createInstance() {
        return new SfidaFinderFragment();
    }

    public void setOnDeviceDiscoveredListener(OnDeviceDiscoveredListener onDeviceDiscoveredListener) {
        this.onDeviceDiscoveredListener = onDeviceDiscoveredListener;
    }

    public void setSfidaVersion(SfidaVersion version) {
        SfidaConstants.SFIDA_VERSION = version;
        switch (AnonymousClass2.$SwitchMap$com$nianticproject$holoholo$sfida$constants$SfidaConstants$SfidaVersion[version.ordinal()]) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                BLE_NAME = "SFIDA";
                return;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                BLE_NAME = "EBISU";
                return;
            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                BLE_NAME = "EBISU";
                return;
            case 4:
                BLE_NAME = "Pokemon GO Plus";
                return;
            default:
                return;
        }
    }

    public void executeFindSfida() {
        if (enableBt() && !this.isScanningSfida) {
            scanLeDevice(true);
        }
    }

    public void executeFindSfida(List<String> bluetoothAddressList) {
        this.bluetoothAddressFilter = bluetoothAddressList;
        this.isFiltered = true;
        executeFindSfida();
    }

    public void cancelFindSfida() {
        scanLeDevice(false);
    }

    private boolean isFilteredDevice(String bluetoothAddress) {
        return this.bluetoothAddressFilter != null && this.bluetoothAddressFilter.contains(bluetoothAddress);
    }

    private boolean detectButtonPressed(byte[] scanRecord) {
        if (scanRecord.length <= 27 || scanRecord[26] == (byte) 0) {
            return false;
        }
        return true;
    }

    private boolean checkBluetoothSettingEnable(Activity activity) {
        if (activity.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            return true;
        }
        Toast.makeText(activity, "BluetoothLE not supported.", 0).show();
        return false;
    }

    private void init(Activity activity) {
        this.bluetoothAdapter = ((BluetoothManager) activity.getSystemService("bluetooth")).getAdapter();
    }

    private boolean enableBt() {
        if (this.bluetoothAdapter != null && this.bluetoothAdapter.isEnabled()) {
            return true;
        }
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_ENABLE_BT);
        return false;
    }

    private void scanLeDevice(boolean enable) {
        Log.d(TAG, "scanLeDevice() : " + (enable ? "start scan." : "cancel scan."));
        if (enable) {
            this.isScanningSfida = true;
            Log.d(TAG, "scanCallback : " + this.leScanCallback);
            this.bluetoothAdapter.startLeScan(this.leScanCallback);
            return;
        }
        this.isScanningSfida = false;
        this.bluetoothAdapter.stopLeScan(this.leScanCallback);
        this.bluetoothAdapter.cancelDiscovery();
    }
}
