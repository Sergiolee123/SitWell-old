package com.fyp.sitwell;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SlidePagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;
    public SlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new IntroPageFragment();
            case 1:
                return new ProfilePageFragment();
            case 2:
                return new SettingPageFragment();
            default:
                return new IntroPageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}