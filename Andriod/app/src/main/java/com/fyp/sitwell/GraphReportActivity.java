package com.fyp.sitwell;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
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

import java.util.ArrayList;

public class GraphReportActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    private static final String TAG = "GraphReportActivity";
    private LineChart lineChart;
    private DBHandler dbHandler;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linechart);
        dbHandler = new DBHandler(this);
        cursor = new DBHandler(this).getAllData();
        LineChartSetup();
    }

    private void LineChartSetup() {
        lineChart = (LineChart) findViewById(R.id.linechart);
        lineChart.setOnChartGestureListener(GraphReportActivity.this);
        lineChart.setOnChartValueSelectedListener(GraphReportActivity.this);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
       /* LimitLine upper_limit = new LimitLine(65f,"Danger");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f,10f,0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(35f,"Too low");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f,10f,0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);*/

        // yAxis.addLimitLine(upper_limit);
        // yAxis.addLimitLine(lower_limit);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setLabelCount(10);
        yAxis.removeAllLimitLines();
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(0f);
        yAxis.enableGridDashedLine(10f, 10f, 0);
        yAxis.setDrawLimitLinesBehindData(true);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        YAxis yAxis1 = lineChart.getAxisRight();
        yAxis1.setEnabled(false);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(lineChart);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setExtraOffsets(10, 20, 10, 20);

        LineDataSet set1 = new LineDataSet(dataValue1(), "Data Set 1");
        set1.setFillAlpha(50);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.GREEN);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    //this the plotting data values (x,y)
    private ArrayList<Entry> dataValue1() {

        ArrayList<Entry> yValues = new ArrayList<Entry>();
        //yValues.add(new Entry(i+1,clone.get(i).getAccuracy()));
        int i = 0;
        while (cursor.moveToNext()) {
            yValues.add(new Entry(i++, cursor.getFloat(9) ));
        }

        return yValues;
    }


    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            return "Day " + (int) value;
        }
    }

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