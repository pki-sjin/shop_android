package org.seven.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	private boolean _active = true;
    private int _splashTime = 2000;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                	// 启动主应用
                	Intent i = new Intent(SplashActivity.this,MainTabActivity.class);
                    startActivity(i);
                	finish();
                }
            }
        };
        splashTread.start();
	}
}
