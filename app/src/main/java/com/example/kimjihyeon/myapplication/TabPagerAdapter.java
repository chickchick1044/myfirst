package com.example.kimjihyeon.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class TabPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FriendFragment tabFragment2 = new FriendFragment();
                //ChatFragment tabFragment1 = new ChatFragment();
                return tabFragment2;
            case 1:
                ChatFragment tabFragment1= new ChatFragment();
                return tabFragment1;
                //FriendFragment tabFragment2 = new FriendFragment();
                //return tabFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
