package com.example.android.bakingtime;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mihirnewalkar on 10/2/17.
 */

public class StepsFragmentPagerAdapter extends FragmentPagerAdapter{
    public StepsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==  0) {
            return new StepFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
