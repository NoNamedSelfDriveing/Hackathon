package com.example.dsm2016.smartcartver4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

public class Home extends AppCompatActivity {

    private Intent searchIntent;

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

        searchIntent = new Intent(this, search.class);

        homeButton = (ImageButton)findViewById(R.id.homeButton);
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        resultButton = (ImageButton)findViewById(R.id.resultButton);
        infoButton = (ImageButton)findViewById(R.id.infoButton);

        searchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(searchIntent);
            }
        });

        alName = new ArrayList<>(Arrays.asList("Cheesy...", "Crispy... ", "Fizzy...", "Cool...", "Softy...", "Fruity...", "Fresh...", "Sticky..."));
        alImage = new ArrayList<>(Arrays.asList(R.drawable.cart3, R.drawable.cheesy, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3));

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HLVAdapter(Home.this, alName, alImage);
        mRecyclerView.setAdapter(mAdapter);

//        intent = new Intent(this, search.class);
//        startActivity(intent);
    }
}
