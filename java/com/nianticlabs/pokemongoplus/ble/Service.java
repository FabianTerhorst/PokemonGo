package com.nianticlabs.pokemongoplus.ble;

import com.nianticlabs.pokemongoplus.ble.callback.CompletionCallback;

public abstract class Service {
    public abstract void discoverCharacteristics(CompletionCallback completionCallback);

    public abstract int getCharacteristicCount();

    public abstract String getUuid();

    public abstract boolean isPrimary();
}
