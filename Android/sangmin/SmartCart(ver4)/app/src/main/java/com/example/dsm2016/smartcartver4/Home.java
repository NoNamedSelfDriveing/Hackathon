package com.example.dsm2016.smartcartver4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;

public class Home extends AppCompatActivity {
    private Realm mRealm;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;

    ArrayList <String> AName;
    ArrayList<Integer> AImage;
    ArrayList <Integer> APrice;

    ImageButton homeButton;
    ImageButton searchButton;
    ImageButton resultButton;
    ImageButton infoButton;
    ImageButton cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeButton = (ImageButton)findViewById(R.id.homeButton);
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        resultButton = (ImageButton)findViewById(R.id.resultButton);
        infoButton = (ImageButton)findViewById(R.id.infoButton);

        cartButton = (ImageButton)findViewById(R.id.cartButton);

        searchButton.setOnClickListener(new View.OnClickListener(){
            Intent intent = new Intent(Home.this, search.class);

            @Override
            public void onClick(View v) {
                finish();
                startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(Home.this, infomation.class);

            @Override
            public void onClick(View v) {
                finish();
                startActivity(intent);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(Home.this, ShoppingList.class);

            @Override
            public void onClick(View v) {
                finish();
                startActivity(intent);
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

        init();

        AName = new ArrayList<>(Arrays.asList("cheesy", "bleach", "chicken", "hippo", "lego", "multitap", "pizza", "toothbrush", "usb", "hippo2"));
        AImage = new ArrayList<>(Arrays.asList(R.drawable.cheesy, R.drawable.bleach, R.drawable.chicken, R.drawable.hippo, R.drawable.lego, R.drawable.multitap, R.drawable.pizza, R.drawable.toothbrush, R.drawable.usb, R.drawable.hippo));
        APrice = new ArrayList<>(Arrays.asList(1000, 10000, 19000, 5000, 50000, 3000, 8000, 2000, 15000, 5000));

        for(int i=0; i < AName.size(); i++){
            insertUserData(AName.get(i), AImage.get(i), APrice.get(i));
        }



//        intent = new Intent(this, search.class);
//        startActivity(intent);
    }

    private void init(){
        Realm.init(getApplicationContext());
        mRealm = Realm.getDefaultInstance();
    }

    private RealmResults<ShoppingItem> getUserList() {
        return mRealm.where(ShoppingItem.class).findAll();
    }

    private void insertUserData (String name, int image, int price){
        mRealm.beginTransaction();

        ShoppingItem user = mRealm.createObject(ShoppingItem.class);
        user.setName(name);
        user.setImage(image);
        user.setPrice(price);
        mRealm.commitTransaction();
    }
}
