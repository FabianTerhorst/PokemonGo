package com.nianticlabs.pokemongoplus.ble.callback;

import com.nianticlabs.pokemongoplus.ble.SfidaConstant.BluetoothError;

public interface CompletionCallback {
    void onCompletion(boolean z, BluetoothError bluetoothError);
}
