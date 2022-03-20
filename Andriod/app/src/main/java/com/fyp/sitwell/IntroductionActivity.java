package com.fyp.sitwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class IntroductionActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 3;

    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private FragmentStateAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        mViewPager = findViewById(R.id.viewPager2);
        mTabLayout = findViewById(R.id.tabLayout);
        mPagerAdapter = new SlidePagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager,
                (tab, position) -> {}).attach();
/*
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        */

    }


}