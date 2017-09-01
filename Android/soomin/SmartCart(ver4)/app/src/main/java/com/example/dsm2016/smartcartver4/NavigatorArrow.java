package com.example.dsm2016.smartcartver4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * Created by magma on 2017-09-01.
 */

public class NavigatorArrow {
    private ImageView img;
    public int mCoordX, mCoordY, tCoordX, tCoordY;
    private int degree;
    Context context;

    public NavigatorArrow(Context ctxt, ImageView iv, int mCoordX, int mCoordY, int tCoordX, int tCoordY){
        img = iv;
        context = ctxt;
        this.mCoordX = mCoordX;
        this.mCoordY = mCoordY;
        this.tCoordX = tCoordX;
        this.tCoordY = tCoordY;
    }

    public void rotateArrow(int degree){
        img.setImageBitmap(rotateImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow3), degree));
    }

    public Bitmap rotateImage(Bitmap src, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public int getDegree(){
        degree = (int)Math.toDegrees(Math.acos( (Math.abs(tCoordY-mCoordY) / (Math.sqrt( (Math.abs(tCoordX-mCoordX)) * (Math.abs(tCoordX-mCoordX)) + (Math.abs(tCoordY-mCoordY)) * (Math.abs(tCoordY-mCoordY)) )))));

        if(tCoordX-mCoordX >= 0 && tCoordY-mCoordY >= 0) ;
        else if((tCoordX==mCoordX) && (mCoordY>tCoordY)) degree = 180;
        else if(tCoordX-mCoordX >= 0 && tCoordY-mCoordY <= 0) degree  = (90-degree) + 90;
        else if(tCoordX-mCoordX <= 0 && tCoordY-mCoordY <= 0) degree += 180;
        else if(tCoordX-mCoordX <= 0 && tCoordY-mCoordY >= 0) degree  = (90-degree) + 270;

        return degree;
    }
}
