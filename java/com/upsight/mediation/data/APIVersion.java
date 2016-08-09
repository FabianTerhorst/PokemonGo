package com.upsight.mediation.data;

import com.upsight.mediation.util.StringUtil;
import java.util.Arrays;

public class APIVersion implements Comparable {
    public final int[] versionComponents;

    public APIVersion(String versionString) {
        this.versionComponents = StringUtil.toIntArray(versionString, "\\.");
    }

    public APIVersion(int... versionComponents) {
        if (versionComponents == null || versionComponents.length == 0) {
            throw new IllegalArgumentException("At least one version component must be specified");
        }
        this.versionComponents = versionComponents;
    }

    public int compareTo(Object other) {
        if (equals((APIVersion) other)) {
            return 0;
        }
        for (int i = 0; i < this.versionComponents.length; i++) {
            int thisComponent = getVersionComponent(i);
            int otherComponent = ((APIVersion) other).getVersionComponent(i);
            if (thisComponent > otherComponent) {
                return i;
            }
            if (thisComponent < otherComponent) {
                return -i;
            }
        }
        return 0;
    }

    public int getVersionComponent(int index) {
        if (this.versionComponents.length > index) {
            return this.versionComponents[index];
        }
        return 0;
    }

    public boolean equals(Object other) {
        return Arrays.equals(this.versionComponents, ((APIVersion) other).versionComponents);
    }

    public String toString() {
        return StringUtil.join(this.versionComponents, ".");
    }
}
