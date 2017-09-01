package com.example.dsm2016.smartcartver4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.ArrayList;

public class Home extends AppCompatActivity implements Serializable{

    //private Intent searchIntent, surroundingItemsViewerIntent;
    //SensorManager mSensorManager;
    //PositionManager posManager;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> alName;
    ArrayList<Integer> alImage;

    ImageButton homeButton;
    ImageButton searchButton;
    ImageButton resultButton;
    ImageButton infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //searchIntent = new Intent(this, search.class);
        //surroundingItemsViewerIntent = new Intent(this, SurroundingItemsViewer.class);

        //mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
       // posManager = new PositionManager(this);
        homeButton = (ImageButton)findViewById(R.id.homeButton);
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        resultButton = (ImageButton)findViewById(R.id.resultButton);
        infoButton = (ImageButton)findViewById(R.id.infoButton);

        //
        //Intent intent = getIntent();
        //posManager = (PositionManager)intent.getSerializableExtra("Home");

        //search 탭 이동 시 PositionManager 넘기기
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, search.class);
                //intent.putExtra("search", posManager);
                startActivity(intent);
            }
        });

        //result 탭 이동 시 PositionManager 넘기기
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SurroundingItemsViewer.class);
                //intent.putExtra("result", posManager);
                startActivity(intent);
            }
        });


//        alName = new ArrayList<>(Arrays.asList("Cheesy...", "Crispy... ", "Fizzy...", "Cool...", "Softy...", "Fruity...", "Fresh...", "Sticky..."));
//        alImage = new ArrayList<>(Arrays.asList(R.drawable.cart3, R.drawable.cheesy, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3));
//
//        // Calling the RecyclerView
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//
//        // The number of Columns
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new HLVAdapter(Home.this, alName, alImage);
//        mRecyclerView.setAdapter(mAdapter);
//
//        Intent intent = new Intent(this, search.class);
//        startActivity(intent);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
//        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(posManager);
//    }
}
