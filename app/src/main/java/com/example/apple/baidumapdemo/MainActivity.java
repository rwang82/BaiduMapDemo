package com.example.apple.baidumapdemo;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
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

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Button mBtnSetMark = null;
    private Button mBtnCleanMark = null;
    private EditText mETLatitude = null;
    private EditText mETLongitude = null;
    private TextView mTVAddr = null;
    private android.os.Handler mRecvHandler = null;
    private LocationManager mLctMgr = null;
//    public LocationClient mLocationClient = null;

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
        //
        findViewById( R.id.btn_location ).setOnClickListener( mOnClickLocation );
        //
        mTVAddr = (TextView)findViewById( R.id.tv_addr );
        //
        mLctMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE );

        //
        mRecvHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch ( msg.what ) {
                    case 0x08:
                    {
                        String strAddr = msg.getData().getString( "Addr" );
                        MainActivity.this.mTVAddr.setText( strAddr );
                    } break;
                    case 0x09:
                    {
                        double dLat = msg.getData().getDouble( "Latitude" );
                        double dLng = msg.getData().getDouble( "Longitude" );
                        mETLatitude.setText( String.valueOf( dLat ) );
                        mETLongitude.setText( String.valueOf( dLng ) );
                    } break;
                    default:
                        break;
                }
            }
        };

        //
        //
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener( mBDLocationListen );
//        mLocationClient.start();
//        mLocationClient.requestLocation();
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
            String strAddr = reverseGeoCodeResult.getAddress();

            Message msg = new Message();
            msg.what = 0x08;
            Bundle bundle = new Bundle();
            bundle.putString("Addr", strAddr);
            msg.setData(bundle);
            mRecvHandler.sendMessage( msg );
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


            //
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target( point )
                    .zoom(18)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.animateMapStatus(mMapStatusUpdate);

        }
    };

    private View.OnClickListener mOnClickBtnCleanMark = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int a = 0;
        }
    };


    private LocationListener mLCTListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            mETLatitude.setText( String.valueOf( location.getLatitude() )  );
            mETLongitude.setText( String.valueOf( location.getLongitude() )  );
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            int a = 0;
        }

        @Override
        public void onProviderEnabled(String s) {
            int a = 0;
        }

        @Override
        public void onProviderDisabled(String s) {
            int a = 0;
        }
    };
    private View.OnClickListener mOnClickLocation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Criteria crtData = new Criteria();
            crtData.setAccuracy( Criteria.ACCURACY_COARSE );
            String strLP = MainActivity.this.mLctMgr.getBestProvider( crtData, true );
            if ( strLP == null ) {
                crtData.setAccuracy( Criteria.ACCURACY_COARSE );
                strLP = MainActivity.this.mLctMgr.getBestProvider( crtData, true );
            }
            if ( strLP == null ) {
                crtData.setAccuracy( Criteria.ACCURACY_LOW );
                List<String> listLP = MainActivity.this.mLctMgr.getProviders( crtData, true );
                if ( listLP.size() == 0 ) {
                    listLP = MainActivity.this.mLctMgr.getProviders( true );
                    if ( listLP.size() == 0 ) {
                        listLP = MainActivity.this.mLctMgr.getAllProviders();

                        if ( listLP.size() == 0 ) {
                            int a = 0;
                            return;
                        }
                    }
                }

                strLP = listLP.get( listLP.size() - 1 );
            }

            mLctMgr.requestLocationUpdates( strLP, 1000, 10, mLCTListener);
            Location location = MainActivity.this.mLctMgr.getLastKnownLocation(strLP);
            if ( location == null ) {
                mLctMgr.requestSingleUpdate( strLP, mLCTListener, null );
                return;
            }

            mETLatitude.setText( String.valueOf( location.getLatitude() )  );
            mETLongitude.setText( String.valueOf( location.getLongitude() )  );
        }
    };

//    private BDLocationListener mBDLocationListen = new BDLocationListener() {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            if (location == null)
//                return ;
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation){
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//            }
//
//            Log.i( "info", sb.toString());
//
//            ///
//            Message msg = new Message();
//            msg.what = 0x09;
//            Bundle bundle = new Bundle();
//
//            bundle.putDouble( "Latitude", location.getLatitude() );
//            bundle.putDouble( "Longitude", location.getLongitude() );
//            msg.setData(bundle);
//            mRecvHandler.sendMessage( msg );
//
//        }
//    };


}
