package com.nianticlabs.pokemongoplus.ble.callback;

import com.nianticlabs.pokemongoplus.ble.Peripheral;

public interface ScanCallback {
    void onScan(Peripheral peripheral);
}
