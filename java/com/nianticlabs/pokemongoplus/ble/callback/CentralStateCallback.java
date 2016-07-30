package com.nianticlabs.pokemongoplus.ble.callback;

import com.nianticlabs.pokemongoplus.ble.SfidaConstant.CentralState;

public interface CentralStateCallback {
    void OnStateChanged(CentralState centralState);
}
