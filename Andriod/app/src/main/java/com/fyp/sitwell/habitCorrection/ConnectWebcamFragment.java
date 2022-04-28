package com.fyp.sitwell.habitCorrection;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyp.sitwell.R;

public class ConnectWebcamFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connect_webcam, container, false);
    }

    public void connectSuccess(){
        TextView textView = (TextView) getActivity().findViewById(R.id.text_status);
        textView.setText("Online");
        textView.setTextColor(Color.parseColor("#00FF00"));
    }

    public void connectFail(){
        TextView textView = (TextView) getActivity().findViewById(R.id.text_status);
        textView.setText("Online");
        textView.setTextColor(Color.parseColor("#B50303"));
    }

}