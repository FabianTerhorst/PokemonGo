package com.nianticproject.holoholo.sfida.service;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;
import com.nianticproject.holoholo.sfida.SfidaUtils;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import spacemadness.com.lunarconsole.R;

@TargetApi(18)
public class SfidaGattCallback extends BluetoothGattCallback {
    public static final String TAG = SfidaGattCallback.class.getSimpleName();
    private SfidaService sfidaService;

    public SfidaGattCallback(SfidaService sfidaService) {
        this.sfidaService = sfidaService;
    }

    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        Log.d(TAG, "[BLE] onDescriptorWrite() \n  UUID   : " + descriptor.getUuid() + "\n" + "  status : " + SfidaUtils.getGattStateName(status) + "\n" + "  value  : " + SfidaUtils.byteArrayToString(descriptor.getValue()));
        switch (status) {
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                Log.e(TAG, "Too early to call enableSecurityServiceNotify().");
                break;
            case BluetoothGattSupport.GATT_INTERNAL_ERROR /*129*/:
                Log.e(TAG, "SFIDA is already unpaired");
                break;
            case BluetoothGattSupport.GATT_ERROR /*133*/:
                Log.e(TAG, "SFIDA is already paired with other device");
                break;
        }
        this.sfidaService.setIsReceivedNotifyCallback(true);
    }

    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == 0) {
            this.sfidaService.onSfidaUpdated(characteristic);
        } else {
            Log.e(TAG, "[BLE] onCharacteristicRead() Read failed.");
        }
    }

    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.d(TAG, "[BLE] onCharacteristicWrite() \n  UUID   : " + characteristic.getUuid() + "\n" + "  status : " + SfidaUtils.getGattStateName(status) + "\n" + "  value  : " + SfidaUtils.byteArrayToString(characteristic.getValue()));
        this.sfidaService.setIsReceivedWriteCallback(true);
        if (status == BluetoothGattSupport.GATT_ERROR) {
            this.sfidaService.disconnectBluetooth();
        }
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.d(TAG, "[BLE] onConnectionStateChange() oldState : " + SfidaUtils.getConnectionStateName(status) + " \u2192 newState : " + SfidaUtils.getConnectionStateName(newState));
        switch (newState) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                Log.d(TAG, "[BLE] Disconnected from GATT server.");
                this.sfidaService.onDisconnectedWithGattServer();
                return;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                Log.d(TAG, "[BLE] Connected with GATT server.");
                this.sfidaService.onConnectedWithGattServer(gatt);
                return;
            case 19:
            case 34:
            case BluetoothGattSupport.GATT_ERROR /*133*/:
                return;
            default:
                Log.e(TAG, "[BLE] onConnectionStateChange() UnhandledState status : " + status + " " + "newState : " + newState);
                return;
        }
    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.d(TAG, "[BLE] onServicesDiscovered() : " + SfidaUtils.getGattStateName(status));
        switch (status) {
            case R.styleable.AdsAttrs_adSize /*0*/:
                this.sfidaService.onServiceDiscovered();
                return;
            default:
                Log.e(TAG, "[BLE] onServicesDiscovered received error: " + status);
                return;
        }
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        this.sfidaService.onSfidaUpdated(characteristic);
    }
}
