package com.example.apple.baidumapdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;


public class MainActivity extends ActionBarActivity {

    private MapView mMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
