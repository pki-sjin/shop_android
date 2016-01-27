package org.seven.activity;

import org.seven.utils.GlobalParam;

import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;

public class XundianBaogaoActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xundian_baogao);
		LocationData locData = GlobalParam.getInstance().GetLastLocation();
		GeoPoint point = new GeoPoint((int)(locData.latitude* 1E6),(int)(locData.longitude* 1E6));
		InitMap(point);
	}

	@Override
	public void setDianpuPoint() {
		
	}
}