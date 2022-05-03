package com.fyp.sitwell;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SlidePagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2;
    public SlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new InitialSettingPageFragment();
            case 0:
            default:
                return new IntroPageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}