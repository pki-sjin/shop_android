package org.seven.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.seven.component.ListAdapter;
import org.seven.conn.RequestTask;
import org.seven.data.StoreInfo;
import org.seven.utils.GlobalParam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StoreInfoGeoActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GlobalParam.GETSTORES_SUCCESS: {
				Bundle b = msg.getData();
				ArrayList<StoreInfo> list = (ArrayList<StoreInfo>) b.getSerializable("list");
				ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < list.size(); i++) {
					StoreInfo s = list.get(i);
					mList.add(s.getMap());
				}
				
				ListAdapter listAdapter = new ListAdapter(StoreInfoGeoActivity.this, mList, list, true);
				lv.setAdapter(listAdapter);
				dialog.dismiss();
				break;
			}
			case 2:
			{
				dialog.dismiss();
				final RequestTask rt = new RequestTask();
				StoreInfoGeoActivity.this.dialog = new ProgressDialog(StoreInfoGeoActivity.this) {
					protected void onStop() {
						rt.cancel();
					}
				};
				StoreInfoGeoActivity.this.dialog.setMessage(getString(R.string.request));
				StoreInfoGeoActivity.this.dialog.setCancelable(true);
				StoreInfoGeoActivity.this.dialog.show();
				rt.getStoreContainsGeo(GlobalParam.getInstance().GetLastLoginInfo().getSid(), GlobalParam.getInstance().GetLastLoginInfo().getUserno(), mHandler);
				break;
			}
			case -1: {
				dialog.dismiss();
				String s = msg.getData().getString("message");
            	AlertDialog ad = new AlertDialog.Builder(StoreInfoGeoActivity.this).setPositiveButton("确认", null).create();
            	ad.setIcon(android.R.drawable.ic_dialog_alert);
            	ad.setTitle(R.string.error_title);
            	ad.setMessage(s);
            	ad.show();
				break;
			}
			default:
				dialog.dismiss();
			}
		};
	};

	private ListView lv = null;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_storeinfogeo);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		TextView v = (TextView) findViewById(R.id.message);
		Button t = (Button) findViewById(R.id.login);
		v.setVisibility(View.INVISIBLE);
		t.setVisibility(View.INVISIBLE);

		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);

		Button button = (Button) findViewById(R.id.submit);
		button.setOnClickListener(this);

		Bundle b = getIntent().getExtras();
		ArrayList<StoreInfo> list = (ArrayList<StoreInfo>) b.getSerializable("list");
		lv = new ListView(this);
		lv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		LinearLayout ll = (LinearLayout) findViewById(R.id.cells);
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setOnItemClickListener(this);
		ll.addView(lv);

		ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			StoreInfo s = list.get(i);
			mList.add(s.getMap());
		}

		ListAdapter listAdapter = new ListAdapter(this, mList, list, true);
		lv.setAdapter(listAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		ListView lv = (ListView) adapterView;
		ListAdapter la = (ListAdapter) lv.getAdapter();
		StoreInfo store = la.getStoreItem(position);
		boolean containsGeo = false;
		if (store.getLat() != null && store.getLng() != null && !store.getLat().equalsIgnoreCase("") && !store.getLng().equalsIgnoreCase(""))
		{
			containsGeo = true;
		}else	
		{
			containsGeo = false;
		}
		
		Bundle b = getIntent().getExtras();
		String src = b.getString("src");
		if (src.equalsIgnoreCase("dianpu"))
		{
			Intent i = new Intent(GlobalParam.getInstance(),DianpuweizhiActivity.class);
			Bundle bundle = new Bundle();
			//biaoji dianpu
			if (containsGeo)
			{
				bundle.putString("src", "dianpugeo");
			}else
			{
				bundle.putString("src", "dianpunogeo");
			}
			bundle.putSerializable("store", store);
        	i.putExtras(bundle);
        	
        	startActivityForResult(i, 0);
		}else
		{
			//kaoqin qiandao
			if (containsGeo)
			{
				Intent i = new Intent(GlobalParam.getInstance(),DianpuweizhiActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("src", "kaoqin");
				bundle.putSerializable("store", store);
            	i.putExtras(bundle);
            	startActivity(i);
			}else
			{
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setButton(getString(R.string.ok),
						Message.obtain(new Handler()));
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle(R.string.error_title);
				ad.setMessage("【" + store.getStrname() + "】的店铺地理位置信息不足，无法签到。");
				ad.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back) {
			finish();
		} else if (v.getId() == R.id.submit) {
			GetStoresGeo();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1)
		{
			// need refresh
			GetStoresGeo();
		}
	}
	
	private void GetStoresGeo()
	{
		final RequestTask rt = new RequestTask();
		this.dialog = new ProgressDialog(this) {
			protected void onStop() {
				rt.cancel();
			}
		};
		this.dialog.setMessage(getString(R.string.request));
		this.dialog.setCancelable(true);
		this.dialog.show();
		rt.getStoreContainsGeo(GlobalParam.getInstance().GetLastLoginInfo().getSid(), GlobalParam.getInstance().GetLastLoginInfo().getUserno(), mHandler);
	
	}
}