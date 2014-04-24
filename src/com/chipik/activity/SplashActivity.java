package com.chipik.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.chipik.R;

public class SplashActivity extends Activity {
	protected int _splashTime = 500;

	public void onAttachedToWindow(){
		super.onAttachedToWindow();
		getWindow().setFormat(PixelFormat.RGBA_8888);
	}

	public void onCreate(Bundle paramBundle){
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_splash);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Intent localIntent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(localIntent);
				finish();
			}
		}
		, this._splashTime);
	}
}
