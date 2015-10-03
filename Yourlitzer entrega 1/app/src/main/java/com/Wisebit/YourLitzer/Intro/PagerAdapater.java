package com.Wisebit.YourLitzer.Intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Alexis on 30-09-2015.
 */
public class PagerAdapater extends FragmentPagerAdapter {
    public PagerAdapater(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }
    public Fragment getItem(int arg0){
        switch (arg0){
            case 0:
                return new fm1();
            case 1:
                return new fm2();
            case 2:
                return new fm3();
            default:
                break;
        }
        return null;
    }

    public int getCount(){
        return 3;
    }
}



