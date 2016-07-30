package com.nianticlabs.pokemongoplus.ble;

import com.nianticlabs.pokemongoplus.ble.SfidaConstant.PeripheralState;
import com.nianticlabs.pokemongoplus.ble.callback.CompletionCallback;
import com.nianticlabs.pokemongoplus.ble.callback.ConnectCallback;

public abstract class Peripheral {
    public abstract void connect(ConnectCallback connectCallback);

    public abstract void disconnect(ConnectCallback connectCallback);

    public abstract void discoverServices(CompletionCallback completionCallback);

    public abstract long getAdvertisingServiceDataLongValue(String str);

    public abstract String getIdentifier();

    public abstract String getName();

    public abstract Service getService(int i);

    public abstract int getServiceCount();

    public abstract PeripheralState getState();

    public abstract void setScanRecord(byte[] bArr);
}
