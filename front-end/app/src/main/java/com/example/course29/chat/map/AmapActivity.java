package com.example.course29.chat.map;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.example.course29.R;
import com.example.course29.chat.chatContent.ChatContentActivity;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class AmapActivity extends AppCompatActivity implements LocationSource {
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    private LocationUtil locationUtil;
    private ImageView mIvAMapReturn;
    private ImageView mIvAMapSend;
    private String longitude;
    private String latitude;
    private String mStrLinkId;
    private String mStrMultiple;
    private TextView mTvAMapText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

        Intent getIntent = getIntent();
        longitude = getIntent.getStringExtra("longitude");
        latitude = getIntent.getStringExtra("latitude");
        mStrLinkId = getIntent.getStringExtra("strLinkId");
        mStrMultiple = getIntent.getStringExtra("strMultiple");

        mIvAMapReturn = findViewById(R.id.iv_aMap_return);
        mTvAMapText = findViewById(R.id.tv_aMap_text);
        mIvAMapReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvAMapSend = findViewById(R.id.iv_aMap_send);

        if(!longitude.equals("") && !latitude.equals("")) {
            mIvAMapSend.setVisibility(View.GONE);
            mTvAMapText.setText("定位");
        }
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        init();

        mIvAMapSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("linkId",mStrLinkId);
                map.put("isMultiple",mStrMultiple);
                map.put("type",4);
                map.put("longitude",longitude);
                map.put("latitude",latitude);
                Map res = HttpUtil.post("/chat/sendMessage",map, AmapActivity.this);
                if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
                    finish();
                }
                else {
                    ToastUtil.showMsg(AmapActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });
    }

    private void init() {
        if(aMap == null){
            aMap = mapView.getMap();
        }

        setLocationCallBack();

        //设置定位监听
        aMap.setLocationSource(this);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //显示定位层并可触发，默认false
        aMap.setMyLocationEnabled(true);
    }

    private void setLocationCallBack(){
        locationUtil = new LocationUtil();
        if(!longitude.equals("") && !latitude.equals("")) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.valueOf(latitude),
                    Double.valueOf(longitude))));
            Log.e("str",Double.valueOf(longitude).toString());
            //添加定位图标
                aMap.addMarker(locationUtil.getMarkerOption("",Double.valueOf(latitude),
                        Double.valueOf(longitude),AmapActivity.this));
            return;
        }
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str,double lat,double lgt,AMapLocation aMapLocation) {
                //根据获取的经纬度，将地图移动到定位位置
                Log.e("str",str);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat,lgt)));
                mListener.onLocationChanged(aMapLocation);
                latitude = String.valueOf(lat);
                longitude = String.valueOf(lgt);
            }
        });
    }

    //定位激活回调
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

        locationUtil.startLocate(getApplicationContext());
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停地图的绘制
        mapView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新绘制加载地图
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}