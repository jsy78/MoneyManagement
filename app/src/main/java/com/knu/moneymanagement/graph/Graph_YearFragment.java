package com.knu.moneymanagement.graph;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.knu.moneymanagement.database.StaticVariable;
import com.knu.moneymanagement.database.MoneyDBCtrct;
import com.knu.moneymanagement.database.MoneyDBHelper;
import com.knu.moneymanagement.R;

import java.util.ArrayList;

public class Graph_YearFragment extends Fragment {

    private LineChart lineChart;

    public Graph_YearFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_graph_year, container, false);

        lineChart = root.findViewById(R.id.lineChart);

        lineChart.setVisibility(View.INVISIBLE);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        lineChart.setVisibility(View.VISIBLE);
        createLine(StaticVariable.year);
    }

    public void createLine(int year) {
        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlSelect;
        Cursor cursor = null;
        long[] monthIncome = new long[12];
        long[] monthExpen = new long[12];

        String[] labels = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"}; // x축 라벨
        ArrayList<Entry> income = new ArrayList<>(); //차트 데이터 셋에 담겨질 데이터
        ArrayList<Entry> expen = new ArrayList<>(); //차트 데이터 셋에 담겨질 데이터

        for (int i = 0; i < 12; i++) {
            sqlSelect =
                    "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                    "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + (i + 1) + " " +
                    "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH;
            cursor = db.rawQuery(sqlSelect, null);
            while (cursor.moveToNext())
                monthIncome[i] = cursor.getLong(0);

            income.add(new Entry(i, monthIncome[i]));
        }

        for (int i = 0; i < 12; i++) {
            sqlSelect =
                    "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                    "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'지출'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + (i + 1) + " " +
                    "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH;
            cursor = db.rawQuery(sqlSelect, null);
            while (cursor.moveToNext())
                monthExpen[i] = cursor.getLong(0);

            expen.add(new Entry(i, monthExpen[i]));
        }

        cursor.close();
        db.close();
        dbHelper.close();

        LineDataSet lineDataSetIncome = new LineDataSet(income, "수입"); //LineDataSet 선언
        lineDataSetIncome.setColor(Color.BLUE); //LineChart에서 Line Color 설정
        lineDataSetIncome.setCircleColor(Color.BLUE); // LineChart에서 Line Circle Color 설정
        lineDataSetIncome.setDrawValues(false);

        LineDataSet lineDataSetExpen = new LineDataSet(expen, "지출"); //LineDataSet 선언
        lineDataSetExpen.setColor(Color.RED); //LineChart에서 Line Color 설정
        lineDataSetExpen.setCircleColor(Color.RED); // LineChart에서 Line Circle Color 설정
        lineDataSetExpen.setDrawValues(false);

        LineData lineData = new LineData(); //LineDataSet을 담는 그릇 여러개의 라인 데이터가 들어갈 수 있습니다.
        lineData.addDataSet(lineDataSetIncome);
        lineData.addDataSet(lineDataSetExpen);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setValueFormatter(new MyXAxisValueFormatter(labels)); // x 축의 데이터를 제 가공함. new MyXAxisValueFormatter는 Custom한 소스
        xAxis.setLabelCount(12, false); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 force가 true 이면 반드시 보여줌
        xAxis.setTextColor(Color.BLACK); // X축 텍스트 컬러 설정
        xAxis.setGridColor(Color.BLACK); // X축 줄의 컬러 설정
        xAxis.setTextSize(12f);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1.0f); // x-axis scale will be 0,1,2,3,4....
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(11f);

        YAxis yAxisLeft = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setTextColor(Color.BLACK); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(Color.BLACK); // Y축 줄의 컬러 설정
        yAxisLeft.setTextSize(10f);
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(1.0f); // y-axis scale will be 0,1,2,3,4....
        yAxisLeft.setAxisMinimum(0f); // y축 최솟값 0으로

        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        //오른쪽 y축의 활성화를 제거함

        lineChart.setVisibleXRangeMinimum(11f); //라인차트에서 최대로 보여질 X축의 범위 설정
        lineChart.setDescription(null); //차트에서 Description 설정

        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        legend.setTextColor(Color.BLACK); // 레전드 컬러 설정

        MyMarkerView marker = new MyMarkerView(getActivity(), R.layout.markerview_text);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);
        lineChart.setData(lineData);
        lineChart.invalidate();

        long monIncomeSum = 0L;
        for (long l : monthIncome)
            monIncomeSum += l;

        long monExpenSum = 0L;
        for (long l : monthExpen)
            monExpenSum += l;

        if (monIncomeSum == 0L && monExpenSum == 0L)
            Toast.makeText(getActivity(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
    }

}
