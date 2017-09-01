package com.example.dsm2016.smartcartver4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class searchResult extends AppCompatActivity {

    private Intent intent;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        intent = getIntent();
        searchText = intent.getExtras().getString("search");

        Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG).show();
    }
}
