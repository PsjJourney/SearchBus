package com.psj.searchbus;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.poi.PoiAddrInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MapView mMapView = null;
    public List<PoiAddrInfo> list = new ArrayList<>();
    Button button = null;
    private Intent intent;
    private int RESULT_LOAD_IMAGE;
    Button buttonLoadPicture = null;
    ImageView imgView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
//        setContentView(R.layout.activity_main);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //由于主要是用于测试，并且便于新手理解，所以activity_main布局写的很简单
        setContentView(R.layout.xiangce_layout);
        imgView = (ImageView) findViewById(R.id.imgView);
        buttonLoadPicture = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
                Dialog dialog = new ListviewDialog(MainActivity.this);
                dialog.show();
            }
        });


//        intent = new Intent(this, LongRunningService.class);
        //开启关闭Service
//        startService(intent);

        //设置一个Toast来提醒使用者提醒的功能已经开始
//        Toast.makeText(MainActivity.this,"提醒的功能已经开启，关闭界面则会取消提醒。",Toast.LENGTH_LONG).show();
//        mMapView = (MapView) findViewById(R.id.bmapView);
//        button = (Button) findViewById(R.id.button);

//        BaiduMap baiduMap = mMapView.getMap();
        //设置地图类型
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //交通图显示
//        baiduMap.setTrafficEnabled(true);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PoiSearch mPoiSearch = PoiSearch.newInstance();
//                OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
//                    @Override
//                    public void onGetPoiResult(PoiResult poiResult) {
//                        list = poiResult.getAllAddr();
//                        for (int i = 0; i < list.size(); i++) {
//                            Log.e("poiResult", list.get(i).address);
//                        }
//                    }
//
//                    @Override
//                    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//                        Log.e("poiDetailResult", poiDetailResult.toString());
//                    }
//                };
//                mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//                mPoiSearch.searchInCity(new PoiCitySearchOption()
//                        .city("北京")
//                        .keyword("美食")
//                        .pageNum(0));
//            }
//        });
//        mPoiSearch.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imgView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mMapView.onPause();
    }
}
