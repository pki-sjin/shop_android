package org.seven.service;

import org.seven.utils.GlobalParam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartupReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
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
}
