package com.fyp.sitwell;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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

public class PieChartReportActivity extends AppCompatActivity {
    private PieChart pieChart;
    private DBHandler dbHandler;
    private static Cursor cursor, cursor2;
    private Button homeBtn;
    private TextView userProgTextView, PerfectMsgTextView, pieChartTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_report);
        pieChart=findViewById(R.id.pieChart);
        homeBtn=findViewById(R.id.HomeBtn);
        userProgTextView=findViewById(R.id.userProgText);
        PerfectMsgTextView=findViewById(R.id.PerfectMessage);
        pieChartTextView=findViewById(R.id.pieChartText);
        dbHandler=new DBHandler(this);
        cursor = dbHandler.getUserProgress();
        cursor2 = dbHandler.getLatestRec();
        cursor2.moveToNext();

        if(cursor2.getInt(0)==0 &&cursor2.getInt(1) ==0 && cursor2.getInt(2)==0 && cursor2.getInt(3)==0 && cursor2.getInt(4)==0){
            setUpPerfectMsg();
        }else{
            setupPieChart();
            loadPieChartData();
        }

    }

    private void setUpPerfectMsg(){
        pieChartTextView.setText("Last Sitting record is perfect");
        PerfectMsgTextView.setText("Keep Working");
    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        cursor.moveToFirst();
        float progStats = cursor.getFloat(0)*100;
        DecimalFormat df = new DecimalFormat("0.00");
        userProgTextView.setText("Finished "+ df.format(progStats)+ " %");

        int neckCount= cursor2.getInt(0);
        int backCount = cursor2.getInt(1);
        int SHLDRCount = cursor2.getInt(2);
        int LT_ARM_Count = cursor2.getInt(3);
        int RT_ARM_Count = cursor2.getInt(4);
        int total_count = (cursor2.getInt(5) + cursor2.getInt(6))*5;

        Log.e("neckCount", ""+neckCount);
        Log.e("backCount", ""+backCount);
        Log.e("SHLDRCount", ""+SHLDRCount);
        Log.e("LT_ARM_Count", ""+LT_ARM_Count);
        Log.e("RT_ARM_Count", ""+RT_ARM_Count);

        float neckError= (float)(neckCount)/total_count*100;
        float backError= (float)(backCount)/total_count*100;
        float SHLDRError= (float)(SHLDRCount)/total_count*100;
        float LT_ARM_Error= (float)(LT_ARM_Count)/total_count*100;
        float RT_ARM_Error= (float)(RT_ARM_Count)/total_count*100;

        Log.e("neckError", ""+neckError);
        Log.e("backError", ""+backError);
        Log.e("SHLDRError", ""+SHLDRError);
        Log.e("LT_ARM_Error", ""+LT_ARM_Error);
        Log.e("RT_ARM_Error", ""+RT_ARM_Error);

        if((int)(neckError)!=0)
            entries.add(new PieEntry(neckError, "neckError"));
        if((int)(backError)!=0)
            entries.add(new PieEntry(backError, "backError"));
        if((int)(SHLDRError)!=0)
            entries.add(new PieEntry(SHLDRError, "SHLDRError"));
        if((int)(LT_ARM_Error)!=0)
            entries.add(new PieEntry(LT_ARM_Error, "LT_ARM_Error"));
        if((int)(RT_ARM_Error)!=0)
            entries.add(new PieEntry(RT_ARM_Error, "RT_ARM_Error"));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "UserProgress");
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


}