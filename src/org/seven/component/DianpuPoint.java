package org.seven.component;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;

public class DianpuPoint extends ItemizedOverlay<OverlayItem> {

	public DianpuPoint(Drawable mark,MapView mapView) {
		super(mark,mapView);
		
	}
}
