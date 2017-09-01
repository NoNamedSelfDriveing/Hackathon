package com.example.dsm2016.smartcartver4;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


//class ListViewAdapter extends BaseAdapter {
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }
//}

class ListItem {
    int profile_image;
    String title, code;
    double coordX, coordY;

    public ListItem(int profile_image, String title, String code, double coordX, double coordY) {
        this.profile_image = profile_image;
        this.title = title;
        this.code = code;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public int getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(int profile_image) {
        this.profile_image = profile_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

class ListViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<ListItem> itemsArrayList;

    ImageView itemsImageView;
    TextView itemsTextView;

    public ListViewAdapter(Context context, ArrayList<ListItem> itemsArrayList) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public int getCount() {
        return this.itemsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.surrounding_items, null);
            itemsImageView = (ImageView)convertView.findViewById(R.id.itemsImageView);
            itemsTextView = (TextView)convertView.findViewById(R.id.itemsTextView);
        }
        itemsImageView.setImageResource(itemsArrayList.get(position).getProfile_image());
        itemsTextView.setText(itemsArrayList.get(position).getTitle());
        return convertView;
    }
}

public class SurroundingItemsViewer extends AppCompatActivity {
    private Handler mhandler = null;
    private Intent intent;
    private String searchText;
    SensorManager mSensorManager;
    PositionManager posManager;

    ListView itemsListView;
    ListViewAdapter listViewAdapter;
    ArrayList<ListItem> itemsArrayList;
    //주변에 있을 시 표시할 마트 물품들
    ListItem[] items = new ListItem[8]; //pizza, chicken, toothbrush, bleach, lego, hippo, multitap, usb;
    double[] itemDistance = new double[8];    //각 아이템과의 거리
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //posManager = new PositionManager(this);
        itemsListView = (ListView)findViewById(R.id.itemsListView);
        itemsArrayList = new ArrayList<ListItem>();
        //itemsArrayList.add(new ListItem(R.drawable.pizza, "섹스"));
        listViewAdapter = new ListViewAdapter(this, itemsArrayList);
        itemsListView.setAdapter(listViewAdapter);

        //리스트뷰 터치 리스너 등록
//        itemsListView.setOnItemClickListener(mItemClickListener);
        posManager = new PositionManager(this);
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);

        //각 item들 위치 좌표 및 이미지 삽입
        ItemsCoordSet();

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    CalcDistance();
                    //mhandler.sendEmptyMessage(1);
                    SystemClock.sleep(50);
                }
            }
        });
        t.start();



        //각각 아이템들과의 거리 계산

//            for(;;) {
//                posManager.CalcDistance(items, itemDistance);
//                Log.i("coordX", Double.toString(posManager.coordX));
//                Log.i("coordY", Double.toString(posManager.coordY));
//                SystemClock.sleep(500);
//            }

        //positionManager = (PositionManager)getIntent().getSerializableExtra("result");
//        itemsArrayList.add(pizza);
//        itemsArrayList.add(chicken);
//        itemsArrayList.add(toothbrush);
//        itemsArrayList.add(bleach);
//        itemsArrayList.add(lego);
//        itemsArrayList.add(hippo);
//        itemsArrayList.add(multitap);
//        itemsArrayList.add(usb);//
        //itemsArrayList.remove(items[0]);
        //itemsArrayList.add(pizza);
        //itemsArrayList.add(chicken);
        //itemsArrayList.remove(pizza);
        //while(true)
         //   Log.i("coordX", Double.toString(posManager.coordX));
    }

    //리스트뷰 아이템 클릭 리스너
//    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            ListItem selectedItem = (ListItem)parent.getAdapter().getItem(position);
            //Intent intent = new Intent(SurroundingItemsViewer.this, info.class);
//            intent.putExtra("name", selectedItem.code);
//            startActivity(intent);
//        }
//    };

    //센서 등록
    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(posManager, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
    }

    //센서 해제
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(posManager);
    }

    //각 item들의 위치 좌표와 이미지를 대입하는 메소드
    void ItemsCoordSet() {
        items[0] = new ListItem(R.drawable.pizza, "슈퍼사이즈 피자", "pizza" ,900, 180);
        items[1] = new ListItem(R.drawable.chicken, "국내산 신선육 통큰 치킨", "chicken", 1000, 180);
        items[2] = new ListItem(R.drawable.toothbrush, "독일산 칫솔(부드러운 모)", "toothbrush", 200, 180);
        items[3] = new ListItem(R.drawable.bleach, "때가 쏙쏙 표백제", "bleach", 300, 180);
        items[4] = new ListItem(R.drawable.lego, "레고 닌자고", "lego", 200, 100);
        items[5] = new ListItem(R.drawable.hippo, "Hippo Hippo", "hippo", 300, 100);
        items[6] = new ListItem(R.drawable.multitap, "절전형 3구 멀티탭", "multitap", 900, 120);
        items[7] = new ListItem(R.drawable.usb, "USB 16GB", "usb", 1000, 120);
    }

    //카트와 각 물품들까지의 거리 측정
    void CalcDistance() {
        for(int i = 0; i < items.length; i++) {
            itemDistance[i] = Math.sqrt(Math.pow(posManager.coordX - items[i].coordX, 2) + Math.pow(posManager.coordY - items[i].coordY, 2));
            Log.i("distance", Double.toString(itemDistance[0]));
            if(itemDistance[i] < 500.0)
                itemsArrayList.add(items[i]);
            else
                itemsArrayList.remove(items[i]);
        }
    }

}
