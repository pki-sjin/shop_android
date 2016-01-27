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

public class StoreInfoActivity extends Activity implements OnItemClickListener,
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
				
				ListAdapter listAdapter = new ListAdapter(StoreInfoActivity.this, mList, list);
				lv.setAdapter(listAdapter);
				dialog.dismiss();
				break;
			}
			case GlobalParam.GETBILLS_SUCCESS: {
				Intent i = new Intent(GlobalParam.getInstance(),BillInfoActivity.class);
            	i.putExtras(msg.getData());
            	startActivityForResult(i, 0);
            	dialog.dismiss();
				break;
			}
			case 2:
			{
				dialog.dismiss();
				final RequestTask rt = new RequestTask();
				StoreInfoActivity.this.dialog = new ProgressDialog(StoreInfoActivity.this) {
					protected void onStop() {
						rt.cancel();
					}
				};
				StoreInfoActivity.this.dialog.setMessage(getString(R.string.request));
				StoreInfoActivity.this.dialog.setCancelable(true);
				StoreInfoActivity.this.dialog.show();
				String s = msg.getData().getString("method");
				if (s.equalsIgnoreCase("getBill"))
				{
					rt.getBill(GlobalParam.getInstance().GetLastLoginInfo().getSid(), (StoreInfo)msg.getData().getSerializable("store"), mHandler);
				}else
				{
					rt.getStore(GlobalParam.getInstance().GetLastLoginInfo().getSid(), GlobalParam.getInstance().GetLastLoginInfo().getUserno(), mHandler);
				}
				break;
			}
			case -1: {
				dialog.dismiss();
				String s = msg.getData().getString("message");
            	AlertDialog ad = new AlertDialog.Builder(StoreInfoActivity.this).setPositiveButton("х╥хо", null).create();
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
		setContentView(R.layout.activity_storeinfo);
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

		ListAdapter listAdapter = new ListAdapter(this, mList, list);
		lv.setAdapter(listAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		ListView lv = (ListView) adapterView;
		ListAdapter la = (ListAdapter) lv.getAdapter();
		StoreInfo store = la.getStoreItem(position);

		final RequestTask rt = new RequestTask();
		this.dialog = new ProgressDialog(this) {
			protected void onStop() {
				rt.cancel();
			}
		};
		this.dialog.setMessage(getString(R.string.request));
		this.dialog.setCancelable(true);
		this.dialog.show();
		rt.getBill(GlobalParam.getInstance().GetLastLoginInfo().getSid(),
				store, mHandler);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back) {
			finish();
		} else if (v.getId() == R.id.submit) {
			GetStores();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1)
		{
			// need refresh
			GetStores();
		}
	}
	
	private void GetStores()
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
		rt.getStore(GlobalParam.getInstance().GetLastLoginInfo().getSid(), GlobalParam.getInstance().GetLastLoginInfo().getUserno(), mHandler);
	
	}
}