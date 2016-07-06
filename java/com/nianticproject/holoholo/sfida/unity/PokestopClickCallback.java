package com.nianticproject.holoholo.sfida.unity;

import com.nianticproject.holoholo.sfida.service.SfidaButtonDetector.OnClickListener;

public class PokestopClickCallback implements OnClickListener {
    private String pokestopId;

    public PokestopClickCallback(String pokestopId) {
        this.pokestopId = pokestopId;
    }

    public String getId() {
        return this.pokestopId;
    }

    public void onClick() {
    }

    public void onPress() {
    }

    public void onRelease() {
    }
}
