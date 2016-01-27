package org.seven.activity;

import org.seven.utils.GlobalParam;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public abstract class MapActivity extends Activity implements BDLocationListener, OnClickListener {

	protected int resultCode = 0;
	
	protected MapView mMapView = null;
	
	private LocationClient mLocationClient = null;
	
	private String lastAddr = null;
	
	private Toast toast = null;
	
	protected boolean autoSpot = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	}
	
	public void InitMap(GeoPoint point)
	{
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		TextView v = (TextView) findViewById(R.id.message);
		Button t = (Button) findViewById(R.id.login);
		v.setVisibility(View.INVISIBLE);
		t.setVisibility(View.INVISIBLE);
		
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		
		mMapView=(MapView)findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		//设置启用内置的缩放控件
		MapController mMapController = mMapView.getController();
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
		mMapController.setCenter(point);//设置地图中心点  
		mMapController.setZoom(19);//设置地图zoom级别
		
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类

		mLocationClient.registerLocationListener(this);

		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式

		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02

		option.setScanSpan(10000);// 设置发起定位请求的间隔时间为5000ms

		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息

		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向

		mLocationClient.setLocOption(option);
		
		mLocationClient.start();
		
		toast = Toast.makeText(this, "正在获取当前位置", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		if (autoSpot)
		{
			toast.show();
		}
	}
	
	@Override
	protected void onDestroy(){
	        mMapView.destroy();
	        if (mLocationClient != null)
	        {
	        	mLocationClient.stop();
	        	mLocationClient = null;
	        }
	        
	        if (toast != null)
	        {
	        	toast.cancel();
	        	toast = null;
	        }
	        
	        super.onDestroy();
	}
	@Override
	protected void onPause(){
	        mMapView.onPause();
	        super.onPause();
	}
	@Override
	protected void onResume(){
	       mMapView.onResume();
	       super.onResume();
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;

		StringBuffer sb = new StringBuffer(256);

		sb.append("time : ");

		sb.append(location.getTime());

		sb.append("\nerror code : ");

		sb.append(location.getLocType());

		sb.append("\nlatitude : ");

		sb.append(location.getLatitude());

		sb.append("\nlontitude : ");

		sb.append(location.getLongitude());

		sb.append("\nradius : ");

		sb.append(location.getRadius());

		if (location.getLocType() == BDLocation.TypeGpsLocation) {

			sb.append("\nspeed : ");

			sb.append(location.getSpeed());

			sb.append("\nsatellite : ");

			sb.append(location.getSatelliteNumber());

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

			sb.append("\naddr : ");

			sb.append(location.getAddrStr());
		}
		
		if(mMapView!=null)
		{
			mMapView.getOverlays().clear();
			MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
			LocationData locData = new LocationData();
			//手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			locData.direction = location.getDirection();
			GlobalParam.getInstance().SetLastLocation(locData);
			myLocationOverlay.setData(locData);
			mMapView.getOverlays().add(myLocationOverlay);
			
			setDianpuPoint();
			
			mMapView.refresh();
			
			if (autoSpot)
			{
				mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude* 1e6)));
			}

			if (lastAddr == null || !lastAddr.equalsIgnoreCase(location.getAddrStr()))
			{
				lastAddr = location.getAddrStr();
				if (toast != null && autoSpot)
				{
					toast.setText(lastAddr);
					toast.show();
				}
			}
		}
		
		Log.i("1510", sb.toString());
	}
	
	public void onReceivePoi(BDLocation poiLocation) {

		// 将在下个版本中去除poi功能
		if (poiLocation == null) {
			return;
		}

		StringBuffer sb = new StringBuffer(256);

		sb.append("Poi time : ");

		sb.append(poiLocation.getTime());

		sb.append("\nerror code : ");

		sb.append(poiLocation.getLocType());

		sb.append("\nlatitude : ");

		sb.append(poiLocation.getLatitude());

		sb.append("\nlontitude : ");

		sb.append(poiLocation.getLongitude());

		sb.append("\nradius : ");

		sb.append(poiLocation.getRadius());

		if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {

			sb.append("\naddr : ");

			sb.append(poiLocation.getAddrStr());

		}

		if (poiLocation.hasPoi()) {

			sb.append("\nPoi:");

			sb.append(poiLocation.getPoi());

		} else {

			sb.append("noPoi information");

		}

		Log.i("1510", sb.toString());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back)
		{
			setResult(resultCode);
			finish();
		}
	}

	protected abstract void setDianpuPoint();
}