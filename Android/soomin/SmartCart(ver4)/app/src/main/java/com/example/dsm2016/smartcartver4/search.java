package com.example.dsm2016.smartcartver4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;

public class search extends AppCompatActivity {

    private  int count = 0;

    Realm mRealm;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

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

        editText = (EditText)findViewById(R.id.editText);

        homeButton = (ImageButton)findViewById(R.id.homeButton);
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        resultButton = (ImageButton)findViewById(R.id.resultButton);
        infoButton = (ImageButton)findViewById(R.id.infoButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(search.this, Home.class);

            @Override
            public void onClick(View v) {
                finish();
                startActivity(intent);
            }
        });

        alName = new ArrayList<>();
        alImage = new ArrayList<>();

        addListenerOnButton();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;

                if(actionId == EditorInfo.IME_ACTION_SEND) {
                    alName.clear();
                    alImage.clear();

                    inputText = editText.getText().toString();
                    editText.setText("");

                    init();
                    RealmResults<ShoppingItem> userList = getUserList();

                    for(int i=0; i<userList.size(); i++){

                        if(inputText.contains(userList.get(i).getName())){

                            alName.add(count, userList.get(i).getName());
                            System.out.println(alName.get(count));

                            alImage.add(count, userList.get(i).getImage());

                            Toast.makeText(getApplicationContext(), inputText, Toast.LENGTH_SHORT).show();

                            count++;
                            inputText = "";
                        }
                    }
                    count = 0;


                    System.out.println("arr size : " + alName.size());
                    listActivity();

                }

                return handle;
            }
        });

        /*alName = new ArrayList<>(Arrays.asList("Cheesy...", "Crispy... ", "Fizzy...", "Cool...", "Softy...", "Fruity...", "Fresh...", "Sticky..."));
        alImage = new ArrayList<>(Arrays.asList(R.drawable.cart3, R.drawable.cheesy, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3, R.drawable.cart3));*/




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

    private void init(){
        Realm.init(getApplicationContext());
        mRealm = Realm.getDefaultInstance();
    }

    private RealmResults<ShoppingItem> getUserList() {
        return mRealm.where(ShoppingItem.class).findAll();
    }

    public void listActivity(){
        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HLVAdapter(search.this, alName, alImage);
        mRecyclerView.setAdapter(mAdapter);
    }


}
