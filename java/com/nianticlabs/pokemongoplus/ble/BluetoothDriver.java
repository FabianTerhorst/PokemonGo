package com.nianticlabs.pokemongoplus.ble;

import com.nianticlabs.pokemongoplus.ble.SfidaConstant.CentralState;
import com.nianticlabs.pokemongoplus.ble.callback.CentralStateCallback;
import com.nianticlabs.pokemongoplus.ble.callback.ScanCallback;

public abstract class BluetoothDriver {
    private CentralState currentState = CentralState.Unknown;

    public abstract boolean IsScanning();

    public abstract int start(CentralStateCallback centralStateCallback);

    public abstract void startScanning(String str, ScanCallback scanCallback);

    public abstract void stop(int i);

    public abstract void stopScanning(String str);
}
