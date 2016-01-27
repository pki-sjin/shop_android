package org.seven.activity;

import java.util.HashMap;

import org.seven.component.DianpuFragment;
import org.seven.component.GengduoFragment;
import org.seven.component.KaoqinFragment;
import org.seven.component.TuozhanFragment;
import org.seven.component.XundianFragment;
import org.seven.conn.RequestTask;
import org.seven.data.LoginInfo;
import org.seven.utils.GlobalParam;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TextView;

public class MainTabActivity extends FragmentActivity implements OnClickListener {
	
	private Handler mHandler = new Handler(){
	  public void handleMessage(Message msg) {
	      switch (msg.what) {
		      case 1:
		      {
		    	  Bundle b = msg.getData();
		    	  String username = b.getString("username");
		    	  String position = b.getString("position");
		    	  TextView m = (TextView) findViewById(R.id.message);
		    	  m.setText(position + " " + username);
		    	  Button t = (Button) findViewById(R.id.login);
		    	  t.setText("登出");
		    	  break;
		      }
		      case -1:
		      {
	            	String s = msg.getData().getString("message");
	            	AlertDialog ad = new AlertDialog.Builder(MainTabActivity.this).setPositiveButton("确认", null).create();
	            	ad.setIcon(android.R.drawable.ic_dialog_alert);
	            	ad.setTitle(R.string.error_title);
	            	ad.setMessage(s);
	            	ad.show();
	            	break;
		      }
		      default:  
	      }
	      dialog.dismiss();
	  };
	};
	
	private ProgressDialog dialog;
	
	private AlertDialog loginDialog;
	
	TabHost mTabHost;
	TabManager mTabManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tab);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		
		Button t = (Button) findViewById(R.id.login);
		t.setOnClickListener(this);
		
		LoginInfo loginInfo = GlobalParam.getInstance().GetLastLoginInfo();
		if (loginInfo.isAutoLogin())
		{
			TextView m = (TextView) findViewById(R.id.message);
			m.setText(loginInfo.getPosition() + " " + loginInfo.getUsername());
	    	t.setText("登出");
		}else
		{
			GlobalParam.getInstance().SetLastLoginInfo(null, null, null, null, null, false);
		}
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

		mTabManager.addTab(
				mTabHost.newTabSpec("巡店").setIndicator("巡店"),
				XundianFragment.class, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("设置").setIndicator("设置"),
				DianpuFragment.class, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("考勤").setIndicator("考勤"),
				KaoqinFragment.class, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("订单").setIndicator("订单"),
				TuozhanFragment.class, null);
		mTabManager.addTab(
				mTabHost.newTabSpec("更多").setIndicator("更多"),
				GengduoFragment.class, null);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		
		if (!GlobalParam.getInstance().isRunning){
			Log.i("1510", "StartUp");
			GlobalParam.getInstance().isRunning = true;
			//系统闹钟，每隔1分钟触发广播接收
			AlarmManager am = (AlarmManager)GlobalParam.getInstance().getSystemService(Service.ALARM_SERVICE);
			Intent ii = new Intent();
			ii.setAction(GlobalParam.getInstance().CLOCK_ACTION);
			PendingIntent pii = PendingIntent.getBroadcast(GlobalParam.getInstance(), GlobalParam.BROADCAST_ID, ii, PendingIntent.FLAG_UPDATE_CURRENT);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pii);
			Log.i("1510", "StartAlarm");
		}
	}

	@Override
	protected void onDestroy() {
		if(GlobalParam.getInstance().mBMapMan!=null){
			GlobalParam.getInstance().mBMapMan.destroy();
			GlobalParam.getInstance().mBMapMan=null;
		}
		super.onDestroy();
		System.exit(0);
	}
	
	@Override
	protected void onPause() {
		if(GlobalParam.getInstance().mBMapMan!=null){
			GlobalParam.getInstance().mBMapMan.stop();
		}
		super.onPause();
		
	}
	
	@Override
	protected void onResume() {
		if(GlobalParam.getInstance().mBMapMan!=null){
			GlobalParam.getInstance().mBMapMan.start();
		}
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	/**
	 * This is a helper class that implements a generic mechanism for
	 * associating fragments with the tabs in a tab host. It relies on a trick.
	 * Normally a tab host has a simple API for supplying a View or Intent that
	 * each tab will show. This is not sufficient for switching between
	 * fragments. So instead we make the content part of the tab host 0dp high
	 * (it is not shown) and the TabManager supplies its own dummy view to show
	 * as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct fragment shown in a separate content area whenever
	 * the selected tab changes.
	 */
	public static class TabManager implements TabHost.OnTabChangeListener {
		private final FragmentActivity mActivity;
		private final TabHost mTabHost;
		private final int mContainerId;
		private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
		TabInfo mLastTab;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabManager(FragmentActivity activity, TabHost tabHost,
				int containerId) {
			mActivity = activity;
			mTabHost = tabHost;
			mContainerId = containerId;
			mTabHost.setOnTabChangedListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mActivity));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			info.fragment = mActivity.getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}

			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
		}

		@Override
		public void onTabChanged(String tabId) {
			TabInfo newTab = mTabs.get(tabId);
			if (mLastTab != newTab) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						ft.detach(mLastTab.fragment);
					}
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(mActivity,
								newTab.clss.getName(), newTab.args);
						ft.add(mContainerId, newTab.fragment, newTab.tag);
					} else {
						ft.attach(newTab.fragment);
					}
				}

				mLastTab = newTab;
				ft.commit();
				mActivity.getSupportFragmentManager()
						.executePendingTransactions();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.login){
			Button t = (Button)v;
			if (t.getText().equals("登录"))
			{
				LayoutInflater factory = LayoutInflater.from(this);
	            final View textEntryView = factory.inflate(R.layout.login, null);
	            this.loginDialog = new AlertDialog.Builder(this).setTitle("登录").setView(textEntryView)
	            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	TextView username = (TextView)textEntryView.findViewById(R.id.plu_no);
	        			TextView password = (TextView)textEntryView.findViewById(R.id.plu_name);
	        			CheckBox autoLogin = (CheckBox)textEntryView.findViewById(R.id.autoLogin);
	        			if (username.getText() != "" && password.getText() != "")
	        			{
	        				final RequestTask rt = new RequestTask();
	                    	MainTabActivity.this.dialog = new ProgressDialog(MainTabActivity.this){
	            				protected void onStop() {
	            					rt.cancel();
	            				}
	            			};
	            			MainTabActivity.this.dialog.setMessage(getString(R.string.request));
	            			MainTabActivity.this.dialog.setCancelable(true);
	            			MainTabActivity.this.dialog.show();
	            			rt.login(username.getText().toString(), password.getText().toString(), autoLogin.isChecked(), mHandler);
	        			}
	                }
	            })
	            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	
	                }
	            }).create();
	            
	            this.loginDialog.show();
			}else
			{
				GlobalParam.getInstance().SetLastLoginInfo(null, null, null, null, null, false);
				TextView m = (TextView) findViewById(R.id.message);
				m.setText("你还没有登录");
				t.setText("登录");
			}
		}
	}
}