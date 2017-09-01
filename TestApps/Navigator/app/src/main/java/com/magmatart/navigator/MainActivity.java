/* OverlayActivity.java - Last modified : 2017.08.27 */

package com.magmatart.navigator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity {

    public static final String TAG = "OverlayActivity";

    private double mCoordX = 0;             //현재 X 좌표
    private double mCoordY = 500;           //현재 Y 좌표
    private final double tCoordX = 600;     //Target X 좌표
    private final double tCoordY = 400;     //Target Y 좌표
    ImageView imageToRotate;                //네비게이터 이미지

    int degree = 0;                             //좌표
    int pDegree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageToRotate = findViewById(R.id.nav_image);

        checkUpdate.start();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private Thread checkUpdate = new Thread(){
        public void run(){
            super.run();

            try{
                while(true){
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){}

                    //임의로 X좌표 10 이동
                    mCoordX += 10;

                    //각도 계산
                    pDegree = degree;
                    degree = (int)Math.toDegrees(Math.acos( (Math.abs(tCoordY-mCoordY) / (Math.sqrt( (Math.abs(tCoordX-mCoordX)) * (Math.abs(tCoordX-mCoordX)) + (Math.abs(tCoordY-mCoordY)) * (Math.abs(tCoordY-mCoordY)) )))));
                    //degree = 90 - degree;
                    //degree += 270;

                    //현재 위치 기준 사분면 처리. 확정되지 않아 일단 아무 처리도 하지 않음
                    if(tCoordX-mCoordX >= 0 && tCoordY-mCoordY >= 0) ;
                    else if((tCoordX==mCoordX) && (mCoordY>tCoordY)) degree = 180;
                    else if(tCoordX-mCoordX >= 0 && tCoordY-mCoordY <= 0) degree  = (90-degree) + 90;
                    else if(tCoordX-mCoordX <= 0 && tCoordY-mCoordY <= 0) degree += 180;
                    else if(tCoordX-mCoordX <= 0 && tCoordY-mCoordY >= 0) degree  = (90-degree) + 270;

                    Log.d(TAG, ""+degree);

                    /*
                    if(pDegree < 90)
                        degree = 90 - degree;
                    if(pDegree >= 90)
                        degree += 90;
                    if(pDegree >= 180)
                        degree += 180;
                    if(pDegree >= 270)
                        degree += 270;
                    if(pDegree >= 360)
                        degree -= 360;
                        */

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //degree = 135;
                            //실제 회전
                            imageToRotate.setImageBitmap(rotateImage(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), degree));
                        }
                    });
                }
            }catch (Exception e){}
        }
    };

    public Bitmap rotateImage(Bitmap src, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
}