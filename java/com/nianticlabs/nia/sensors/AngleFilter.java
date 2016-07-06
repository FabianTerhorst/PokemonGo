package com.nianticlabs.nia.sensors;

public class AngleFilter {
    private static final float FRICTION_COEFFICIENT_1 = 1.0f;
    private static final float FRICTION_COEFFICIENT_2 = 0.5f;
    private static final float MAX_DT = 10.0f;
    private static final float SIGNAL_LEVEL = 10.0f;
    private static final float STEP_SIZE = 0.5f;
    private static final float TIME_NORMALIZATION_MS = 100.0f;
    protected float currentValue;
    private long lastReadingTime = 0;
    private float speed = 0.0f;
    private final boolean wrap;

    public AngleFilter(boolean wrap) {
        this.wrap = wrap;
    }

    public float filter(long time, float in) {
        if (this.lastReadingTime == 0 || time < this.lastReadingTime) {
            this.currentValue = in;
        } else {
            if (this.wrap && Math.abs(in - this.currentValue) * 2.0f > 360.0f) {
                if (in < this.currentValue) {
                    in += 360.0f;
                } else {
                    in -= 360.0f;
                }
            }
            float dT = ((float) (time - this.lastReadingTime)) / TIME_NORMALIZATION_MS;
            if (dT > SIGNAL_LEVEL || dT < 0.0f) {
                this.currentValue = in;
                this.speed = 0.0f;
            } else {
                while (dT > 0.0f) {
                    step(Math.min(STEP_SIZE, dT), in);
                    dT -= STEP_SIZE;
                }
            }
        }
        this.lastReadingTime = time;
        if (this.wrap) {
            while (this.currentValue >= 360.0f) {
                this.currentValue -= 360.0f;
            }
            while (this.currentValue < 0.0f) {
                this.currentValue += 360.0f;
            }
        }
        return this.currentValue;
    }

    private void step(float dT, float in) {
        float diff = (in - this.currentValue) / SIGNAL_LEVEL;
        this.speed += (Math.abs(diff) * diff) * dT;
        if (this.speed != 0.0f) {
            float diffSpeedRatio = (diff * SIGNAL_LEVEL) / this.speed;
            float c = (float) ((1.0d * Math.exp((double) ((-diffSpeedRatio) * diffSpeedRatio))) + 0.5d);
            if (c * dT >= FRICTION_COEFFICIENT_1) {
                this.speed = 0.0f;
                return;
            }
            this.speed -= (this.speed * c) * dT;
            this.currentValue += this.speed * dT;
        }
    }
}
