package com.fyp.sitwell;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetupPostureFragment extends Fragment {

    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_posture, container, false);
    }

    public void setImage(Bitmap bitmap){
        ImageView imageView = (ImageView) this.getActivity().findViewById(R.id.image_webcam);
        this.bitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    public Bitmap getImage(){
        return bitmap;
    }

}