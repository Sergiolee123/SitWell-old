package com.fyp.sitwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class InitialSettingPageFragment extends Fragment {
    private Button mStartBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_initialsetting, container, false);
        mStartBtn = view.findViewById(R.id.button_start);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent MainActivity = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(MainActivity);
            }
        });
        return view;
    }
}