package ca.runner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
 
public class SplashScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME = 3000;
	Handler mHandler;
	Runnable splashRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		splashRunnable = new Runnable() {

			public void run() {
				splash();
			}
		};

		mHandler = new Handler();
		mHandler.postDelayed(splashRunnable, SPLASH_TIME);
	}

	private void splash() {
		//start splash
		if(isFinishing())
			return;
		startActivity(new Intent(this, MainMenu.class));
		finish();
	}
}