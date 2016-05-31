package com.psj.searchbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psj on 2016/5/30.
 */
public class MapActivity extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener {
    MapView mMapView = null;
    //    PoiSearch mPoiSearch = null;
    Button bt = null;
    TextView text = null;

    // 搜索相关
    private PoiSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BusLineSearch mBusLineSearch = null;
    BusLineOverlay overlay;

    private int busLineIndex = 0;
    private List<String> busLineIDList = null;
    private BusLineResult route = null; // 保存驾车/步行路线数据的变量，供浏览节点时使用
    private BaiduMap baiduMap = null;
    private int nodeIndex = -2; // 节点索引,供浏览节点时使用
    private EditText cityName, routeName;
    private String city = null;
    private String busNumber = null;
    private ListView listView;
    private List<String> list;
    private ListviewAdapter listviewAdapter;
    Button switcMode;
    private int modeState = 0;

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            TextView text = (TextView) findViewById(R.id.text);
            text.setTextColor(Color.RED);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                text.setText("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s
                    .equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                text.setText("key 验证成功! 功能可以正常使用");
//                text.setTextColor(Color.BLUE);
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                text.setText("网络出错");
            }
        }
    }

    private SDKReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        list = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);
        LayoutInflater inflater = getLayoutInflater();
        listviewAdapter = new ListviewAdapter(inflater, this);
        listView.setAdapter(listviewAdapter);
        listView.setDividerHeight(10);

        switcMode = (Button) findViewById(R.id.switch_mode);
        switcMode.setOnClickListener(this);

        bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();
        cityName = (EditText) findViewById(R.id.city_name);
        routeName = (EditText) findViewById(R.id.route_name);
        //设置地图类型
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //交通图显示
        baiduMap.setTrafficEnabled(true);

        busLineIDList = new ArrayList<String>();

        mSearch = PoiSearch.newInstance();
        mSearch.setOnGetPoiSearchResultListener(this);
        mBusLineSearch = BusLineSearch.newInstance();
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);
        overlay = new BusLineOverlay(baiduMap);
        baiduMap.setOnMarkerClickListener(overlay);

    }

    public void searchNextBusline(View v) {
        if (busLineIndex >= busLineIDList.size()) {
            busLineIndex = 0;
        }
        if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
                && busLineIDList.size() > 0) {
            mBusLineSearch.searchBusLine((new BusLineSearchOption()
                    .city(city).uid(busLineIDList.get(busLineIndex))));

            busLineIndex++;
        }

    }

    @Override
    protected void onDestroy() {
//        mPoiSearch.destroy();
        mBusLineSearch.destroy();
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view == bt) {
            busLineIDList.clear();
            busLineIndex = 0;

            if (TextUtils.isEmpty(cityName.getText())) {
                city = "";
            } else {
                city = cityName.getText() + "";
            }
            if (TextUtils.isEmpty(routeName.getText())) {
                busNumber = "";
            } else {
                busNumber = routeName.getText() + "";
            }
            // 发起poi检索，从得到所有poi中找到公交线路类型的poi，再使用该poi的uid进行公交详情搜索
            mSearch.searchInCity((new PoiCitySearchOption()).city(
                    city)
                    .keyword(busNumber));
        } else if (view == switcMode) {
            if (modeState == 1) {
                modeState = 0;
                mMapView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                switcMode.setText("显示地图");
            } else if (modeState == 0) {
                modeState = 1;
                mMapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                switcMode.setText("显示名称");
            }
        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        for (PoiInfo poi : result.getAllPoi()) {
            if (poi.type == PoiInfo.POITYPE.BUS_LINE
                    || poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                busLineIDList.add(poi.uid);
                Log.e("result", poi.uid);
            }
        }
        searchNextBusline(null);
//        route = null;
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetBusLineResult(BusLineResult busLineResult) {
//        Log.e("name", "bt");
        if (busLineResult == null || busLineResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        baiduMap.clear();
        route = busLineResult;
        nodeIndex = -1;
        overlay.removeFromMap();
        overlay.setData(busLineResult);
        overlay.addToMap();
        overlay.zoomToSpan();
        Toast.makeText(MapActivity.this, busLineResult.getBusLineName(),
                Toast.LENGTH_SHORT).show();
        for (int i = 0; i < busLineResult.getStations().size(); i++) {
            Log.e("name", busLineResult.getStations().get(i).getTitle());
            list.add(busLineResult.getStations().get(i).getTitle());
        }
        listviewAdapter.setData(list);
        listviewAdapter.notifyDataSetChanged();
    }
}
