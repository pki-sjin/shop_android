package org.seven.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.seven.component.ListAdapter;
import org.seven.data.BillDetailInfo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailInfoActivity extends Activity implements OnClickListener {

	private ListView lv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.detail);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		TextView v = (TextView) findViewById(R.id.message);
		Button t = (Button) findViewById(R.id.login);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)v.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		v.setLayoutParams(params);
		t.setVisibility(View.INVISIBLE);
		
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		
		Bundle b = getIntent().getExtras();
		
		v.setText(b.getString("billNo"));
		
		ArrayList<BillDetailInfo> list = (ArrayList<BillDetailInfo>) b.getSerializable("list");
		
		lv = new ListView(this);
		lv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		LinearLayout ll = (LinearLayout) findViewById(R.id.cells);
		lv.setCacheColorHint(Color.TRANSPARENT);
		ll.addView(lv);

		ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			BillDetailInfo c = list.get(i);
			mList.add(c.getMap());
		}

		ListAdapter listAdapter = new ListAdapter(DetailInfoActivity.this, mList, list, "detail");
		lv.setAdapter(listAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back)
		{
			finish();
		}
	}
}
