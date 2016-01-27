package org.seven.component;

import org.seven.activity.R;
import org.seven.activity.XundianBaogaoActivity;
import org.seven.activity.XundianDengjiActivity;
import org.seven.utils.GlobalParam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class XundianFragment extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.xundian, container, false);
		Button button1 = (Button)view.findViewById(R.id.xundian1);
		Button button2 = (Button)view.findViewById(R.id.xundian2);
		Button button3 = (Button)view.findViewById(R.id.xundian3);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.xundian1){
			Intent i = new Intent(GlobalParam.getInstance(),XundianDengjiActivity.class);
			Bundle b = new Bundle();
			b.putInt("type", 0);
			i.putExtras(b);
        	startActivity(i);
		}else if (v.getId() == R.id.xundian2)
		{
			Intent i = new Intent(GlobalParam.getInstance(),XundianBaogaoActivity.class);
        	startActivity(i);
		}else if (v.getId() == R.id.xundian3)
		{
			Intent i = new Intent(GlobalParam.getInstance(),XundianDengjiActivity.class);
			Bundle b = new Bundle();
			b.putInt("type", 1);
			i.putExtras(b);
        	startActivity(i);
		}
	}
}
