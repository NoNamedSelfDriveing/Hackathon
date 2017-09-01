package com.aquaman.smartcart_wifi;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SensorManager mSensorManager;
    PositionManager posManager;
    SurroundingItemsViewer itemsViewer;
    //AccemeterListener eventListener;
    //LayoutInflater inflater = getLayoutInflater();
    TextView tvAcceleration, tvVelocity, tvDistance, tvOrientation, tvTurnedDegree;
    ArrayAdapter adapter;
    ListView listview;
    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAcceleration = (TextView)findViewById(R.id.acceleromter);
        tvVelocity = (TextView)findViewById(R.id.velocity);
        tvDistance = (TextView)findViewById(R.id.distance);
        tvOrientation = (TextView)findViewById(R.id.orientation);
        tvTurnedDegree = (TextView)findViewById(R.id.turnedDegree);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, LIST_MENU);
        listview.setAdapter(adapter);
        posManager = new PositionManager(this);
        itemsViewer = new SurroundingItemsViewer(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(posManager);
    }
}
