package com.knu.moneymanagement.graph;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.knu.moneymanagement.database.Constant;
import com.knu.moneymanagement.LoadingDialog;
import com.knu.moneymanagement.database.StaticVariable;
import com.knu.moneymanagement.database.MoneyDBCtrct;
import com.knu.moneymanagement.database.MoneyDBHelper;
import com.knu.moneymanagement.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Graph_MonthFragment extends Fragment implements Constant {

    private LoadingDialog mLoadingDialog;

    private static class ChartTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Graph_MonthFragment> FragmentReference;

        private ChartTask(Graph_MonthFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            Graph_MonthFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.getActivity() == null || fragment.getActivity().isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("데이터 갱신 중...");
            fragment.mLoadingDialog.show();
            fragment.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final Graph_MonthFragment fragment = FragmentReference.get();

            if(fragment != null && fragment.getActivity() != null && !fragment.getActivity().isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fragment.getActivity().runOnUiThread(() -> fragment.createChart(StaticVariable.year, StaticVariable.month));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Graph_MonthFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.getActivity() == null || fragment.getActivity().isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            fragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(result);
        }
    }

    private ConstraintLayout constraintLayout;
    private TextView monthText;
    private PieChart pieChart;

    public Graph_MonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getActivity() != null) {
            View root = inflater.inflate(R.layout.fragment_graph_month, container, false);

            mLoadingDialog = new LoadingDialog(getActivity(), R.style.Loading_Dialog_Theme);

            constraintLayout = root.findViewById(R.id.graphMonthView);
            monthText = root.findViewById(R.id.monthGraphText);
            Button prev = root.findViewById(R.id.btn_prev_graph_month);
            Button next = root.findViewById(R.id.btn_next_graph_month);
            pieChart = root.findViewById(R.id.pieChart);

            if (StaticVariable.month < 10)
                monthText.setText(getString(R.string.contents, "0"+StaticVariable.month));
            else
                monthText.setText(getString(R.string.contents, ""+StaticVariable.month));

//           prev.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                        prev.setBackgroundColor(0xFFC0C0C0);
//                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                        prev.setBackgroundColor(0x00FF0000);
//                    }
//                    return false;
//                }
//            });
//
//            next.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                        next.setBackgroundColor(0xFFC0C0C0);
//                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                        next.setBackgroundColor(0x00FF0000);
//                    }
//                    return false;
//                }
//            });
            prev.setOnClickListener(view -> {
                StaticVariable.day = 1;
                switch (StaticVariable.month) {
                    case 1:
                        StaticVariable.month = 12;
                        monthText.setText(getString(R.string.contents, ""+StaticVariable.month));
                        new ChartTask(Graph_MonthFragment.this).execute();
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        StaticVariable.month -= 1;
                        monthText.setText(getString(R.string.contents, "0"+StaticVariable.month));
                        new ChartTask(Graph_MonthFragment.this).execute();
                        break;
                    case 11:
                    case 12:
                        StaticVariable.month -= 1;
                        monthText.setText(getString(R.string.contents, ""+StaticVariable.month));
                        new ChartTask(Graph_MonthFragment.this).execute();
                        break;
                    default:
                        Toast.makeText(getActivity(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            });

            next.setOnClickListener(view -> {
                StaticVariable.day = 1;
                switch (StaticVariable.month) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        StaticVariable.month += 1;
                        monthText.setText(getString(R.string.contents, "0"+StaticVariable.month));
                        new ChartTask(Graph_MonthFragment.this).execute();
                        break;
                    case 9:
                    case 10:
                    case 11:
                        StaticVariable.month += 1;
                        monthText.setText(getString(R.string.contents, ""+StaticVariable.month));
                        new ChartTask(Graph_MonthFragment.this).execute();
                        break;
                    case 12:
                        StaticVariable.month = 1;
                        monthText.setText(getString(R.string.contents, "0"+StaticVariable.month));
                        new ChartTask(Graph_MonthFragment.this).execute();
                        break;
                    default:
                        Toast.makeText(getActivity(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            });

            constraintLayout.setVisibility(View.INVISIBLE);

            return root;
        }
        else
            return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        constraintLayout.setVisibility(View.VISIBLE);
        createChart(StaticVariable.year, StaticVariable.month);
    }

    public void createChart(int year, int month){
        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlSelect;
        Cursor cursor;
        long monIncome = 0L;
        long monExpen = 0L;

        ArrayList<PieEntry> val = new ArrayList<>(); //그래프로 그릴 데이터의 값을 저장한다.

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " " +
                "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH;
        cursor = db.rawQuery(sqlSelect, null);
        while (cursor.moveToNext())
            monIncome = cursor.getLong(0);

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'지출'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " " +
                "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH;
        cursor = db.rawQuery(sqlSelect, null);
        while (cursor.moveToNext())
            monExpen = cursor.getLong(0);

        cursor.close();
        db.close();
        dbHelper.close();

        pieChart.setUsePercentValues(true);//그래프에 표시되는 수치를 true로 하면 percent, false로 하면 rawData
        pieChart.setDrawHoleEnabled(true); //true로 하면 중앙의 구멍이 사라짐
        if(StaticVariable.month < 10)
            pieChart.setCenterText(year+"년\n" + "0" + month + "월\n" + "수입/지출");
        else
            pieChart.setCenterText(year+"년\n" + month + "월\n" + "수입/지출");
        pieChart.setCenterTextSize(15f);

        val.add(new PieEntry(monIncome,"수입"));
        val.add(new PieEntry(monExpen,"지출"));

        Description description = new Description();
        description.setText("수입/지출 비율(%)   "); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description); //원그래프에 description을 붙인다.

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(val,"");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(15f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK); //원그래프 안에 수치를 표시하는 text의 색

        pieChart.setData(data);
        pieChart.invalidate();

        if(monIncome == 0L && monExpen == 0L)
            Toast.makeText(getActivity(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
    }
}
