package com.fyp.sitwell;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HabitCorrectionFragment extends Fragment {

    private Bitmap bitmap;

    public HabitCorrectionFragment(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_habit_correction, container, false);
    }

    public void setTextView(String text){
        TextView textView = getActivity().findViewById(R.id.text_habit_remind);
        textView.setText(text);
    }

    public void setImageView(Bitmap bitmap){
        ImageView imageView = getActivity().findViewById(R.id.image_habit_webcam);
        imageView.setImageBitmap(bitmap);
    }

    public void showCorrectPose(){
        ImageView imageView = getActivity().findViewById(R.id.image_correctPose);
        imageView.setImageBitmap(bitmap);
    }

    public void clearAll(){
        TextView textView = getActivity().findViewById(R.id.text_habit_remind);
        textView.setText("Great! You are in Correct Posture! Keep it!");
        ImageView imageView = getActivity().findViewById(R.id.image_correctPose);
        imageView.setImageResource(android.R.color.transparent);
    }
}