package com.example.dsm2016.compass;

import android.os.Bundle;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static java.lang.Math.abs;

public class MainActivity extends Activity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    // Rotation Matrix로 동쪽으로 향한 장치의 X축과 남쪽으로 향한 장치의 Y축의 값
    private float[] mOrientation = new float[3]; // Matrix를 통해 azimuth, pitch, roll를 구한 값
    private float mOrientation1; // 1 = north, 2 = east, 3 = south, 4 = west
    private int direction;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //가속도 센서
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); // 방향 센서
    }

    protected void onResume() { // registerListener on
        super.onResume();
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) { // 센서값을 읽어오는 부분
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            mOrientation1 = mOrientation[0];
             // 방향 판별
            if(mOrientation1 > 0.3){
                direction = 2;
            }
            else if(mOrientation1 >= -0.3 && mOrientation1 <= 0.3) {
                direction = 1;
            }
            else if(mOrientation1 < -0.3){
                direction = 3;
            }
            else{
                Log.e("error", String.format("error"));
            }
            Log.i("OrientationTestActivity", String.format("Orientation: %f, %f, %d",
                    mOrientation1, abs(mOrientation[0])*60, direction)); // 센서값, 각도, 방향
        }
    }
}