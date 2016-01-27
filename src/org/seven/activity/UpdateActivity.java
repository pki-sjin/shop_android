package org.seven.activity;

import java.io.File;

import org.seven.utils.GlobalParam;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class UpdateActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final File file = new File("/sdcard/1510/One5One10.tmp");
		if (file.exists()){
			new Thread(){
				public void run(){
					File f = new File("/sdcard/1510/One5One10.apk");
					file.renameTo(f);
					NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				    nm.cancel(GlobalParam.getInstance().UPDATE_ID);
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
					startActivity(intent);
				}
			}.start();
		}
		finish();
	}
}