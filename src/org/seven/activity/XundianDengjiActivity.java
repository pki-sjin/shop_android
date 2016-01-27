package org.seven.activity;

import org.seven.utils.GlobalParam;

import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.widget.TextView;

public class XundianDengjiActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xundian_dengji);
		LocationData locData = GlobalParam.getInstance().GetLastLocation();
		GeoPoint point = new GeoPoint((int)(locData.latitude* 1E6),(int)(locData.longitude* 1E6));
		InitMap(point);
		TextView t = (TextView)findViewById(R.id.view_title);
		Bundle b = getIntent().getExtras();
		int type = b.getInt("type");
		if (type == 0)
		{
			t.setText("µΩµÍµ«º«");
		}else
		{
			t.setText("¿ÎµÍµ«º«");
		}
	}

	@Override
	public void setDianpuPoint() {
		
	}
}