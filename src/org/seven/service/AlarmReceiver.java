package org.seven.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Calendar;

import org.seven.activity.R;
import org.seven.activity.UpdateActivity;
import org.seven.utils.GlobalParam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//接收广播，每天的9点10分满足进行提醒
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		if ((hour == 7 || hour == 12 || hour == 17) && minute == 15){
			//检查版本
			new Thread(){
				public void run(){
					HttpURLConnection conn = null;
					InputStream is = null;
					FileOutputStream fos = null;
					
					String tickerText = GlobalParam.getInstance().getString(R.string.update_notify);
			        Notification notif = new Notification(R.drawable.icon, tickerText, System.currentTimeMillis());
			        NotificationManager nm = (NotificationManager)GlobalParam.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
			        Intent i = new Intent(GlobalParam.getInstance(),UpdateActivity.class);
			        PendingIntent contentIntent = PendingIntent.getActivity(GlobalParam.getInstance(), GlobalParam.getInstance().UPDATE_ID,i, PendingIntent.FLAG_ONE_SHOT );
					
			        File f = new File("/sdcard/1510/One5One10.apk");
			        File file = new File("/sdcard/1510/One5One10.tmp");
			        
			        try{
						URL u = new URL("http://218.83.243.78:3386/version.txt");
						ConnectivityManager connMng = (ConnectivityManager)GlobalParam.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo netInf = connMng.getActiveNetworkInfo();
						
						if (netInf!=null && "WIFI".equalsIgnoreCase(netInf.getTypeName())){
							conn = (HttpURLConnection)u.openConnection();
						}else{
							String proxyHost = android.net.Proxy.getDefaultHost();
							if (proxyHost != null){
								java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress(android.net.Proxy.getDefaultHost(),android.net.Proxy.getDefaultPort()));
								conn = (HttpURLConnection)u.openConnection(p);
							}else{
								conn = (HttpURLConnection)u.openConnection();
							}
						}
						is = conn.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] data = new byte[1024];
						int chunk = 0;
						while((chunk = is.read(data))!=-1){
							baos.write(data, 0, chunk);
						}
						String version = new String(baos.toByteArray());
						PackageInfo pi = GlobalParam.getInstance().getPackageManager().getPackageInfo(GlobalParam.getInstance().getPackageName(), 0);
						if (Double.parseDouble(version)>Double.parseDouble(pi.versionName)){
							if (null != is){
								try {
									is.close();
								} catch (Exception e) {
								}
							}
							
							if (null != conn){
								conn.disconnect();
							}
							//需要更新
							File dir = new File("/sdcard/1510");
							if (!dir.exists()){
								dir.mkdir();
							}
							
							f.deleteOnExit();
							
					        notif.setLatestEventInfo(GlobalParam.getInstance(), GlobalParam.getInstance().getString(R.string.app_name), GlobalParam.getInstance().getString(R.string.update_inprogress), contentIntent);
					        nm.notify(GlobalParam.getInstance().UPDATE_ID, notif);

							u = new URL("http://218.83.243.78:3386/One5One10.apk");

							if (netInf!=null && "WIFI".equalsIgnoreCase(netInf.getTypeName())){
								conn = (HttpURLConnection)u.openConnection();
							}else{
								String proxyHost = android.net.Proxy.getDefaultHost();
								if (proxyHost != null){
									java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress(android.net.Proxy.getDefaultHost(),android.net.Proxy.getDefaultPort()));
									conn = (HttpURLConnection)u.openConnection(p);
								}else{
									conn = (HttpURLConnection)u.openConnection();
								}
							}
							is = conn.getInputStream();
							String length = conn.getHeaderField("Content-Length");
							Log.i("1501",length);
							if (file.exists()){
								//tmp文件存在,比较大小
								if (file.length() == Long.parseLong(length)){
									//文件大小一致
									notif.setLatestEventInfo(GlobalParam.getInstance(), GlobalParam.getInstance().getString(R.string.app_name), GlobalParam.getInstance().getString(R.string.update_done), contentIntent);
									nm.notify(GlobalParam.getInstance().UPDATE_ID, notif);
									throw new Exception();
									//return;
								}else{
									//不一致则为未清理文件,需清理后创建新的下载
									file.delete();
									file.createNewFile();
								}
							}else{
								file.createNewFile();
							}
							data = new byte[255*255];
							chunk = 0;
							int sum = 0;
							fos = new FileOutputStream(file);
							while((chunk = is.read(data))!=-1){
								fos.write(data, 0, chunk);
								fos.flush();
								sum+=chunk;
								notif.setLatestEventInfo(GlobalParam.getInstance(), GlobalParam.getInstance().getString(R.string.app_name), (int)(100*sum/Double.parseDouble(length))+"%", contentIntent);
								nm.notify(GlobalParam.getInstance().UPDATE_ID, notif);
							}
							notif.setLatestEventInfo(GlobalParam.getInstance(), GlobalParam.getInstance().getString(R.string.app_name), GlobalParam.getInstance().getString(R.string.update_done), contentIntent);
							nm.notify(GlobalParam.getInstance().UPDATE_ID, notif);
						}
					}catch(Exception e){
						f.deleteOnExit();
						file.deleteOnExit();
						Log.i("1510",e.toString());
						nm.cancelAll();
					}finally{
						if (null != fos){
							try {
								fos.close();
							} catch (Exception e) {
							}
						}
						
						if (null != is){
							try {
								is.close();
							} catch (Exception e) {
							}
						}
						
						if (null != conn){
							conn.disconnect();
						}
					}
				}
			}.start();
		}
	}
}