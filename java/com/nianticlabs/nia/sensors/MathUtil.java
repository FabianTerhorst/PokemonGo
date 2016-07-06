package com.nianticlabs.nia.sensors;

public final class MathUtil {
    public static final float DEGREES_TO_RADIANS = 0.017453292f;
    public static final float HALF_PI = 1.5707964f;
    public static final double NANOSECONDS_PER_SECOND = 1.0E9d;
    public static final long NANOSECONDS_PER_SECOND_AS_LONG = 1000000000;
    public static final float PI = 3.1415927f;
    public static final float RADIANS_TO_DEGREES = 57.29578f;
    public static final float TWO_PI = 6.2831855f;

    public static float lerp(float a, float b, float t) {
        return ((b - a) * t) + a;
    }

    public static float linearStep(float edge1, float edge2, float t) {
        return saturate((t - edge1) / (edge2 - edge1));
    }

    public static float ease(float t) {
        return (float) Math.sin((((double) Math.max(Math.min(1.0f, t), 0.0f)) * 3.141592653589793d) / 2.0d);
    }

    public static float saturate(float f) {
        return clamp(f, 0.0f, 1.0f);
    }

    public static float clamp(float f, float low, float high) {
        return Math.max(low, Math.min(high, f));
    }

    public static int clamp(int value, int low, int high) {
        return Math.max(low, Math.min(high, value));
    }

    public static float radToDeg(float radians) {
        return RADIANS_TO_DEGREES * radians;
    }

    public static float degToRad(float degrees) {
        return DEGREES_TO_RADIANS * degrees;
    }

    public static float normalizeAngle(float radians) {
        while (radians > PI) {
            radians -= TWO_PI;
        }
        while (radians <= -3.1415927f) {
            radians += TWO_PI;
        }
        return radians;
    }

    public static double normalizeAngle(double radians) {
        while (radians > 3.141592653589793d) {
            radians -= 6.283185307179586d;
        }
        while (radians <= -3.141592653589793d) {
            radians += 6.283185307179586d;
        }
        return radians;
    }

    public static float wrapAngle(float radians) {
        while (radians >= TWO_PI) {
            radians -= TWO_PI;
        }
        while (radians < 0.0f) {
            radians += TWO_PI;
        }
        return radians;
    }

    public static double wrapAngle(double radians) {
        while (radians >= 6.283185307179586d) {
            radians -= 6.283185307179586d;
        }
        while (radians < 0.0d) {
            radians += 6.283185307179586d;
        }
        return radians;
    }

    public static float[] quadraticBezier(float x0, float y0, float x1, float y1, float x2, float y2, int N) {
        float[] a = new float[(N * 2)];
        for (int i = 0; i < N; i++) {
            float t = ((float) i) * (1.0f / ((float) (N - 1)));
            a[i * 2] = ((((1.0f - t) * (1.0f - t)) * x0) + ((((1.0f - t) * 2.0f) * t) * x1)) + ((t * t) * x2);
            a[(i * 2) + 1] = ((((1.0f - t) * (1.0f - t)) * y0) + ((((1.0f - t) * 2.0f) * t) * y1)) + ((t * t) * y2);
        }
        return a;
    }

    public static float randomRange(float min, float max) {
        return ((max - min) * ((float) Math.random())) + min;
    }

    private MathUtil() {
    }

    public static long nextPowerOf2(long val) {
        long v = Math.max(val, 0) - 1;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        return (v | (v >> 16)) + 1;
    }
}
