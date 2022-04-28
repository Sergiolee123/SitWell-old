package com.fyp.sitwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SettingPageFragment extends Fragment {
    private Button mStartBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (ViewGroup)  inflater.inflate(R.layout.fragment_page_setting, container, false);
    }
}
