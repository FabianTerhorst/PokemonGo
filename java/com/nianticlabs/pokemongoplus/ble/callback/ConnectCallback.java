package com.nianticlabs.pokemongoplus.ble.callback;

import com.nianticlabs.pokemongoplus.ble.SfidaConstant.BluetoothError;

public interface ConnectCallback {
    void onConnectionStateChanged(boolean z, BluetoothError bluetoothError);
}
