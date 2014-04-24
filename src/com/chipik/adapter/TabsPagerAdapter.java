package com.chipik.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chipik.fragment.LocationFragment;
import com.chipik.fragment.PikStreamFragment;
import com.chipik.fragment.RootFragment;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {

	List<Fragment> fragments = new ArrayList<Fragment>();
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<Fragment>();
        fragments.add(new RootFragment());
        fragments.add(new PikStreamFragment());
        fragments.add(new LocationFragment());
    }
 
    @Override
    public Fragment getItem(int index) {
    	return fragments.get(index);
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}