package com.upsight.mediation.mraid.properties;

import java.util.Arrays;

public final class MRAIDResizeProperties {
    public static final int CUSTOM_CLOSE_POSITION_BOTTOM_CENTER = 5;
    public static final int CUSTOM_CLOSE_POSITION_BOTTOM_LEFT = 4;
    public static final int CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT = 6;
    public static final int CUSTOM_CLOSE_POSITION_CENTER = 3;
    public static final int CUSTOM_CLOSE_POSITION_TOP_CENTER = 1;
    public static final int CUSTOM_CLOSE_POSITION_TOP_LEFT = 0;
    public static final int CUSTOM_CLOSE_POSITION_TOP_RIGHT = 2;
    public boolean allowOffscreen;
    public int customClosePosition;
    public int height;
    public int offsetX;
    public int offsetY;
    public int width;

    public MRAIDResizeProperties() {
        this(CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_LEFT, CUSTOM_CLOSE_POSITION_TOP_RIGHT, true);
    }

    public MRAIDResizeProperties(int width, int height, int offsetX, int offsetY, int customClosePosition, boolean allowOffscreen) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.customClosePosition = customClosePosition;
        this.allowOffscreen = allowOffscreen;
    }

    public static int customClosePositionFromString(String name) {
        int idx = Arrays.asList(new String[]{"top-left", "top-center", "top-right", "center", "bottom-left", "bottom-center", "bottom-right"}).indexOf(name);
        return idx != -1 ? idx : CUSTOM_CLOSE_POSITION_TOP_RIGHT;
    }
}
