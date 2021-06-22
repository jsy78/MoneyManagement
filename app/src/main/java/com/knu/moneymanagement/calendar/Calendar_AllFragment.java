package com.knu.moneymanagement.calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

public class Calendar_AllFragment extends Fragment implements Constant {

    private LinearLayout linearLayout;
    private TextView dayText, allCountText, inexpText, contentDetailText, notExistText, monIncomeText, monExpenText, monCashText, allCashText;
    private RecyclerView mRecyclerView = null;
    private RecyclerViewAdapter mAdapter = null;
    private final ArrayList<RecyclerViewItem> mItemList = new ArrayList<>();
    private final DecimalFormat myFormatter = new DecimalFormat("###,###");

    public Calendar_AllFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() != null) {
            View root = inflater.inflate(R.layout.fragment_calendar_all, container, false);

            linearLayout = root.findViewById(R.id.calendarAllView);
            dayText = root.findViewById(R.id.dayAllText);
            allCountText = root.findViewById(R.id.allCountText);
            inexpText = root.findViewById(R.id.inexpText);
            contentDetailText = root.findViewById(R.id.allContentDetailText);
            notExistText = root.findViewById(R.id.dayAllNotExist);
            monIncomeText = root.findViewById(R.id.monIncomeText);
            monExpenText = root.findViewById(R.id.monExpenText);
            monCashText = root.findViewById(R.id.monCashText);
            allCashText = root.findViewById(R.id.allCashText);
            mRecyclerView = root.findViewById(R.id.dayAllRecyclerView);

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

            ActivityResultLauncher<Intent> modifyActivityResult = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == UPDATE_SUCCESS) {
                            Toast.makeText(getActivity(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                        } else if (result.getResultCode() == DELETE_SUCCESS) {
                            Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        } else if (result.getResultCode() == MODIFY_CANCEL) {
                            Toast.makeText(getActivity(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

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

                modifyActivityResult.launch(it);
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
        createAllInfo(StaticVariable.year, StaticVariable.month, StaticVariable.day);
    }

    private void createAllInfo(int year, int month, int day) {
        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlSelect;
        Cursor cursor;

        Bitmap incomeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_income);
        Bitmap expenIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_expen);

        dayText.setText(getString(R.string.contents, year + "년 " + month + "월 " + day + "일"));

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " AND " + MoneyDBCtrct.COL_DAY + "=" + day + " " +
                "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH + ", " + MoneyDBCtrct.COL_DAY;
        cursor = db.rawQuery(sqlSelect, null);
        long income = 0L;
        while (cursor.moveToNext())
            income = cursor.getLong(0);

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'지출'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " AND " + MoneyDBCtrct.COL_DAY + "=" + day + " " +
                "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH + ", " + MoneyDBCtrct.COL_DAY;
        cursor = db.rawQuery(sqlSelect, null);
        long expen = 0L;
        while (cursor.moveToNext())
            expen = cursor.getLong(0);

        inexpText.setText(getString(R.string.contents, "수입 : " + myFormatter.format(income) + "원" + "  " + " 지출 : " + myFormatter.format(expen) + "원"));

        contentDetailText.setText(getString(R.string.contents, year + "년 " + month + "월 " + day + "일 전체 내역"));

        mItemList.clear();
        sqlSelect =
                MoneyDBCtrct.SQL_SELECT + " " +
                        "WHERE " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " AND " + MoneyDBCtrct.COL_DAY + "=" + day + " " +
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

            item.setCheck(false);
            item.setId(id);
            item.setCategory(category);
            item.setYear(yearValue);
            item.setMonth(monthValue);
            item.setDay(dayValue);
            item.setDate(yearValue, monthValue, dayValue);
            item.setDetail(detail);
            item.setMoney(money);
            item.setMoneyStr(money);

            if (category.equals("수입"))
                item.setIcon(incomeIcon);
            else
                item.setIcon(expenIcon);

            mItemList.add(item);
        }
        mAdapter.notifyDataSetChanged();

        allCountText.setText(getString(R.string.contents, "총 " + mAdapter.getItemCount() + "건"));

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

        monIncomeText.setText(getString(R.string.contents, year + "년 " + month + "월 " + "수입 : " + myFormatter.format(monAllIncome) + "원"));

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'지출'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + year + " AND " + MoneyDBCtrct.COL_MONTH + "=" + month + " " +
                "GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH;
        cursor = db.rawQuery(sqlSelect, null);
        long monAllExpen = 0L;
        while (cursor.moveToNext())
            monAllExpen = cursor.getLong(0);

        monExpenText.setText(getString(R.string.contents, year + "년 " + month + "월 " + "지출 : " + myFormatter.format(monAllExpen) + "원"));

        monCashText.setText(getString(R.string.contents, year + "년 " + month + "월 " + "잔액 : " + myFormatter.format(monAllIncome - monAllExpen) + "원"));

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " " +
                "GROUP BY " + MoneyDBCtrct.COL_CATEGORY;
        cursor = db.rawQuery(sqlSelect, null);
        long allIncome = 0L;
        while (cursor.moveToNext())
            allIncome = cursor.getLong(0);

        sqlSelect =
                "SELECT " + "SUM(" + MoneyDBCtrct.COL_MONEY + ")" + " FROM " + MoneyDBCtrct.TBL_MONEY + " " +
                "WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'지출'" + " " +
                "GROUP BY " + MoneyDBCtrct.COL_CATEGORY;
        cursor = db.rawQuery(sqlSelect, null);
        long allExpen = 0L;
        while (cursor.moveToNext())
            allExpen = cursor.getLong(0);

        allCashText.setText(getString(R.string.contents, "전체 누적 잔액 : " + myFormatter.format(allIncome - allExpen) + "원"));

        cursor.close();
        db.close();
        dbHelper.close();

        if (monAllIncome == 0L && monAllExpen == 0L)
            Toast.makeText(getActivity(), "수입 및 지출 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
    }
}
