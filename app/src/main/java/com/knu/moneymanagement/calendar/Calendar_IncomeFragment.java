package com.knu.moneymanagement.calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knu.moneymanagement.ModifyActivity;
import com.knu.moneymanagement.RecyclerViewAdapter;
import com.knu.moneymanagement.RecyclerViewItem;
import com.knu.moneymanagement.database.Constant;
import com.knu.moneymanagement.database.StaticVariable;
import com.knu.moneymanagement.database.MoneyDBCtrct;
import com.knu.moneymanagement.database.MoneyDBHelper;
import com.knu.moneymanagement.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Calendar_IncomeFragment extends Fragment implements Constant {

    private LinearLayout linearLayout;
    private TextView dayText, incomeCountText, inText, contentDetailText, notExistText, monBiggestIncomeText, monAvgIncomeText, monAllIncomeText, allIncomeText;
    private RecyclerView mRecyclerView = null;
    private RecyclerViewAdapter mAdapter = null;
    private final ArrayList<RecyclerViewItem> mItemList = new ArrayList<>();
    private final DecimalFormat myFormatter = new DecimalFormat("###,###");

    public Calendar_IncomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getActivity() != null) {
            View root = inflater.inflate(R.layout.fragment_calendar_income, container, false);

            linearLayout = root.findViewById(R.id.calendarIncomeView);
            dayText = root.findViewById(R.id.dayIncomeText);
            incomeCountText = root.findViewById(R.id.incomeCountText);
            inText = root.findViewById(R.id.inText);
            contentDetailText = root.findViewById(R.id.incomeContentDetailText);
            notExistText = root.findViewById(R.id.dayIncomeNotExist);
            monBiggestIncomeText = root.findViewById(R.id.monBiggestIncomeText);
            monAvgIncomeText = root.findViewById(R.id.monAvgIncomeText);
            monAllIncomeText = root.findViewById(R.id.monAllIncomeText);
            allIncomeText = root.findViewById(R.id.allIncomeText);
            mRecyclerView = root.findViewById(R.id.dayIncomeRecyclerView);

            mAdapter = new RecyclerViewAdapter(mItemList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

//            mAdapter.setOnItemTouchListener(new RecyclerViewAdapter.OnItemTouchListener() {
//                @Override
//                public void onItemTouch(View view, MotionEvent event, int position) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        view.setBackgroundColor(0xCFDCDCDC);
//                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                        view.setBackgroundColor(0x00000000);
//                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                        view.setBackgroundColor(0xCFDCDCDC);
//                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                        view.setBackgroundColor(0x00000000);
//                    } else {
//                        view.setBackgroundColor(0x00000000);
//                    }
//                }
//            });

            mAdapter.setOnItemClickListener((view, position) -> {
                Intent it = new Intent(getActivity(), ModifyActivity.class);
                Bundle bundle = new Bundle();

                int i = mItemList.get(position).getId();
                String category = mItemList.get(position).getCategory();
                int year = mItemList.get(position).getYear();
                int month = mItemList.get(position).getMonth();
                int day = mItemList.get(position).getDay();
                String detail = mItemList.get(position).getDetail();
                int money = mItemList.get(position).getMoney();

                bundle.putInt("id", i);
                bundle.putString("category", category);
                bundle.putInt("year", year);
                bundle.putInt("month", month);
                bundle.putInt("day", day);
                bundle.putString("detail", detail);
                bundle.putInt("money", money);

                it.putExtras(bundle);

                startActivityForResult(it, ACT_MODIFY);
            });

            linearLayout.setVisibility(View.INVISIBLE);

            return root;
        }
        else
            return null;

    }

    @Override
    public void onResume() {
        super.onResume();
        linearLayout.setVisibility(View.VISIBLE);
        createIncomeInfo(StaticVariable.year, StaticVariable.month, StaticVariable.day);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACT_MODIFY) {
            if (resultCode == UPDATE_SUCCESS) {
                Toast.makeText(getActivity(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == DELETE_SUCCESS) {
                Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == MODIFY_CANCEL) {
                Toast.makeText(getActivity(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createIncomeInfo(int year, int month, int day) {
        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlSelect;
        Cursor cursor;

        Bitmap incomeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_income);

        dayText.setText(getString(R.string.contents, year + "년 " + month + "월 " + day + "일"));

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                        "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " AND " + MoneyDBCtrct.COL_DAY + "=" + day + " " +
                        "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH + ", " + MoneyDBCtrct.COL_DAY;
        cursor = db.rawQuery(sqlSelect, null);
        long income = 0L;
        while (cursor.moveToNext())
            income = cursor.getLong(0);

        inText.setText(getString(R.string.contents, "수입 : " + myFormatter.format(income) + "원"));

        contentDetailText.setText(getString(R.string.contents, year + "년 " + month + "월 " + day + "일 수입 내역"));

        mItemList.clear();
        sqlSelect =
                MoneyDBCtrct.SQL_SELECT + " " +
                        "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " AND " + MoneyDBCtrct.COL_DAY + "=" + day + " " +
                        "ORDER BY " + MoneyDBCtrct.COL_DAY + " ASC";
        cursor = db.rawQuery(sqlSelect, null);
        while (cursor.moveToNext()) {
            RecyclerViewItem item = new RecyclerViewItem();

            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            int yearValue = cursor.getInt(2);
            int monthValue = cursor.getInt(3);
            int dayValue = cursor.getInt(4);
            String detail = cursor.getString(5);
            int money = cursor.getInt(6);

            item.setId(id);
            item.setCategory(category);
            item.setYear(yearValue);
            item.setMonth(monthValue);
            item.setDay(dayValue);
            item.setDate(yearValue, monthValue, dayValue);
            item.setDetail(detail);
            item.setMoney(money);
            item.setMoneyStr(money);
            item.setIcon(incomeIcon);

            mItemList.add(item);
        }
        mAdapter.notifyDataSetChanged();

        incomeCountText.setText(getString(R.string.contents, "총 " + mAdapter.getItemCount() + "건"));

        if (mAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            notExistText.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            notExistText.setVisibility(View.GONE);
        }

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                        "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " " +
                        "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH;
        cursor = db.rawQuery(sqlSelect, null);
        long monAllIncome = 0L;
        while (cursor.moveToNext())
            monAllIncome = cursor.getLong(0);

        monAllIncomeText.setText(getString(R.string.contents, year + "년 " + month + "월 " + "전체 수입 : " + myFormatter.format(monAllIncome) + "원"));

        sqlSelect =
                "SELECT " + "MAX(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                        "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " " +
                        "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH;
        cursor = db.rawQuery(sqlSelect, null);
        long monBiggestIncome = 0L;
        while (cursor.moveToNext())
            monBiggestIncome = cursor.getLong(0);

        monBiggestIncomeText.setText(getString(R.string.contents, year + "년 " + month + "월 " + "최고 수입 : " + myFormatter.format(monBiggestIncome) + "원"));

        sqlSelect =
                "SELECT " + "COUNT(" + MoneyDBCtrct.COL_CATEGORY + ")" + " " +
                        "FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                        "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month;
        cursor = db.rawQuery(sqlSelect, null);
        int count = 0;
        while (cursor.moveToNext())
            count = cursor.getInt(0);

        long monAvgIncome;
        if (count == 0)
            monAvgIncome = monAllIncome;
        else
            monAvgIncome = monAllIncome / count;

        monAvgIncomeText.setText(getString(R.string.contents, year + "년 " + month + "월 " + "평균 수입 : " + myFormatter.format(monAvgIncome) + "원"));

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                        "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " " +
                        "GROUP BY " + MoneyDBCtrct.COL_CATEGORY;
        cursor = db.rawQuery(sqlSelect, null);
        long allIncome = 0L;
        while (cursor.moveToNext())
            allIncome = cursor.getLong(0);

        allIncomeText.setText(getString(R.string.contents, "전체 누적 수입 : " + myFormatter.format(allIncome) + "원"));

        cursor.close();
        db.close();
        dbHelper.close();

        if (monAllIncome == 0L)
            Toast.makeText(getActivity(), "수입 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
    }

}
