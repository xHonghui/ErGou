package com.laka.androidlib.util;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * @Author:Rayman
 * @Date:2019/2/28
 * @Description:摇一摇工具类
 */

public class ShakeUtils implements SensorEventListener {

    private SensorManager mSensorManager = null;
    private OnShakeListener mOnShakeListener = null;
    private static final int SENSOR_VALUE = 14;
    private Vibrator vibrator;
    private long lastSensorTime = System.currentTimeMillis();

    public ShakeUtils(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            //这里可以调节摇一摇的灵敏度
            if ((Math.abs(values[0]) > SENSOR_VALUE || Math.abs(values[1]) > SENSOR_VALUE || Math.abs(values[2]) > SENSOR_VALUE)) {
                System.out.println("sensor value == " + " " + values[0] + " " + values[1] + " " + values[2]);
                if (null != mOnShakeListener) {
                    // 限制一秒内只回调一次.
                    if (System.currentTimeMillis() - lastSensorTime > 1000) {
                        mOnShakeListener.onShake();
                        lastSensorTime = System.currentTimeMillis();
                    }
                    vibrator.vibrate(300);
                }
            }
        }
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        mOnShakeListener = onShakeListener;
    }

    public void release() {
        mSensorManager.unregisterListener(this);
        mOnShakeListener = null;
    }

    public interface OnShakeListener {

        /**
         * 摇一摇回调
         */
        void onShake();
    }
}
