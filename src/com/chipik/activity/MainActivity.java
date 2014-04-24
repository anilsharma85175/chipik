package com.chipik.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.RequestToken;
import com.chipik.R;
import com.chipik.adapter.TabsPagerAdapter;
import com.chipik.fragment.CustomCameraHost;
import com.chipik.fragment.CustomCameraHost.OnCameraClickedCallBack;
import com.chipik.fragment.PikStreamFragment;
import com.chipik.fragment.PostFragment;
import com.chipik.fragment.PostFragment.OnPostUploadCallback;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;


public class MainActivity extends ActionBarActivity 
	implements ActionBar.TabListener, CameraHostProvider, OnCameraClickedCallBack, OnPostUploadCallback, LocationListener {

	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	private LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (BaasUser.current() == null){//Check user logged in
			startLoginScreen();
	        return;
		}
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 60000, 1000, this);//60mins,1km
		
		setContentView(R.layout.activity_main);

		if (savedInstanceState!=null){
            logoutToken = RequestToken.loadAndResume(savedInstanceState,LOGOUT_TOKEN_KEY,logoutHandler);
        }
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		actionBar.addTab(actionBar.newTab().setText("Camera")
				.setIcon(getResources().getDrawable(R.drawable.camera_selector))
				.setTabListener(this));

		actionBar.addTab(actionBar.newTab().setText("Stream")
				.setIcon(getResources().getDrawable(R.drawable.grid_selector))
				.setTabListener(this));

		actionBar.addTab(actionBar.newTab().setText("Location")
				.setIcon(getResources().getDrawable(R.drawable.location_selector))
				.setTabListener(this));
		
		//on swiping the viewpager make respective tab selected
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {// on changing the page, make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		//grid is default
		if (savedInstanceState != null) {
			viewPager.setCurrentItem(savedInstanceState.getInt("tab", 1));
        }
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onStop() {
        super.onStop();
	}
	
	//Login/Logout checks
	private final static String LOGOUT_TOKEN_KEY = "logout";
    private RequestToken logoutToken;
    private final BaasHandler<Void> logoutHandler = new BaasHandler<Void>() {
    	@Override
    	public void handle(BaasResult<Void> voidBaasResult) {
    		logoutToken=null;
    		onLogout();
    	}
	};
	
    private void onLogout(){
        startLoginScreen();
    }
    
	private void startLoginScreen(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }	
            
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
        if (logoutToken!=null){
        	logoutToken.suspendAndSave(outState, LOGOUT_TOKEN_KEY);
        }    	
    }        
                
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar actions click
		switch (item.getItemId()) {
			case R.id.menu_photo : {
				viewPager.setCurrentItem(0);
				return true;
			}
			case R.id.menu_refresh : {
	            return true;
	        }
			case R.id.menu_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}
	
	//-- Fragment Listener Methods
	@Override
	public void onPhotoClicked() {//Show PostFragments
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		/*
		 * IMPORTANT: We use the "root frame" defined in fragment_root.xml" as the reference to replace fragment
		 */
		trans.replace(R.id.root_frame, new PostFragment());

		/*
		 * IMPORTANT: The following lines allow us to add the fragment
		 * to the stack and return to it later, by pressing back
		 */
		trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		trans.addToBackStack(null);
		trans.commit();
	}
	
	@Override
	public void onPhotoUploaded() {//Show PikStream tab with refreshed content
		PikStreamFragment fragment = (PikStreamFragment) mAdapter.getItem(1);
		fragment.refresh();		
		actionBar.setSelectedNavigationItem(1);
		viewPager.setCurrentItem(1);		
	}

	// -- CameraHostProvider interface
	private CameraHost host = null;
	@Override
	public CameraHost getCameraHost() {
		if (host == null) {
			host = new CustomCameraHost(this);
		}
		return host;
	}

	Location location;
	@Override
	public Location getLocation() {
		return location;
	};
	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Provider : " + provider + " disabled", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Provider : " + provider + " enabled", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Toast.makeText(this, "Statuschanged - Provider : " + provider, Toast.LENGTH_LONG).show();
		
	}	
}
