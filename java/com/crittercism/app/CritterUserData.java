package com.crittercism.app;

import com.voxelbusters.nativeplugins.defines.Keys;
import crittercism.android.dx;
import java.util.Map;

public class CritterUserData {
    private Map a;
    private final boolean b;

    CritterUserData(Map map, boolean isOptedOut) {
        this.a = map;
        this.b = isOptedOut;
    }

    public boolean shouldShowRateMyAppAlert() {
        if (this.a.containsKey("shouldShowRateAppAlert")) {
            return ((Boolean) this.a.get("shouldShowRateAppAlert")).booleanValue();
        }
        if (this.b) {
            dx.c("User has opted out of Crittercism.  Returning false.");
        } else {
            dx.c("CritterUserData instance has no value for shouldShowMyRateAppAlert().  Defaulting to false.");
        }
        return false;
    }

    public String getRateMyAppTitle() {
        if (!this.a.containsKey(Keys.TITLE)) {
            if (this.b) {
                dx.c("User has opted out of Crittercism.  Returning null.");
            } else {
                dx.c("CritterUserData instance has no value for getRateMyAppTitle().  Returning null.");
            }
        }
        return (String) this.a.get(Keys.TITLE);
    }

    public String getRateMyAppMessage() {
        if (!this.a.containsKey(Keys.MESSAGE)) {
            if (this.b) {
                dx.c("User has opted out of Crittercism.  Returning null.");
            } else {
                dx.c("CritterUserData instance has no value for getRateMyAppMessage().  Returning null.");
            }
        }
        return (String) this.a.get(Keys.MESSAGE);
    }

    public boolean isOptedOut() {
        if (this.a.containsKey("optOutStatus")) {
            return ((Boolean) this.a.get("optOutStatus")).booleanValue();
        }
        return this.b;
    }

    public boolean crashedOnLastLoad() {
        if (this.a.containsKey("crashedOnLastLoad")) {
            return ((Boolean) this.a.get("crashedOnLastLoad")).booleanValue();
        }
        if (this.b) {
            dx.c("User has opted out of Crittercism.  Returning false.");
        } else {
            dx.c("CritterUserData instance has no value for crashedOnLastLoad().  Defaulting to false.");
        }
        return false;
    }

    public String getUserUUID() {
        if (!this.a.containsKey("userUUID")) {
            if (this.b) {
                dx.c("User has opted out of Crittercism.  Returning null.");
            } else {
                dx.c("CritterUserData instance has no value for getUserUUID().  Returning null.");
            }
        }
        return (String) this.a.get("userUUID");
    }
}
