package com.example.dsm2016.smartcartver4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class search extends AppCompatActivity {

    Intent homeIntent;
    Intent intent;

    EditText editText;

    String inputText;

    ImageButton imageButton;
    ImageButton homeButton;
    ImageButton searchButton;
    ImageButton resultButton;
    ImageButton infoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        homeIntent = new Intent(this, Home.class);
        intent = new Intent(this, SurroundingItemsViewer.class);

        editText = (EditText)findViewById(R.id.editText);

        homeButton = (ImageButton)findViewById(R.id.homeButton);
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        resultButton = (ImageButton)findViewById(R.id.resultButton);
        infoButton = (ImageButton)findViewById(R.id.infoButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(homeIntent);
            }
        });

        addListenerOnButton();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;

                if(actionId == EditorInfo.IME_ACTION_SEND) {
                    inputText = editText.getText().toString();

                    intent.putExtra("search", inputText);

                    Toast.makeText(getApplicationContext(), inputText, Toast.LENGTH_LONG).show();
                   // startActivity(intent);
                }

                return handle;
            }
        });
    }

    public void addListenerOnButton(){
        imageButton = (ImageButton)findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

    }

}
