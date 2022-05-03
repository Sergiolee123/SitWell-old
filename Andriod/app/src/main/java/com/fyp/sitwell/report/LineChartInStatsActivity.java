package com.fyp.sitwell.report;

import static java.time.LocalDate.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.sitwell.R;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class LineChartInStatsActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static final String TAG = "LineChartInStatsActivity";
    private LineChart mpLineChart;
    private Legend legend;
    private DBHandler dbHandler;
    private Cursor cursor;
    private int cursorCount;
    private TextView topicTextView;
    private Spinner spinner;
    private static ArrayList<String> spinnerItemsList;
    private static ArrayList<Entry> weekOneRec;
    private static ArrayList<Entry> weekTwoRec;
    private static ArrayList<Entry> weekThreeRec;
    private Button BtnPieChart;

    private ArrayList<String> xAxisLabel = new ArrayList<>();
    private boolean checkXAxisLabel=false;
    String [] weekArr = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat","Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_in_statics);
        topicTextView = findViewById(R.id.topic1);
        spinner = findViewById(R.id.spinner1);
        BtnPieChart=findViewById(R.id.BtnPieChart);
        dbHandler = new DBHandler(this);
        spinnerItemsList = new ArrayList<>();
        cursor =dbHandler.getSelectedQuerySitAccuray();
        cursorCount= cursor.getCount();


        setUpXaxisLabels();
        if(cursorCount>0 && checkXAxisLabel==true){
            setupSpinnerSelection();
            LineChartSetup();
            loadLineChartData();
        }else{
            setUpNoDataDisplay();
        }

        BtnPieChart = findViewById(R.id.BtnPieChart);
        BtnPieChart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void setUpNoDataDisplay(){
        topicTextView.setText("No sitting Records are found");
    }

    private void setUpXaxisLabels(){
        Cursor c = dbHandler.getTheLatestSittingRecDate();
        Log.e("fk1",""+c.getCount());
        c.moveToNext();

        if(c.getCount()==0)return;
        //***
        String latestDate = c.getString(0);
        Log.e("latestDate", ""+latestDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        try {
            dt1 = format.parse(latestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("EE");
        Log.e("GET THE DAY OF THE WEEK", ""+ df.format(dt1));
        //***

        int dayCount =0;
        int dayEndPos=0;
        //get the dayEndPos
        for (int i=0;i<weekArr.length;i++){
            if(weekArr[i].equals(df.format(dt1))){
                dayCount++;
            }
            if(dayCount==2){
                dayEndPos=i;
                break;
            }
        }
        xAxisLabel.clear();
        for(int i=dayEndPos-cursorCount+1;i<=dayEndPos;i++){
            xAxisLabel.add(weekArr[i]);
        }
        checkXAxisLabel=true;
    }


    private void setupSpinnerSelection(){
        spinnerItemsList.clear();
        if(cursorCount<7){
            spinnerItemsList.add("Recent Days");
        }
        if(cursorCount==7){
            spinnerItemsList.add("Last 7 days");
        }
        if(cursorCount==14){
            spinnerItemsList.add("Last 14 days");
            spinnerItemsList.add("Last 7 days");

        }
        if(cursorCount==21){
            spinnerItemsList.add("Last 21 days");
            spinnerItemsList.add("Last 14 days");
            spinnerItemsList.add("Last 7 days");
        }
        String [] arr  = new String[spinnerItemsList.size()];
        for (int i=0;i<arr.length;i++) {
            arr[i] = spinnerItemsList.get(i);
            Log.e("setupSpinnerSelection()", ""+ arr[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void LineChartSetup() {
        mpLineChart = findViewById(R.id.linechart1);
        mpLineChart.setOnChartGestureListener(LineChartInStatsActivity.this);
        mpLineChart.setOnChartValueSelectedListener(LineChartInStatsActivity.this);
        mpLineChart.setDragEnabled(true);
        mpLineChart.setScaleEnabled(false);
        mpLineChart.setDrawGridBackground(true);
        mpLineChart.setDrawBorders(true);
        mpLineChart.setExtraOffsets(100,20,0,0);

        legend = mpLineChart.getLegend();
        legend.setWordWrapEnabled(true);

        YAxis yAxis = mpLineChart.getAxisLeft();
        yAxis.setLabelCount(10);
        yAxis.removeAllLimitLines();
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(0f);
        //yAxis.enableGridDashedLine(10f, 10f, 0);
        yAxis.setDrawLimitLinesBehindData(true);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        YAxis yAxis1 = mpLineChart.getAxisRight();
        yAxis1.setEnabled(false);

        //ValueFormatter xAxisFormatter = new LineChartInStatsActivity.DayAxisValueFormatter(mpLineChart);
        ValueFormatter xAxisFormatter = new LineChartInStatsActivity.DayAxisValueFormatter2(mpLineChart);
        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        mpLineChart.getDescription().setEnabled(false);
        mpLineChart.getLegend().setEnabled(true);
        mpLineChart.setExtraOffsets(10, 30, 10, 30);
        //mpLineChart.getXAxis().setLabelCount(5);

         /*LimitLine upper_limit = new LimitLine(65f,"Danger");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f,10f,0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(35f,"Too low");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f,10f,0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);*/

        //yAxis.addLimitLine(upper_limit);
        //yAxis.addLimitLine(lower_limit);

       /* LineDataSet set1 = new LineDataSet(dataValue1(), "Data Set 1");
        set1.setFillAlpha(50);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.GREEN);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();*/
    }

    private void loadLineChartData(){
        ArrayList<Float> arr = getDataValues();
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        Log.e("getArraylist()", ""+arr.size());
        weekOneRec= new ArrayList<Entry>();
        weekTwoRec= new ArrayList<Entry>();
        weekThreeRec= new ArrayList<Entry>();
        topicTextView.setText("sth wrong here");
       // Log.e("y_label",""+getInitialLabelDay());

        try {
            int y_label = 0;
            if(arr.size()<=7){
                for (int i = 0; i < arr.size(); i++) {
                    weekOneRec.add(new Entry(y_label++, arr.get(i)));
                }
            }else if(arr.size() == 14) {
                y_label = 0;
                Log.e("condition 2","condition 2");
                y_label = 0;
                for(int i=0;i<arr.size()/2;i++){
                    weekOneRec.add(new Entry(y_label++, arr.get(i)));
                }
                y_label = 0;
                for(int i=arr.size()/2;i<arr.size();i++){
                    weekTwoRec.add(new Entry(y_label++, arr.get(i)));
                }
                Log.e("weekTwoRec", ""+weekTwoRec.size());
            }else if (arr.size() == 21) {
                Log.e("condition 3","condition 3");
                y_label = 0;
                for (int i = 0; i < arr.size()/3; i++) {
                    weekOneRec.add(new Entry(y_label++, arr.get(i)));
                }
                y_label = 0;
                for (int i = arr.size()/3; i < arr.size()/3*2; i++) {
                    weekTwoRec.add(new Entry(y_label++, arr.get(i)));
                }
                y_label = 0;
                for (int i = arr.size()/3*2; i < arr.size(); i++) {
                    weekThreeRec.add(new Entry(y_label++, arr.get(i)));
                }
            }
            int initial_day = getInitialLabelDay();
            if (weekOneRec.size() > 0) {
                Log.e("weekOneRec.size() > 0" ,"weekOneRec.size() > 0");
                LineDataSet set1 = new LineDataSet(weekOneRec, "Days "+initial_day+"-"+(initial_day+6));
                set1.setFillAlpha(50);
                set1.setColor(Color.YELLOW);
                set1.setLineWidth(3f);
                set1.setValueTextSize(12f);
                set1.setValueTextColor(Color.BLACK);
                set1.setFillColor(Color.CYAN);
                // set1.setDrawValues(false);
                lineDataSets.add(set1);
            }
            if (weekTwoRec.size() > 0) {
                Log.e("weekTwoRec.size() > 0" ,"weekTwoRec.size() > 0");
                LineDataSet set2 = new LineDataSet(weekTwoRec, "Days "+(initial_day+7)+"-"+(initial_day+7+6));
                set2.setFillAlpha(50);
                set2.setColor(Color.CYAN);
                set2.setLineWidth(3f);
                set2.setValueTextSize(12f);
                set2.setValueTextColor(Color.BLACK);
                //set2.setDrawValues(false);
                lineDataSets.add(set2);
            }
            if (weekThreeRec.size() > 0) {
                Log.e("weekThreeRec.size() > 0" ,"weekThreeRec.size() > 0");
                LineDataSet set3 = new LineDataSet(weekThreeRec, "Days "+(initial_day+7*2)+"-"+(initial_day+7*2+6));
                set3.setFillAlpha(50);
                set3.setColor(Color.GREEN);
                set3.setLineWidth(5f);
                set3.setValueTextSize(12f);
                set3.setValueTextColor(Color.BLACK);
                // set3.setDrawValues(false);
                lineDataSets.add(set3);
            }
        }catch (Exception e){
            Log.e("dataSets2", "sth wrong");
        }

        LineData data = new LineData(lineDataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }

    private ArrayList<Float> getDataValues(){
        ArrayList<Float> arrayList = new ArrayList<Float>();
        while(cursor.moveToNext()){
            arrayList.add(cursor.getFloat(0));
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    private int getInitialLabelDay(){
        int recordID =0;
        try {
            Cursor cursor = dbHandler.getSelectedQuery();
            cursor.moveToLast();
            recordID = cursor.getInt(0);
        }catch(Exception e){
            Log.e("getInitialLabelDay()", "sth wrong");
        }
        return recordID;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (parent.getId() == R.id.spinner1) {
            String valueFromSpinner = parent.getItemAtPosition(pos).toString();
            Toast toast = Toast.makeText(this, valueFromSpinner, Toast.LENGTH_SHORT);
            toast.show();
        }

        updateSpinnerSelection(parent.getItemAtPosition(pos).toString());

    }


    private void updateSpinnerSelection(String pos){
        mpLineChart.clear();
        if(spinnerItemsList.size()>0){
            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            int init_days=getInitialLabelDay();
            if(cursorCount<7 && pos.equals("Recent Days")){
                LineDataSet set = new LineDataSet(weekOneRec, "Days "+init_days+"-"+(init_days+6));
                set.setFillAlpha(50);
                set.setColor(Color.YELLOW);
                set.setLineWidth(3f);
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.BLACK);
                lineDataSets.add(set);
                topicTextView.setText("Recent Days Sitting performance");
            }

            if(cursorCount==7 && pos.equals("Last 7 days")){
                LineDataSet set = new LineDataSet(weekOneRec, "Days "+init_days+"-"+(init_days+6));
                set.setFillAlpha(50);
                set.setColor(Color.YELLOW);
                set.setLineWidth(3f);
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.BLACK);
                lineDataSets.add(set);
                topicTextView.setText("Last 7 days Sitting performance");
            }

            if(cursorCount==14 && pos.equals("Last 7 days")){
                LineDataSet set = new LineDataSet(weekTwoRec, "Days "+(init_days+7)+"-"+(init_days+7+6));
                set.setFillAlpha(50);
                set.setColor(Color.GREEN);
                set.setLineWidth(3f);
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.BLACK);
                lineDataSets.add(set);
                topicTextView.setText("Last 7 days Sitting performance");
            }
            if(cursorCount==14 && pos.equals("Last 14 days")){
                LineDataSet set = new LineDataSet(weekOneRec, "Days "+init_days+"-"+(init_days+6));
                set.setFillAlpha(50);
                set.setColor(Color.YELLOW);
                set.setLineWidth(3f);
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.BLACK);
                lineDataSets.add(set);

                LineDataSet set1 = new LineDataSet(weekTwoRec, "Days "+(init_days+7)+"-"+(init_days+7+6));
                set1.setFillAlpha(50);
                set1.setColor(Color.CYAN);
                set1.setLineWidth(3f);
                set1.setValueTextSize(12f);
                set1.setValueTextColor(Color.BLACK);
                lineDataSets.add(set1);
                topicTextView.setText("Last 14 days Sitting performance");
            }
            if(cursorCount==21 && pos.equals("Last 7 days")){
                LineDataSet set = new LineDataSet(weekThreeRec, "Days "+(init_days+7*2)+"-"+(init_days+7*2+6));
                set.setFillAlpha(50);
                set.setColor(Color.GREEN);
                set.setLineWidth(3f);
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.BLACK);
                lineDataSets.add(set);
                topicTextView.setText("Last 7 days Sitting performance");
            }
            if(cursorCount==21 && pos.equals("Last 14 days")){
                LineDataSet set1 = new LineDataSet(weekTwoRec, "Days "+(init_days+7)+"-"+(init_days+7+6));
                set1.setFillAlpha(50);
                set1.setColor(Color.CYAN);
                set1.setLineWidth(3f);
                set1.setValueTextSize(12f);
                set1.setValueTextColor(Color.BLACK);
                lineDataSets.add(set1);

                LineDataSet set = new LineDataSet(weekThreeRec, "Days "+(init_days+7*2)+"-"+(init_days+7*2+6));
                set.setFillAlpha(50);
                set.setColor(Color.GREEN);
                set.setLineWidth(3f);
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.BLACK);
                lineDataSets.add(set);
                topicTextView.setText("Last 14 days Sitting performance");
            }
            if(cursorCount==21 && pos.equals("Last 21 days")){

                LineDataSet set2 = new LineDataSet(weekOneRec, "Days "+(init_days)+"-"+(init_days+6));
                set2.setFillAlpha(50);
                set2.setColor(Color.YELLOW);
                set2.setLineWidth(3f);
                set2.setValueTextSize(12f);
                set2.setValueTextColor(Color.BLACK);
                lineDataSets.add(set2);

                LineDataSet set1 = new LineDataSet(weekTwoRec, "Days "+(init_days+7)+"-"+(init_days+7+6));
                set1.setFillAlpha(50);
                set1.setColor(Color.CYAN);
                set1.setLineWidth(3f);
                set1.setValueTextSize(12f);
                set1.setValueTextColor(Color.BLACK);
                lineDataSets.add(set1);

                LineDataSet set = new LineDataSet(weekThreeRec, "Days "+(init_days+7*2)+"-"+(init_days+7*2+6));
                set.setFillAlpha(50);
                set.setColor(Color.GREEN);
                set.setLineWidth(3f);
                set.setValueTextSize(12f);
                set.setValueTextColor(Color.BLACK);
                lineDataSets.add(set);

                topicTextView.setText("Last 21 days Sitting performance");
            }

            LineData data = new LineData(lineDataSets);
            mpLineChart.setData(data);
            mpLineChart.invalidate();
        }

    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            return "Day " + (int) (value+1);
        }
    }

    //sth wrong need to do the checking
    public class DayAxisValueFormatter2 extends ValueFormatter{
        private final BarLineChartBase<?> chart;

        public DayAxisValueFormatter2(BarLineChartBase<?> chart) {
            this.chart = chart;
        }


        @Override
        public String getFormattedValue(float value) {
            return xAxisLabel.get((int) value);
        }
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(this, PieChartSittingReportActivity.class);
        startActivity(intent);
    }

    //the plotting data values (x,y)
    /*private ArrayList<Entry> dataValue1() {
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        //yValues.add(new Entry(i+1,clone.get(i).getAccuracy()));
        int i = 0;
        while (cursor.moveToNext()) {
            yValues.add(new Entry(i++, cursor.getFloat(9) ));
            Log.e("dataValue1", ""+cursor.getFloat(9));
        }
        return yValues;
    }*/

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}