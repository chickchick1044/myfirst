package com.example.kimjihyeon.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFab1;
    private TabPagerAdapter mPagerAdapter;

    private String TAG = "MainActivity";
    public static final boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout)findViewById(R.id.tabLayout);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        FloatingActionButton mFab1 = (FloatingActionButton)findViewById(R.id.fab1);
        mFab1.setOnClickListener(this);

        mTabLayout.setupWithViewPager(mViewPager);
        setUpViewPager();

    }//onCreate ended

    private void setUpViewPager() {
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new ChatFragment(), "채팅");
        mPagerAdapter.addFragment(new FriendFragment(), "친구");
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab1:
                Fragment currentFragment = mPagerAdapter.getItem(mViewPager.getCurrentItem());
                if (currentFragment instanceof FriendFragment){
                    //Toast.makeText(MainActivity.this, "Friend Fragment 입니다.", Toast.LENGTH_LONG).show();
                    ((FriendFragment)currentFragment).toggleSearchBar();
                }else{
                    //Toast.makeText(MainActivity.this, "Friend Fragment 가 아닙니다.", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}//mainActivity ended
