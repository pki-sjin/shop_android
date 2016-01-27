package org.seven.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.seven.component.ListAdapter;
import org.seven.conn.RequestTask;
import org.seven.data.BillInfo;
import org.seven.data.StoreInfo;
import org.seven.utils.GlobalParam;
import org.seven.utils.GlobalParam.BillSortType;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BillInfoActivity extends MapActivity implements
		OnItemClickListener, OnClickListener {

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
			{
				dialog.dismiss();
				String s = msg.getData().getString("message");
				AlertDialog ad = new AlertDialog.Builder(BillInfoActivity.this)
						.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Bundle b = BillInfoActivity.this.getIntent().getExtras();
								final RequestTask rt = new RequestTask();
								BillInfoActivity.this.dialog = new ProgressDialog(BillInfoActivity.this) {
									protected void onStop() {
										rt.cancel();
									}
								};
								BillInfoActivity.this.dialog.setMessage(getString(R.string.request));
								BillInfoActivity.this.dialog.setCancelable(true);
								BillInfoActivity.this.dialog.show();
								rt.getBill(GlobalParam.getInstance().GetLastLoginInfo().getSid(),
										(StoreInfo)b.getSerializable("store"), mHandler);
							}
						}).create();
				ad.setIcon(android.R.drawable.ic_dialog_alert);
				ad.setTitle("提示");
				ad.setMessage(s);
				ad.show();
				
				resultCode = 1;
				
				break;
			}
			case GlobalParam.GETBILLS_SUCCESS: {
				Bundle b = msg.getData();
				ArrayList<BillInfo> list = (ArrayList<BillInfo>) b.getSerializable("list");
				ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < list.size(); i++) {
					BillInfo c = list.get(i);
					mList.add(c.getMap());
				}

				ListAdapter listAdapter = new ListAdapter(BillInfoActivity.this, mList, list, 0);
				lv.setAdapter(listAdapter);
				dialog.dismiss();
				break;
			}
			case GlobalParam.GETDETAIL_SUCCESS:
			{
				Bundle b = msg.getData();
				b.putString("billNo",lastBillno);
				Intent i = new Intent(GlobalParam.getInstance(),DetailInfoActivity.class);
            	i.putExtras(b);
            	startActivity(i);
				
				dialog.dismiss();
				break;
			}
			case 2:
			{
				dialog.dismiss();
				final RequestTask rt = new RequestTask();
				BillInfoActivity.this.dialog = new ProgressDialog(BillInfoActivity.this) {
					protected void onStop() {
						rt.cancel();
					}
				};
				BillInfoActivity.this.dialog.setMessage(getString(R.string.request));
				BillInfoActivity.this.dialog.setCancelable(true);
				BillInfoActivity.this.dialog.show();
				String s = msg.getData().getString("method");
				if (s.equalsIgnoreCase("getBill"))
				{
					rt.getBill(GlobalParam.getInstance().GetLastLoginInfo().getSid(), (StoreInfo)msg.getData().getSerializable("store"), mHandler);
				}else if (s.equalsIgnoreCase("getDetail"))
				{
					rt.getBillDetail(GlobalParam.getInstance().GetLastLoginInfo().getSid(), msg.getData().getString("strno"), msg.getData().getString("billno"), mHandler);
				}
				else
				{
					rt.dealBillByStr(GlobalParam.getInstance().GetLastLoginInfo().getSid(), "1", msg.getData().getString("strno"), msg.getData().getString("billno"), mHandler);
				}
				break;
			}
			case -1: {
				dialog.dismiss();
				String s = msg.getData().getString("message");
				AlertDialog ad = new AlertDialog.Builder(BillInfoActivity.this)
						.setPositiveButton("确认", null).create();
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
	private AlertDialog ad = null;
	private ProgressDialog dialog;
	private String lastBillno = null;
	private ArrayList<BillInfo> list = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billinfo);
		
		LocationData locData = GlobalParam.getInstance().GetLastLocation();
		GeoPoint point = new GeoPoint((int)(locData.latitude* 1E6),(int)(locData.longitude* 1E6));
		InitMap(point);
		
		Button button = (Button) findViewById(R.id.submit);
		button.setOnClickListener(this);

		Bundle b = getIntent().getExtras();
		GlobalParam.getInstance().billSortType = BillSortType.NO_SORT;
		list = (ArrayList<BillInfo>) b.getSerializable("list");
		Collections.sort(list);
		StoreInfo store = (StoreInfo)b.getSerializable("store");
		TextView v = (TextView) findViewById(R.id.message);
		v.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)v.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		v.setLayoutParams(params);
		v.setText(store.getStrname());
		
		lv = new ListView(this);
		lv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		LinearLayout ll = (LinearLayout) findViewById(R.id.cells);
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setOnItemClickListener(this);
		ll.addView(lv);

		ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			BillInfo c = list.get(i);
			mList.add(c.getMap());
		}

		ListAdapter listAdapter = new ListAdapter(this, mList, list, 0);
		lv.setAdapter(listAdapter);

		ad = new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setItems(new String[] { "查看明细", "收货确认", "问题反馈", "无法执行" }, this)
				.create();
		
		findViewById(R.id.label_packagecnt).setOnClickListener(this);
		findViewById(R.id.label_s_num).setOnClickListener(this);
		findViewById(R.id.label_satisfied).setOnClickListener(this);
		findViewById(R.id.label_o_num).setOnClickListener(this);
		findViewById(R.id.label_s_store).setOnClickListener(this);
		findViewById(R.id.label_billno).setOnClickListener(this);
		findViewById(R.id.label_s_date).setOnClickListener(this);
		findViewById(R.id.label_a_num).setOnClickListener(this);
		findViewById(R.id.label_skucount).setOnClickListener(this);
		findViewById(R.id.label_remark).setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		ListView lv = (ListView) adapterView;
		ListAdapter la = (ListAdapter) lv.getAdapter();
		BillInfo cell = la.getBillItem(position);
		lastBillno = cell.getBillno();
		ad.setTitle(cell.getBillno());
		ad.show();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.submit) {
			Bundle b = getIntent().getExtras();
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
					(StoreInfo)b.getSerializable("store"), mHandler);
		}else if (v.getId() == R.id.label_packagecnt)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("箱数 ▼");
				tv.setTag(1);
				refreshList(BillSortType.PACKAGECNT_SORT_DOWN);
			}else
			{
				tv.setText("箱数 ▲");
				tv.setTag(0);
				refreshList(BillSortType.PACKAGECNT_SORT_UP);
			}
		}else if (v.getId() == R.id.label_s_num)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("发货数量 ▼");
				tv.setTag(1);
				refreshList(BillSortType.S_NUM_SORT_DOWN);
			}else
			{
				tv.setText("发货数量 ▲");
				tv.setTag(0);
				refreshList(BillSortType.S_NUM_SORT_UP);
			}
		}else if (v.getId() == R.id.label_satisfied)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("满足率 ▼");
				tv.setTag(1);
				refreshList(BillSortType.SATISFIED_SORT_DOWN);
			}else
			{
				tv.setText("满足率 ▲");
				tv.setTag(0);
				refreshList(BillSortType.SATISFIED_SORT_UP);
			}
		}else if (v.getId() == R.id.label_o_num)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("订货数量 ▼");
				tv.setTag(1);
				refreshList(BillSortType.O_NUM_SORT_DOWN);
			}else
			{
				tv.setText("订货数量 ▲");
				tv.setTag(0);
				refreshList(BillSortType.O_NUM_SORT_UP);
			}
		}else if (v.getId() == R.id.label_s_store)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("发货门店 ▼");
				tv.setTag(1);
				refreshList(BillSortType.S_STORE_SORT_DOWN);
			}else
			{
				tv.setText("发货门店 ▲");
				tv.setTag(0);
				refreshList(BillSortType.S_STORE_SORT_UP);
			}
		}else if (v.getId() == R.id.label_billno)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("单据号 ▼");
				tv.setTag(1);
				refreshList(BillSortType.BILLNO_SORT_DOWN);
			}else
			{
				tv.setText("单据号 ▲");
				tv.setTag(0);
				refreshList(BillSortType.BILLNO_SORT_UP);
			}
		}else if (v.getId() == R.id.label_s_date)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("发货时间 ▼");
				tv.setTag(1);
				refreshList(BillSortType.S_DATE_SORT_DOWN);
			}else
			{
				tv.setText("发货时间 ▲");
				tv.setTag(0);
				refreshList(BillSortType.S_DATE_SORT_UP);
			}
		}else if (v.getId() == R.id.label_a_num)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("审核数量 ▼");
				tv.setTag(1);
				refreshList(BillSortType.A_NUM_SORT_DOWN);
			}else
			{
				tv.setText("审核数量 ▲");
				tv.setTag(0);
				refreshList(BillSortType.A_NUM_SORT_UP);
			}
		}else if (v.getId() == R.id.label_skucount)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("SKU数 ▼");
				tv.setTag(1);
				refreshList(BillSortType.SKUCOUNT_SORT_DOWN);
			}else
			{
				tv.setText("SKU数 ▲");
				tv.setTag(0);
				refreshList(BillSortType.SKUCOUNT_SORT_UP);
			}
		}else if (v.getId() == R.id.label_remark)
		{
			clearOrderTag();
			TextView tv = (TextView)v;
			if (tv.getTag().equals(0))
			{
				tv.setText("备注 ▼");
				tv.setTag(1);
				refreshList(BillSortType.REMARK_SORT_DOWN);
			}else
			{
				tv.setText("备注 ▲");
				tv.setTag(0);
				refreshList(BillSortType.REMARK_SORT_UP);
			}
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Bundle b = getIntent().getExtras();
		if (which == 0) {
			final RequestTask rt = new RequestTask();
			this.dialog = new ProgressDialog(this) {
				protected void onStop() {
					rt.cancel();
				}
			};
			this.dialog.setMessage(getString(R.string.request));
			this.dialog.setCancelable(true);
			this.dialog.show();
			rt.getBillDetail(GlobalParam.getInstance().GetLastLoginInfo().getSid(), ((StoreInfo)b.getSerializable("store")).getStrno(), lastBillno, mHandler);
		} else if (which == 1)
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
			rt.dealBillByStr(GlobalParam.getInstance().GetLastLoginInfo().getSid(), "1", ((StoreInfo)b.getSerializable("store")).getStrno(), lastBillno, mHandler);
		}else if (which == 2)
		{
			
		}else if (which == 3)
		{
			
		}
	}

	@Override
	public void setDianpuPoint() {
		
	}
	
	private void clearOrderTag()
	{
		{
			TextView tv = (TextView)findViewById(R.id.label_packagecnt);
			tv.setText("箱数");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_s_num);
			tv.setText("发货数量");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_satisfied);
			tv.setText("满足率");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_o_num);
			tv.setText("订货数量");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_s_store);
			tv.setText("发货门店");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_billno);
			tv.setText("单据号");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_s_date);
			tv.setText("发货时间");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_a_num);
			tv.setText("审核数量");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_skucount);
			tv.setText("SKU数");
		}
		
		{
			TextView tv = (TextView)findViewById(R.id.label_remark);
			tv.setText("备注");
		}
	}

	private void refreshList(BillSortType type)
	{
		GlobalParam.getInstance().billSortType = type;
		Collections.sort(list);
		ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			BillInfo c = list.get(i);
			mList.add(c.getMap());
		}

		ListAdapter listAdapter = new ListAdapter(this, mList, list, 0);
		lv.setAdapter(listAdapter);
	}
}