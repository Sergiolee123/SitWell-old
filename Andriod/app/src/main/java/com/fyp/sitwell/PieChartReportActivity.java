package com.fyp.sitwell;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartReportActivity extends AppCompatActivity {
    private PieChart pieChart;
    private DBHandler dbHandler;
    private static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_report);
        pieChart=findViewById(R.id.pieChart);
        dbHandler=new DBHandler(this);
        cursor = dbHandler.getLatestRec();
        setupPieChart();
        loadPieChartData();
    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        /*entries.add(new PieEntry(0.0f, "Food & Dining"));
        entries.add(new PieEntry(0.0f, "Medical"));
        entries.add(new PieEntry(0.0f, "Entertainment"));
        entries.add(new PieEntry(0.0f, "Electricity and Gas"));
        entries.add(new PieEntry(0.0f, "Housing"));*/

        cursor.moveToFirst();
        Log.e("cursor", ""+cursor.getFloat(7)+" ,"+cursor.getColumnName(7));
        if(cursor.getCount()==1 && (int)(cursor.getFloat(7))==100){
            entries.add(new PieEntry(1.0f, cursor.getColumnName(7)));
            //Log.e("cursor fk", ""+cursor.getFloat(7)+" ,"+cursor.getColumnName(7));
           /* entries.add(new PieEntry(0f, cursor.getColumnName(0)));
            entries.add(new PieEntry(0f, cursor.getColumnName(1)));
            entries.add(new PieEntry(0f, cursor.getColumnName(2)));
            entries.add(new PieEntry(0f, cursor.getColumnName(3)));
            entries.add(new PieEntry(0f, cursor.getColumnName(4)));
            entries.add(new PieEntry(1.0f, cursor.getColumnName(7)));*/
        }
        if(cursor.getCount()==1 && ((int)(cursor.getFloat(7)))<100){
            entries.add(new PieEntry(0f, cursor.getColumnName(1)));
            entries.add(new PieEntry(0f, cursor.getColumnName(2)));
            entries.add(new PieEntry(0f, cursor.getColumnName(3)));
            entries.add(new PieEntry(0f, cursor.getColumnName(4)));
        }


        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
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
        //pieChart.setCenterText("Category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend legend=pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    //retrieve the count
}