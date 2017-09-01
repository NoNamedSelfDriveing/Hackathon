package com.example.dsm2016.smartcartver4;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class infomation extends AppCompatActivity {

    private Realm mRealm;
    private static final String TAG = infomation.class.getSimpleName();

    Intent intent;

    int k=0;

    ImageButton homeButton;
    ImageButton searchButton;
    ImageButton resultButton;
    ImageButton infoButton;
    ImageButton cartButton;
    ImageButton cancelButton;
    ImageButton purchaseButton;

    TextView textName;
    TextView textPrice;
    TextView textCompany;

    ImageView arrow;

    NavigatorArrow nArrow;

    SensorManager mSensorManager;
    PositionManager posManager;

    int initialDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);

        init();

        textName = (TextView)findViewById(R.id.textName);
        textPrice = (TextView)findViewById(R.id.textPrice);
        textCompany = (TextView)findViewById(R.id.textCompany);

        homeButton = (ImageButton)findViewById(R.id.homeButton);
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        resultButton = (ImageButton)findViewById(R.id.resultButton);
        infoButton = (ImageButton)findViewById(R.id.infoButton);

        cartButton = (ImageButton)findViewById(R.id.cartButton);

        cancelButton = (ImageButton)findViewById(R.id.cancelButton);
        purchaseButton = (ImageButton)findViewById(R.id.purchaseButton);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        posManager = new PositionManager(this, true);

        arrow = (ImageView)findViewById(R.id.nav_arrow);

        nArrow = new NavigatorArrow(getApplicationContext(), arrow, 1200, 150, 700, 700);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nArrow.rotateArrow(nArrow.getDegree());
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(infomation.this, ShoppingList.class);

            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(infomation.this, Home.class);
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(infomation.this, search.class);

            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        NavigatorMove.start();
    }

    private void init(){
        Realm.init(getApplicationContext());
        mRealm = Realm.getDefaultInstance();

        getUserList();
    }

    private RealmResults<ShoppingItem> getUserList() {
       return mRealm.where(ShoppingItem.class).findAll();
    }

    @Override
    public void onResume(){
        super.onResume();

        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
    }

    public Thread NavigatorMove = new Thread(){
        @Override
        public void run() {
            super.run();

            initialDegree = nArrow.getDegree();

            while(true) {

                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nArrow.rotateArrow(initialDegree + (int)posManager.turnedDegree);
                    }
                });
            }
        }
    };

}
