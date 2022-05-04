package com.fyp.sitwell.report;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fyp.sitwell.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PieChartExFreqReportActivity extends AppCompatActivity {
    private PieChart pieChart;
    private DBHandler dbHandler;
    private static Cursor cursor;
    private Button homeBtn;
    private TextView userProgTextView, PerfectMsgTextView, pieChartTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_exercise_report);
        dbHandler = new DBHandler(this);
        pieChart=findViewById(R.id.pieChart);
        homeBtn=findViewById(R.id.HomeBtn);
        userProgTextView=findViewById(R.id.userProgText);
        PerfectMsgTextView=findViewById(R.id.PerfectMessage);
        pieChartTextView=findViewById(R.id.pieChartText);
        cursor=dbHandler.getRecentUserExerciseData();

        if(cursor.getCount()==1){
            cursor.moveToNext();
            if(cursor.getInt(2)==0 &&cursor.getInt(3)==0){
                setUpNoRecMsg();
            }else{
                setupPieChart();
                loadPieChartData();
            }
        }else {
            setUpNoRecMsg();
        }

        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void setUpNoRecMsg(){
        pieChartTextView.setText("No Exercise record is found");
        PerfectMsgTextView.setText("");
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setCenterText("UserProgress");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend legend=pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    private void loadPieChartData(){
        float progStats = cursor.getFloat(0)*100;
        DecimalFormat df = new DecimalFormat("0.00");
        userProgTextView.setText("Finished "+ df.format(progStats)+ " %");
        //demo purpose
        userProgTextView.setText("Finished 34.9 %");

        ArrayList<PieEntry> entries = new ArrayList<>();
        int strengthExCount=cursor.getInt(2);
        int relaxExCount = cursor.getInt(3);

        float strengthExFloat=(float)(strengthExCount);
        float relaxExFloat=(float) (relaxExCount);

        if(strengthExCount!=0){
            entries.add(new PieEntry(strengthExFloat,"Strength section"));
        }
        if(relaxExCount!=0){
            entries.add(new PieEntry(relaxExFloat,"Relax section"));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "UserExercise");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1000, Easing.EaseInOutQuad);
    }

}