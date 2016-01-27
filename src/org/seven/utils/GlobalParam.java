package org.seven.utils;

import org.seven.data.DatabaseHelper;
import org.seven.data.LoginInfo;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;

public class GlobalParam extends Application {

	private static GlobalParam gp;
	
	private DatabaseHelper dh;
	
	public boolean isRunning = false;
	
	public String IMEI;
	
	public enum BillSortType
	{
		NO_SORT,
		BILLNO_SORT_DOWN,
		BILLNO_SORT_UP,
		S_STORE_SORT_DOWN,
		S_STORE_SORT_UP,
		S_DATE_SORT_DOWN,
		S_DATE_SORT_UP,
		PACKAGECNT_SORT_DOWN,
		PACKAGECNT_SORT_UP,
		O_NUM_SORT_UP,
		O_NUM_SORT_DOWN,
		A_NUM_SORT_DOWN,
		A_NUM_SORT_UP,
		S_NUM_SORT_DOWN,
		S_NUM_SORT_UP,
		SKUCOUNT_SORT_DOWN,
		SKUCOUNT_SORT_UP,
		REMARK_SORT_DOWN,
		REMARK_SORT_UP,
		SATISFIED_SORT_DOWN,
		SATISFIED_SORT_UP
	}
	
	public BillSortType billSortType = BillSortType.S_DATE_SORT_DOWN;
	
	//π„≤•±Í æ
	public final String CLOCK_ACTION = "org.seven.clock.CLOCK_ACTION";
	
	public final int NOTICE_ID = "org.seven.notice".hashCode();
	
	public final int UPDATE_ID = "org.seven.update".hashCode();
	
	public static final int BROADCAST_ID = "org.seven.broadcast".hashCode();
	
	public static final int LOGIN_SUCCESS = 1;
	
	public static final int GETSTORES_SUCCESS = 11;
	
	public static final int GETBILLS_SUCCESS = 111;
	
	public static final int GETDETAIL_SUCCESS = 1111;
	
	public static final int SIGN_SUCCESS = 11111;
	
	public static final int REGISTER_SUCCESS = 111111;
	
	public BMapManager mBMapMan = null;
	
	public static GlobalParam getInstance(){
		return gp;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		IMEI = tm.getDeviceId();
		dh = new DatabaseHelper(this);
		mBMapMan=new BMapManager(this);
//		mBMapMan.init("3m26YaCXivQcG53d7Nfo6mA7", null);
		mBMapMan.init("eq41z6hM0WAvt6TIf3UcK06B", null);
		gp = this;
	}
	
	public DatabaseHelper getDB(){
		if (dh == null){
			dh = new DatabaseHelper(this);
		}
		return dh;
	}
	
	public void closeDB(){
		try{
			dh.close();
		}catch(Exception e){
			Log.i("1510", e.getMessage(), e);
		}
		dh = null;
	}
	
	public LocationData GetLastLocation()
	{
		SharedPreferences share = this.getSharedPreferences("LOCATION", MODE_PRIVATE);
		LocationData locData = new LocationData();
		locData.latitude = share.getFloat("latitude", 39.915f);
		locData.longitude = share.getFloat("longitude", 116.404f);
		return locData;
	}
	
	public void SetLastLocation(LocationData locData)
	{
		SharedPreferences share = this.getSharedPreferences("LOCATION", MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putFloat("latitude", (float)locData.latitude);
		editor.putFloat("longitude", (float)locData.longitude);
		editor.commit();
	}
	
	public void SetLastLoginInfo(String sid, String userno, String username, String password, String position, boolean autoLogin)
	{
		SharedPreferences share = this.getSharedPreferences("LOGININFO", MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString("sid", sid);
		editor.putString("userno", userno);
		editor.putString("username", username);
		editor.putString("password", password);
		editor.putString("position", position);
		editor.putBoolean("autoLogin", autoLogin);
		editor.commit();
	}
	
	public LoginInfo GetLastLoginInfo()
	{
		SharedPreferences share = this.getSharedPreferences("LOGININFO", MODE_PRIVATE);
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setSid(share.getString("sid", null));
		loginInfo.setUserno(share.getString("userno", null));
		loginInfo.setUsername(share.getString("username", null));
		loginInfo.setPassword(share.getString("password", null));
		loginInfo.setPosition(share.getString("position", null));
		loginInfo.setAutoLogin(share.getBoolean("autoLogin", false));
		return loginInfo;
	}
}