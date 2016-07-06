package com.upsight.android.analytics.internal.dispatcher.delivery;

import com.upsight.android.analytics.internal.dispatcher.routing.Packet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Batch {
    private int mCapacityLeft;
    private Set<Packet> mPackets = new HashSet();

    public Batch(int capacity) {
        this.mCapacityLeft = capacity;
    }

    public int capacityLeft() {
        return this.mCapacityLeft;
    }

    public void addPacket(Packet packet) {
        this.mPackets.add(packet);
        this.mCapacityLeft--;
    }

    public Set<Packet> getPackets() {
        return Collections.unmodifiableSet(this.mPackets);
    }
}
