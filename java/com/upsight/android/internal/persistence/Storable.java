package com.upsight.android.internal.persistence;

public final class Storable {
    String id;
    String type;
    String value;

    public static Storable create(String id, String type, String value) {
        return new Storable(id, type, value);
    }

    Storable(String id, String type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getID() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }
}
