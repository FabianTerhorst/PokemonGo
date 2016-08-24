package com.nianticlabs.nia.sensors;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.view.Display;
import android.view.WindowManager;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.ServiceStatus;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.R;

public class NianticSensorManager extends ContextService implements SensorEventListener {
    private static final float ANGLE_CHANGE_THRESHOLD_DEGREES = 1.0f;
    private static final int DECLINATION_UPDATE_INTERVAL_MSEC = 600000;
    private static final boolean ENABLE_VERBOSE_LOGS = false;
    private static final int MAX_SENSOR_UPDATE_DIFF_MSEC = 5000;
    private static final int MIN_SENSOR_UPDATE_INTERVAL_MSEC = 50;
    private static final float SINE_OF_45_DEGREES = (((float) Math.sqrt(2.0d)) / Default.HTTP_REQUEST_BACKOFF_MULT);
    private static final String TAG = "NianticSensorManager";
    private Sensor accelerometer;
    private float[] accelerometerData = new float[3];
    private long accelerometerReadingMs;
    private float declination;
    private long declinationUpdateTimeMs;
    private final Display display;
    private Sensor gravity;
    private Sensor gyroscope;
    private float lastAzimuthUpdate;
    private float lastPitchUpdate;
    private long lastUpdateTimeMs;
    private Sensor linearAcceleration;
    private Sensor magnetic;
    private float[] magneticData = new float[3];
    private long magnetometerReadingMs;
    private final AngleFilter orientationFilter = new AngleFilter(true);
    private Sensor rotation;
    private float[] rotationData = new float[5];
    private final SensorManager sensorManager;
    private ServiceStatus status = ServiceStatus.UNDEFINED;
    private final float[] tmpMatrix1 = new float[9];
    private final float[] tmpMatrix2 = new float[9];
    private final float[] tmpMatrix3 = new float[9];
    private final float[] tmpOrientationAngles = new float[3];

    private native void nativeCompassUpdate(long j, float f);

    private native void nativeSensorUpdate(int i, long j, float[] fArr);

