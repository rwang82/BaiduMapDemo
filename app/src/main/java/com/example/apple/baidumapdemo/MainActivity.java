package com.example.apple.baidumapdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;


public class MainActivity extends ActionBarActivity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Button mBtnSetMark = null;
    private Button mBtnCleanMark = null;
    private EditText mETLatitude = null;
    private EditText mETLongitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        //
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //
        mBtnSetMark = (Button) findViewById( R.id.btn_setmark );
        mBtnSetMark.setOnClickListener( mOnClickBtnSetMark );
        //
        mBtnCleanMark = (Button) findViewById( R.id.btn_clearmark );
        mBtnCleanMark.setOnClickListener( mOnClickBtnCleanMark );
        //
        mETLatitude = (EditText) findViewById( R.id.et_lat );
        mETLongitude = (EditText) findViewById( R.id.et_lng );
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

    private OnGetGeoCoderResultListener mOnGetGeoCoderResult = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            int a = 0;
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            int b = 0;
        }
    };
    private View.OnClickListener mOnClickBtnSetMark = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int a = 0;
            double dLat = Double.parseDouble( mETLatitude.getText().toString() );
            double dLng = Double.parseDouble( mETLongitude.getText().toString() );
            LatLng sourceLatLng = new LatLng( dLat, dLng );

            //
            CoordinateConverter converter  = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
            // sourceLatLng待转换坐标
            converter.coord( sourceLatLng );
            LatLng desLatLng = converter.convert();

            //



            //定义Maker坐标点
            LatLng point = new LatLng( desLatLng.latitude, desLatLng.longitude );
//构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_launcher);
//构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
//在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);



            // 查询街道信息
            String add = "";
            GeoCoder geoCoder = GeoCoder.newInstance();
            geoCoder.setOnGetGeoCodeResultListener( mOnGetGeoCoderResult );
            ReverseGeoCodeOption rgcOption = new ReverseGeoCodeOption();
            rgcOption.location( desLatLng );
            if( !geoCoder.reverseGeoCode( rgcOption ) ) {
                int m = 0;
            }
        }
    };

    private View.OnClickListener mOnClickBtnCleanMark = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int a = 0;
        }
    };
}
