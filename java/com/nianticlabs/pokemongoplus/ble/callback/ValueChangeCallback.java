package com.nianticlabs.pokemongoplus.ble.callback;

import com.nianticlabs.pokemongoplus.ble.SfidaConstant.BluetoothError;

public interface ValueChangeCallback {
    void OnValueChange(boolean z, boolean z2, BluetoothError bluetoothError);
}
