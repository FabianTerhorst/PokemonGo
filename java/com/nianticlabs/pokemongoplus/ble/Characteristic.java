package com.nianticlabs.pokemongoplus.ble;

import com.nianticlabs.pokemongoplus.ble.callback.CompletionCallback;

public abstract class Characteristic {
    public abstract void disableNotify(CompletionCallback completionCallback);

    public abstract void enableNotify(CompletionCallback completionCallback);

    public abstract long getLongValue();

    public abstract String getUuid();

    public abstract byte[] getValue();

    public abstract void readValue(CompletionCallback completionCallback);

    public abstract void writeByteArray(byte[] bArr, CompletionCallback completionCallback);
}
