package com.example.dsm2016.smartcartver4;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dsm2016 on 2017-09-02.
 */

public class ShoppingItem extends RealmObject {

    private String name;
    private int price;
    private int image;
    private String company;
    private boolean purchase = false;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public  void setImage(int image){
        this.image = image;
    }

    public int getImage(){
        return image;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public String getCompany(String company){
        return company;
    }

    public void setPurchase(boolean purchase){
        this.purchase = purchase;
    }

    public boolean getPurchase(){
        return purchase;
    }
}
