package com.fyp.sitwell;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fyp.sitwell.muscleTrainingRecommendation.MuscleTrainingRecommendationActivity;
import com.fyp.sitwell.report.LineChartInStatsActivity;
import com.fyp.sitwell.report.PieChartExFreqReportActivity;
import com.fyp.sitwell.report.PieChartSitOverallErrReportActivity;

public class StatsFragment  extends Fragment {

    private Button lineChartBtn, pieChartSitBtn, pieChartExBtn, pieChartRecommendBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats ,container ,false);

        lineChartBtn=view.findViewById(R.id.lineChartBtn);
        pieChartSitBtn =view.findViewById(R.id.pieChartBtn);
        pieChartExBtn=view.findViewById(R.id.pieChartExBtn);
        pieChartRecommendBtn= view.findViewById(R.id.pieChartRecommendBtn);

        lineChartBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), LineChartInStatsActivity.class));
                getActivity().startActivityForResult(new Intent(getActivity(), LineChartInStatsActivity.class),111);
            }
        });

        pieChartSitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), PieChartSittingReportActivity.class));
                getActivity().startActivityForResult(new Intent(getActivity(), PieChartSitOverallErrReportActivity.class),112);
            }
        });

        pieChartExBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), LineChartInStatsActivity.class));
                getActivity().startActivityForResult(new Intent(getActivity(), PieChartExFreqReportActivity.class),113);
            }
        });

        pieChartRecommendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), LineChartInStatsActivity.class));
                getActivity().startActivityForResult(new Intent(getActivity(), MuscleTrainingRecommendationActivity.class),114);
            }
        });



        /*
        *    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 111) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                    new MainFragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_home);
        }
    }
*/


        return view;
    }
}