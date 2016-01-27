package org.seven.activity;

import org.seven.component.DianpuPoint;
import org.seven.conn.RequestTask;
import org.seven.data.StoreInfo;
import org.seven.utils.GlobalParam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class DianpuweizhiActivity extends MapActivity {

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GlobalParam.SIGN_SUCCESS: {
				dialog.dismiss();
				String s = msg.getData().getString("message");
				AlertDialog ad = new AlertDialog.Builder(
						DianpuweizhiActivity.this)
						.setPositiveButton("确认", null).create();
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle("提示");
				ad.setMessage(s);
				ad.show();
				break;
			}case GlobalParam.REGISTER_SUCCESS: {
				dialog.dismiss();
				String s = msg.getData().getString("message");
				AlertDialog ad = new AlertDialog.Builder(
						DianpuweizhiActivity.this)
						.setPositiveButton("确认", null).create();
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle("提示");
				ad.setMessage(s);
				ad.show();
				
				Button button = (Button) findViewById(R.id.submit);
				button.setText("已标记");
				button.setEnabled(false);
				store.setLat(GlobalParam.getInstance().GetLastLocation().latitude+"");
				store.setLng(GlobalParam.getInstance().GetLastLocation().longitude+"");
				setDianpuPoint();
				mMapView.refresh();
				
				double lat = Double.parseDouble(store.getLat());
				double lng = Double.parseDouble(store.getLng());
				mMapView.getController().animateTo(
						new GeoPoint((int) (lat * 1e6), (int) (lng * 1e6)));
				
				resultCode = 1;
				
				break;
			}
			case 2: {
				dialog.dismiss();
				final RequestTask rt = new RequestTask();
				DianpuweizhiActivity.this.dialog = new ProgressDialog(
						DianpuweizhiActivity.this) {
					protected void onStop() {
						rt.cancel();
					}
				};
				DianpuweizhiActivity.this.dialog
						.setMessage(getString(R.string.request));
				DianpuweizhiActivity.this.dialog.setCancelable(true);
				DianpuweizhiActivity.this.dialog.show();
				String s = msg.getData().getString("method");
				if (s.equalsIgnoreCase("signInAndOut")) {
					rt.signInAndOut(GlobalParam.getInstance()
							.GetLastLoginInfo().getSid(), GlobalParam
							.getInstance().GetLastLoginInfo().getUserno(),
							store.getStrno(), GlobalParam.getInstance()
									.GetLastLocation().latitude, GlobalParam
									.getInstance().GetLastLocation().longitude,
							mHandler);
				} else {
					rt.registerStore(
							GlobalParam.getInstance().GetLastLoginInfo()
									.getSid(),
							store.getStrno(),
							GlobalParam.getInstance().GetLastLocation().latitude,
							GlobalParam.getInstance().GetLastLocation().longitude,
							mHandler);
				}

				break;
			}
			case -1: {
				dialog.dismiss();
				String s = msg.getData().getString("message");
				AlertDialog ad = new AlertDialog.Builder(
						DianpuweizhiActivity.this)
						.setPositiveButton("确认", null).create();
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle(R.string.error_title);
				ad.setMessage(s);
				ad.show();
				break;
			}
			default:
			}
		};
	};

	private ProgressDialog dialog;

	private StoreInfo store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dianpu_weizhi);
		Button button = (Button) findViewById(R.id.submit);
		button.setOnClickListener(this);

		Button button1 = (Button) findViewById(R.id.wodeweizhi);
		button1.setOnClickListener(this);

		Button button2 = (Button) findViewById(R.id.dianpuweizhi);
		button2.setOnClickListener(this);

		Bundle b = getIntent().getExtras();
		String src = b.getString("src");
		store = (StoreInfo) b.getSerializable("store");

		LocationData locData = GlobalParam.getInstance().GetLastLocation();
		GeoPoint point = new GeoPoint((int) (locData.latitude * 1E6),
				(int) (locData.longitude * 1E6));
		if (src.equalsIgnoreCase("kaoqin")) {
			button.setText("签到");
		} else if (src.equalsIgnoreCase("dianpugeo")) {
			autoSpot = false;
			button.setText("已标记");
			button.setEnabled(false);
			if (store.getLat() != null && store.getLng() != null
					&& !store.getLat().equalsIgnoreCase("")
					&& !store.getLng().equalsIgnoreCase("")) {
				double lat = Double.parseDouble(store.getLat());
				double lng = Double.parseDouble(store.getLng());
				point.setLatitudeE6((int) (lat * 1E6));
				point.setLongitudeE6((int) (lng * 1E6));
			}
		} else {
			button.setText("标记");
		}

		InitMap(point);

		TextView v = (TextView) findViewById(R.id.message);
		v.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v
				.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		v.setLayoutParams(params);
		v.setText(store.getStrname());
	}

	@Override
	public void setDianpuPoint() {
		if (store.getLat() != null && store.getLng() != null
				&& !store.getLat().equalsIgnoreCase("")
				&& !store.getLng().equalsIgnoreCase("")) {
			double lat = Double.parseDouble(store.getLat());
			double lng = Double.parseDouble(store.getLng());
			GeoPoint p1 = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			OverlayItem item = new OverlayItem(p1, store.getStrname(),
					store.getStraddress());
			Drawable mark = getResources().getDrawable(R.drawable.point);
			DianpuPoint dp = new DianpuPoint(mark, mMapView);
			mMapView.getOverlays().add(dp);
			dp.addItem(item);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.submit) {
			Button button = (Button) v;
			if (button.getText().equals("签到")) {
				AlertDialog ad = new AlertDialog.Builder(
						DianpuweizhiActivity.this)
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										final RequestTask rt = new RequestTask();
										DianpuweizhiActivity.this.dialog = new ProgressDialog(DianpuweizhiActivity.this) {
											@Override
											protected void onStop() {
												rt.cancel();
											}
										};
										DianpuweizhiActivity.this.dialog.setMessage(getString(R.string.request));
										DianpuweizhiActivity.this.dialog.setCancelable(true);
										DianpuweizhiActivity.this.dialog.show();
										rt.signInAndOut(GlobalParam.getInstance().GetLastLoginInfo()
												.getSid(), GlobalParam.getInstance().GetLastLoginInfo()
												.getUserno(), store.getStrno(), GlobalParam
												.getInstance().GetLastLocation().latitude, GlobalParam
												.getInstance().GetLastLocation().longitude, mHandler);
									}
								}).setNegativeButton("否", null).create();
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle("提示");
				ad.setMessage("是否将当前位置\n纬度：" + GlobalParam.getInstance()
						.GetLastLocation().latitude + "\n经度：" + GlobalParam.getInstance()
						.GetLastLocation().longitude + "\n进行签到");
				ad.show();
			} else if (button.getText().equals("标记")) {
				AlertDialog ad = new AlertDialog.Builder(
						DianpuweizhiActivity.this)
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										final RequestTask rt = new RequestTask();
										DianpuweizhiActivity.this.dialog = new ProgressDialog(
												DianpuweizhiActivity.this) {
											protected void onStop() {
												rt.cancel();
											}
										};
										DianpuweizhiActivity.this.dialog
												.setMessage(getString(R.string.request));
										DianpuweizhiActivity.this.dialog
												.setCancelable(true);
										DianpuweizhiActivity.this.dialog.show();
										rt.registerStore(
												GlobalParam.getInstance()
														.GetLastLoginInfo()
														.getSid(),
												store.getStrno(),
												GlobalParam.getInstance()
														.GetLastLocation().latitude,
												GlobalParam.getInstance()
														.GetLastLocation().longitude,
												mHandler);
									}
								}).setNegativeButton("否", null).create();
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle("提示");
				ad.setMessage("是否将当前位置\n纬度：" + GlobalParam.getInstance()
						.GetLastLocation().latitude + "\n经度：" + GlobalParam.getInstance()
						.GetLastLocation().longitude + "\n设置为店铺位置");
				ad.show();
			}
		} else if (v.getId() == R.id.wodeweizhi) {
			autoSpot = true;
			mMapView.getController()
					.animateTo(
							new GeoPoint((int) (GlobalParam.getInstance()
									.GetLastLocation().latitude * 1e6),
									(int) (GlobalParam.getInstance()
											.GetLastLocation().longitude * 1e6)));
		} else if (v.getId() == R.id.dianpuweizhi) {
			if (store.getLat() != null && store.getLng() != null
					&& !store.getLat().equalsIgnoreCase("")
					&& !store.getLng().equalsIgnoreCase("")) {
				autoSpot = false;
				double lat = Double.parseDouble(store.getLat());
				double lng = Double.parseDouble(store.getLng());
				mMapView.getController().animateTo(
						new GeoPoint((int) (lat * 1e6), (int) (lng * 1e6)));
			}
		}
	}
}