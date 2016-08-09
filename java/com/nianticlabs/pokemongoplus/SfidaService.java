package com.nianticlabs.pokemongoplus;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import com.nianticlabs.pokemongoplus.ble.Characteristic;
import com.nianticlabs.pokemongoplus.ble.Service;
import com.nianticlabs.pokemongoplus.ble.callback.CompletionCallback;
import java.util.ArrayList;
import java.util.Iterator;

public class SfidaService extends Service {
    private ArrayList<SfidaCharacteristic> characteristicRef = new ArrayList();
    private long nativeHandle;
    private BluetoothGattService service;

    public SfidaService(BluetoothGattService service, BluetoothGatt gatt) {
        this.service = service;
        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
            this.characteristicRef.add(new SfidaCharacteristic(characteristic, gatt));
        }
    }

    public String getUuid() {
        return this.service.getUuid().toString();
    }

    public boolean isPrimary() {
        return false;
    }

    public void discoverCharacteristics(CompletionCallback complete) {
    }

    public synchronized int getCharacteristicCount() {
        return this.characteristicRef.size();
    }

    public synchronized SfidaCharacteristic getCharacteristic(int index) {
        SfidaCharacteristic sfidaCharacteristic;
        if (index > getCharacteristicCount() - 1) {
            sfidaCharacteristic = null;
        } else {
            sfidaCharacteristic = (SfidaCharacteristic) this.characteristicRef.get(index);
        }
        return sfidaCharacteristic;
    }

    public SfidaCharacteristic getCharacteristic(String uuid) {
        int count = getCharacteristicCount();
        for (int index = 0; index < count; index++) {
            SfidaCharacteristic characteristic = (SfidaCharacteristic) this.characteristicRef.get(index);
            if (characteristic.getUuid().equalsIgnoreCase(uuid)) {
                return characteristic;
            }
        }
        return null;
    }

    public synchronized void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        String uuid = characteristic.getUuid().toString();
        Iterator it = this.characteristicRef.iterator();
        while (it.hasNext()) {
            SfidaCharacteristic sfidaChar = (SfidaCharacteristic) it.next();
            if (sfidaChar.getUuid().equals(uuid)) {
                sfidaChar.onCharacteristicWrite(status);
                break;
            }
        }
    }

    public synchronized void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        String uuid = characteristic.getUuid().toString();
        Iterator it = this.characteristicRef.iterator();
        while (it.hasNext()) {
            SfidaCharacteristic sfidaChar = (SfidaCharacteristic) it.next();
            if (sfidaChar.getUuid().equals(uuid)) {
                sfidaChar.onCharacteristicChanged();
                break;
            }
        }
    }

    public synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        String uuid = characteristic.getUuid().toString();
        Iterator it = this.characteristicRef.iterator();
        while (it.hasNext()) {
            SfidaCharacteristic sfidaChar = (SfidaCharacteristic) it.next();
            if (sfidaChar.getUuid().equals(uuid)) {
                sfidaChar.onCharacteristicRead(status);
                break;
            }
        }
    }

    public synchronized void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        String uuid = descriptor.getCharacteristic().getUuid().toString();
        Iterator it = this.characteristicRef.iterator();
        while (it.hasNext()) {
            SfidaCharacteristic sfidaChar = (SfidaCharacteristic) it.next();
            if (sfidaChar.getUuid().equals(uuid)) {
                sfidaChar.onDescriptorWrite(descriptor, status);
                break;
            }
        }
    }

    public void onDestroy() {
        Iterator it = this.characteristicRef.iterator();
        while (it.hasNext()) {
            Characteristic characteristic = (Characteristic) it.next();
            if (characteristic instanceof SfidaCharacteristic) {
                ((SfidaCharacteristic) characteristic).onDestroy();
            }
        }
        this.characteristicRef.clear();
    }
}
