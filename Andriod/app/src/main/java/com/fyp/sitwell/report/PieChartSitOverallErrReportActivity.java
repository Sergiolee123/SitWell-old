package com.fyp.sitwell.report;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class PieChartSitOverallErrReportActivity extends AppCompatActivity{
    private PieChart pieChart;
    private DBHandler dbHandler;
    private Cursor cursor, cursor2;
    private Button homeBtn;
    private TextView userProgTextView, PerfectMsgTextView, pieChartTextView;
    private int neckCount=0,backCount=0, SHLDRCount=0,LT_ARM_Count=0,RT_ARM_Count=0 ,sitWellCount=0,sitBadCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_sitting_report);
        dbHandler=new DBHandler(this);
        pieChart=findViewById(R.id.pieChart);
        homeBtn=findViewById(R.id.HomeBtn);
        userProgTextView=findViewById(R.id.userProgText);
        PerfectMsgTextView=findViewById(R.id.PerfectMessage);
        pieChartTextView=findViewById(R.id.pieChartText);
        cursor = dbHandler.getUserProgressStatus();
        //cursor2 = dbHandler.getTheLatestSittingRecData();
        cursor2 = dbHandler.getAllSitRecs();

        Log.e("cursor2.getCount()",cursor2.getCount()+"");
        Log.e("cursor.getCount()",cursor.getCount()+"");

        if(cursor2.getCount()>0){
            handleAllCounts();
           /* cursor2.moveToNext();
            neckCount=cursor2.getInt(0);
            backCount=cursor2.getInt(1);
            SHLDRCount=cursor2.getInt(2);
            LT_ARM_Count=cursor2.getInt(3);
            RT_ARM_Count=cursor2.getInt(4);
            Log.e("fk1",""+cursor2.getInt(0) + " "+ cursor2.getInt(1)+ " "+ cursor2.getInt(2)+ " "+ cursor2.getInt(3)+ " "+ cursor2.getInt(4));*/
            if( neckCount==0 && backCount==0 && SHLDRCount==0 && LT_ARM_Count==0  && RT_ARM_Count==0){
                setUpPerfectMsg();
            }else{
                setupPieChart();
                loadPieChartData();
            }
        }else{
            setUpNoRecMsg();
        }

      /*  if(cursor3.getCount()>0){
            handleAllCounts();
            if( neckCount==0 && backCount==0 && SHLDRCount==0 && LT_ARM_Count==0  && RT_ARM_Count==0){
                setUpPerfectMsg();
            }else{
                setupSpinnerSelection();
                setupPieChart();
                loadPieChartData();
            }
        }else{
            setUpNoRecMsg();
        }*/

        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void handleAllCounts() {
        Log.e("cursor2.moveToNext()" , ""+ cursor2.getCount());
        //cursor2.moveToNext();
        int count =0;
        while(cursor2.moveToNext()){
            neckCount+=cursor2.getInt(2);
            backCount+=cursor2.getInt(3);
            SHLDRCount+=cursor2.getInt(4);
            LT_ARM_Count+=cursor2.getInt(5);
            RT_ARM_Count+=cursor2.getInt(6);
            sitWellCount+=cursor2.getInt(7);
            sitBadCount+=cursor2.getInt(8);
            Log.e("while loop", ""+ cursor2.getInt(2)+ cursor2.getInt(3)+ cursor2.getInt(4) +cursor2.getInt(5)+ cursor2.getInt(6));
            count++;
            Log.e("count",""+count);
        }
        /*cursor2.moveToNext();
        neckCount=cursor2.getInt(0);
        backCount=cursor2.getInt(1);
        SHLDRCount=cursor2.getInt(2);
        LT_ARM_Count=cursor2.getInt(3);
        RT_ARM_Count=cursor2.getInt(4);*/

    }

    private void setUpNoRecMsg(){
        pieChartTextView.setText("No Sitting record is found");
        PerfectMsgTextView.setText("");
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
        userProgTextView.setText("Finished  68 %");//demo

        int total_count = (sitWellCount + sitBadCount)*5;

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