    public NianticSensorManager(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.sensorManager = (SensorManager) context.getSystemService("sensor");
        this.gravity = this.sensorManager.getDefaultSensor(9);
        this.gyroscope = this.sensorManager.getDefaultSensor(4);
        this.accelerometer = this.sensorManager.getDefaultSensor(1);
        this.magnetic = this.sensorManager.getDefaultSensor(2);
        this.rotation = this.sensorManager.getDefaultSensor(11);
        this.linearAcceleration = this.sensorManager.getDefaultSensor(10);
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onResume() {
        startSensorManager();
    }

    public void onPause() {
        stopSensorManager();
    }

    private void startSensorManager() {
        if (this.gravity != null) {
            this.sensorManager.registerListener(this, this.gravity, 3, ContextService.getServiceHandler());
        }
        if (this.gyroscope != null) {
            this.sensorManager.registerListener(this, this.gyroscope, 3, ContextService.getServiceHandler());
        }
        if (this.accelerometer != null) {
            this.sensorManager.registerListener(this, this.accelerometer, 2, ContextService.getServiceHandler());
        }
        if (this.magnetic != null) {
            this.sensorManager.registerListener(this, this.magnetic, 2, ContextService.getServiceHandler());
        }
        if (this.rotation != null) {
            this.sensorManager.registerListener(this, this.rotation, 2, ContextService.getServiceHandler());
        }
        if (this.linearAcceleration != null) {
            this.sensorManager.registerListener(this, this.linearAcceleration, 3, ContextService.getServiceHandler());
        }
        this.status = ServiceStatus.INITIALIZED;
    }

    private void stopSensorManager() {
        this.sensorManager.unregisterListener(this);
        this.status = ServiceStatus.STOPPED;
    }

    public void onSensorChanged(SensorEvent event) {
        this.status = ServiceStatus.RUNNING;
        long now = System.currentTimeMillis();
        switch (event.sensor.getType()) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                this.accelerometerReadingMs = now;
                System.arraycopy(event.values, 0, this.accelerometerData, 0, this.accelerometerData.length);
                if (updateOrientationFromRaw(now)) {
                    safeCompassUpdate(this.lastUpdateTimeMs, this.lastAzimuthUpdate);
                    break;
                }
                break;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                this.magnetometerReadingMs = now;
                System.arraycopy(event.values, 0, this.magneticData, 0, this.magneticData.length);
                if (updateOrientationFromRaw(now)) {
                    safeCompassUpdate(this.lastUpdateTimeMs, this.lastAzimuthUpdate);
                    break;
                }
                break;
            case 11:
                System.arraycopy(event.values, 0, this.rotationData, 0, Math.min(event.values.length, this.rotationData.length));
                if (event.values.length == 3) {
                    this.rotationData[3] = computeRotationVectorW(this.rotationData);
                }
                if (updateOrientationFromRotation(now)) {
                    safeCompassUpdate(this.lastUpdateTimeMs, this.lastAzimuthUpdate);
                    break;
                }
                break;
        }
        safeSensorUpdate(event.sensor.getType(), now, event.values);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private boolean updateOrientationFromRotation(long now) {
        if (this.lastUpdateTimeMs + 50 > now) {
            return ENABLE_VERBOSE_LOGS;
        }
        float[] matrix = this.tmpMatrix1;
        calcMatrixFromRotationVector(this.rotationData, matrix);
        return updateOrientation(now, matrix);
    }

    private void calcMatrixFromRotationVector(float[] reading, float[] matrix) {
        float q0 = reading[3];
        float q1 = reading[0];
        float q2 = reading[1];
        float q3 = reading[2];
        float sq_q1 = (Default.HTTP_REQUEST_BACKOFF_MULT * q1) * q1;
        float sq_q2 = (Default.HTTP_REQUEST_BACKOFF_MULT * q2) * q2;
        float sq_q3 = (Default.HTTP_REQUEST_BACKOFF_MULT * q3) * q3;
        float q1_q2 = (Default.HTTP_REQUEST_BACKOFF_MULT * q1) * q2;
        float q3_q0 = (Default.HTTP_REQUEST_BACKOFF_MULT * q3) * q0;
        float q1_q3 = (Default.HTTP_REQUEST_BACKOFF_MULT * q1) * q3;
        float q2_q0 = (Default.HTTP_REQUEST_BACKOFF_MULT * q2) * q0;
        float q2_q3 = (Default.HTTP_REQUEST_BACKOFF_MULT * q2) * q3;
        float q1_q0 = (Default.HTTP_REQUEST_BACKOFF_MULT * q1) * q0;
        matrix[0] = (ANGLE_CHANGE_THRESHOLD_DEGREES - sq_q2) - sq_q3;
        matrix[1] = q1_q2 - q3_q0;
        matrix[2] = q1_q3 + q2_q0;
        matrix[3] = q1_q2 + q3_q0;
        matrix[4] = (ANGLE_CHANGE_THRESHOLD_DEGREES - sq_q1) - sq_q3;
        matrix[5] = q2_q3 - q1_q0;
        matrix[6] = q1_q3 - q2_q0;
        matrix[7] = q2_q3 + q1_q0;
        matrix[8] = (ANGLE_CHANGE_THRESHOLD_DEGREES - sq_q1) - sq_q2;
    }

    private float computeRotationVectorW(float[] xyz) {
        float sumOfSquares = SINE_OF_45_DEGREES;
        for (float v : xyz) {
            sumOfSquares += v * v;
        }
        return (float) Math.sqrt((double) (ANGLE_CHANGE_THRESHOLD_DEGREES - Math.min(sumOfSquares, ANGLE_CHANGE_THRESHOLD_DEGREES)));
    }

    private boolean updateOrientationFromRaw(long now) {
        if (this.lastUpdateTimeMs + 50 > now || Math.abs(this.accelerometerReadingMs - this.magnetometerReadingMs) > 5000) {
            return ENABLE_VERBOSE_LOGS;
        }
        float[] matrix = this.tmpMatrix1;
        if (SensorManager.getRotationMatrix(matrix, null, this.accelerometerData, this.magneticData)) {
            return updateOrientation(now, matrix);
        }
        return ENABLE_VERBOSE_LOGS;
    }

    private boolean updateOrientation(long now, float[] matrix) {
        int xAxis;
        int yAxis;
        switch (this.display.getRotation()) {
            case R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                xAxis = 2;
                yAxis = 129;
                break;
            case R.styleable.LoadingImageView_circleCrop /*2*/:
                xAxis = 129;
                yAxis = 130;
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                xAxis = 130;
                yAxis = 1;
                break;
            default:
                xAxis = 1;
                yAxis = 2;
                break;
        }
        float[] tmpAngles = this.tmpOrientationAngles;
        if (!SensorManager.remapCoordinateSystem(matrix, xAxis, yAxis, this.tmpMatrix2)) {
            return ENABLE_VERBOSE_LOGS;
        }
        float pitch;
        if (this.tmpMatrix2[7] <= SINE_OF_45_DEGREES) {
            SensorManager.getOrientation(this.tmpMatrix2, tmpAngles);
            pitch = (float) Math.toDegrees((double) tmpAngles[1]);
        } else if (!SensorManager.remapCoordinateSystem(this.tmpMatrix2, 1, 3, this.tmpMatrix3)) {
            return ENABLE_VERBOSE_LOGS;
        } else {
            SensorManager.getOrientation(this.tmpMatrix3, tmpAngles);
            pitch = ((float) Math.toDegrees((double) tmpAngles[1])) - 90.0f;
        }
        long j = now;
        float azimuth = this.orientationFilter.filter(j, MathUtil.RADIANS_TO_DEGREES * MathUtil.wrapAngle(tmpAngles[0] + (MathUtil.DEGREES_TO_RADIANS * getDeclination())));
        if (Math.abs(azimuth - this.lastAzimuthUpdate) < ANGLE_CHANGE_THRESHOLD_DEGREES && Math.abs(pitch - this.lastPitchUpdate) < ANGLE_CHANGE_THRESHOLD_DEGREES) {
            return ENABLE_VERBOSE_LOGS;
        }
        this.lastAzimuthUpdate = azimuth;
        this.lastPitchUpdate = pitch;
        this.lastUpdateTimeMs = now;
        return true;
    }

    private float getDeclination() {
        long now = System.currentTimeMillis();
        if (this.declinationUpdateTimeMs + MarketingContentStoreImpl.DEFAULT_TIME_TO_LIVE_MS > now) {
            Location location = null;
            if (location != null) {
                this.declinationUpdateTimeMs = now;
                this.declination = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), now).getDeclination();
            }
        }
        return this.declination;
    }

    private void safeCompassUpdate(long time, float azimuth) {
        synchronized (this.callbackLock) {
            nativeCompassUpdate(time, azimuth);
        }
    }

    private void safeSensorUpdate(int type, long timestamp, float[] values) {
        synchronized (this.callbackLock) {
            nativeSensorUpdate(type, timestamp, values);
        }
    }
}
