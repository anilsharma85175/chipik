package com.chipik;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

public class BaseApplication extends Application {
	public static String PACKAGE_NAME;
	public static String VERSION_NAME;

	private static Context _context;
	private static SharedPreferences preferences = null;

	private BaasBox box;

	@Override
	public void onCreate() {
		super.onCreate();
		VERSION_NAME = getVersionName();
		_context = this;
		preferences = getSharedPreferences(getString(R.string.app_name), 0);
		PACKAGE_NAME = _context.getPackageName();

		//baasbox-android sdk 0.7.4 version code 
		BaasBox.Builder builder = new BaasBox.Builder(this);
		box = builder.setAuthentication(BaasBox.Config.AuthType.SESSION_TOKEN)
				.setAppCode("1234567890")
				.setHttpSocketTimeout(10000)
				.setHttpConnectionTimeout(10000)
				.setApiDomain("192.168.1.4") //host local - 192.168.1.x server - baasbox-anilsharma.rhcloud.com
				.setPort(9000) //local 9000, server 80
				.init();
		//baasbox-android sdk  0.7.3 version code    
		/*BaasBox.Config config = new BaasBox.Config();
		config.authenticationType= BaasBox.Config.AuthType.SESSION_TOKEN;
		config.appCode = "1234567890";
		config.apiDomain = "baasbox-anilsharma.rhcloud.com";//host local - 192.168.1.2 server - baasbox-anilsharma.rhcloud.com
		config.httpPort = 80;//local 9000		
		box = BaasBox.initDefault(this,config);*/

		//String endpoint = box.requestFactory.getEndpoint("file/{}", id);
	}

	public BaasBox getBaasBox(){
		return box;
	}

	public static SharedPreferences getPreferences(){
		if (preferences == null)
			preferences = _context.getSharedPreferences(_context.getString(R.string.app_name), 0);
		return preferences;
	}

	public boolean isAppInstalled(String paramString) {
		try{
			getPackageManager().getApplicationInfo(paramString, 0);
			return true;
		}catch (PackageManager.NameNotFoundException localNameNotFoundException){
		}
		return false;
	}

	public boolean isAppRunning(String paramString){
		List localList = ((ActivityManager)getSystemService("activity")).getRunningAppProcesses();
		for (int i = 0; ; i++){
			int j = localList.size();
			boolean bool = false;
			if (i < j){
				if (((ActivityManager.RunningAppProcessInfo)localList.get(i)).processName.equals(paramString))
					bool = true;
			}
			else
				return bool;
		}
	}

	private String getVersionName() {
		PackageManager localPackageManager = getPackageManager();
		try {
			String str = localPackageManager.getPackageInfo(getPackageName(), 0).versionName;
			return str;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException){
		}
		return "";
	}
	
	static Picasso singleton = null;	
	public static Picasso getPicasso() {
		if (singleton == null){
		    Picasso.Builder builder = new Picasso.Builder(_context);
		    builder.downloader(new UrlConnectionDownloader(_context) {//OkHttpDownloader
		        @Override
		        protected HttpURLConnection openConnection(Uri uri) throws IOException {
		            HttpURLConnection connection = super.openConnection(uri);
		            connection.setRequestProperty("X-BB-SESSION", BaasUser.current().getToken());
		            return connection;
		        }
		    });
		    builder.debugging(true);
		    singleton = builder.build();
		}
		return singleton;
	}
}
