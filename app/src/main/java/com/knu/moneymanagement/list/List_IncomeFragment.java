package com.knu.moneymanagement.list;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.knu.moneymanagement.ListFragment;
import com.knu.moneymanagement.RecyclerViewAdapter;
import com.knu.moneymanagement.RecyclerViewItem;
import com.knu.moneymanagement.database.Constant;
import com.knu.moneymanagement.database.StaticVariable;
import com.knu.moneymanagement.ModifyActivity;
import com.knu.moneymanagement.database.MoneyDBCtrct;
import com.knu.moneymanagement.database.MoneyDBHelper;
import com.knu.moneymanagement.R;

import java.util.ArrayList;

public class List_IncomeFragment extends Fragment implements Constant {

    private ConstraintLayout constraintLayout;
    private boolean isSingleSelectionMode = true;
    private TextView notExistText;
    private RecyclerView mRecyclerView = null;
    private RecyclerViewAdapter mAdapter = null;
    private final ArrayList<RecyclerViewItem> mItemList = new ArrayList<>();

    public List_IncomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getActivity() != null) {
            View root = inflater.inflate(R.layout.fragment_list_income, container, false);

            constraintLayout = root.findViewById(R.id.listIncomeView);
            notExistText = root.findViewById(R.id.incomeRecyclerNotExist);
            mRecyclerView = root.findViewById(R.id.incomeRecyclerView);
            mAdapter = new RecyclerViewAdapter(mItemList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

//            mAdapter.setOnItemTouchListener(new RecyclerViewAdapter.OnItemTouchListener() {
//                @Override
//                public void onItemTouch(View view, MotionEvent event, int position) {
//                    if (isSingleSelectionMode) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            view.setBackgroundColor(0xCFDCDCDC);
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            view.setBackgroundColor(0x00000000);
//                        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                            view.setBackgroundColor(0xCFDCDCDC);
//                        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                            view.setBackgroundColor(0x00000000);
//                        } else {
//                            view.setBackgroundColor(0x00000000);
//                        }
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
                if (isSingleSelectionMode) {
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
                }
                else {
                    mItemList.get(position).setCheck(!mItemList.get(position).isChecked());
                    mAdapter.notifyItemChanged(position);
                    if(getParentFragment() != null)
                        ((ListFragment)getParentFragment()).mToolbar.setTitle("선택된 항목 (" + mAdapter.getSelectedItemCount() + " / " + mAdapter.getItemCount() + ")");
                }
            });

            mAdapter.setOnItemLongClickListener((view, position) -> {
                if(getParentFragment() != null && isSingleSelectionMode) {
                    isSingleSelectionMode = false;
                    Toast.makeText(getActivity(), "선택 모드", Toast.LENGTH_SHORT).show();
                    ((ListFragment) getParentFragment()).mViewPager2.setUserInputEnabled(false);
                    ((ListFragment) getParentFragment()).multiSelectionMode();
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

        createIncomeList(StaticVariable.year, StaticVariable.month, StaticVariable.sort);

        if(getParentFragment() != null) {
            ((ListFragment)getParentFragment()).mToolbar.setTitle("선택된 항목 (" + 0 + " / " + mAdapter.getItemCount() + ")");

            ((ListFragment)getParentFragment()).delete.setOnClickListener(v -> {
                if (mAdapter.getSelectedItemCount() <= 0) {
                    Toast.makeText(getActivity(), "한 개 이상의 항목을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    final ArrayList<RecyclerViewItem> selectedItem = mAdapter.getSelectedItem();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Loading_Dialog_Theme);

                    builder.setMessage("일괄 삭제를 진행하시겠습니까?");
                    builder.setPositiveButton("OK", (dialog, id) -> {
                        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        for(int i = 0; i < selectedItem.size(); i++) {
                            String sqlDelete = MoneyDBCtrct.SQL_DELETE + " WHERE " + MoneyDBCtrct.COL_ID + "=" + selectedItem.get(i).getId();
                            db.execSQL(sqlDelete);
                        }

                        db.close();
                        dbHelper.close();

                        if (getParentFragment() != null)
                            getParentFragment().onResume();

                        Toast.makeText(getActivity(), "일괄 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    });

                    builder.setNegativeButton("Cancel", (dialog, id) -> Toast.makeText(getActivity(), "일괄 삭제가 취소되었습니다.", Toast.LENGTH_SHORT).show());

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            ((ListFragment) getParentFragment()).cancel.setOnClickListener(v -> {
                if(getParentFragment() != null) {
                    Toast.makeText(getActivity(), "선택 모드 해제", Toast.LENGTH_SHORT).show();
                    isSingleSelectionMode = true;
                    mAdapter.clearSelectedItem();
                    ((ListFragment)getParentFragment()).mToolbar.setTitle("선택된 항목 (" + 0 + " / " + mAdapter.getItemCount() + ")");
                    ((ListFragment)getParentFragment()).mViewPager2.setUserInputEnabled(true);
                    ((ListFragment)getParentFragment()).singleSelectionMode();
                }
            });
        }
    }

    private void createIncomeList(int yearValue, int monthValue, int sort) {
        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlSelect;
        Cursor cursor;

        Bitmap incomeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_income);

        mItemList.clear();
        switch (sort) {
            case DESC_DAY:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_DAY + " DESC";
                break;
            case ASC_MONEY:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_MONEY + " ASC";
                break;
            case DESC_MONEY:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_MONEY + " DESC";
                break;
            case ASC_DAY:
            default:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_DAY + " ASC";
        }
        cursor = db.rawQuery(sqlSelect, null);
        while (cursor.moveToNext()){
            RecyclerViewItem item = new RecyclerViewItem();

            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            int year = cursor.getInt(2);
            int month = cursor.getInt(3);
            int day = cursor.getInt(4);
            String detail = cursor.getString(5);
            int money = cursor.getInt(6);

            item.setId(id);
            item.setCategory(category);
            item.setYear(year);
            item.setMonth(month);
            item.setDay(day);
            item.setDate(year, month, day);
            item.setDetail(detail);
            item.setMoney(money);
            item.setMoneyStr(money);
            item.setIcon(incomeIcon);

            mItemList.add(item);
        }
        mAdapter.notifyDataSetChanged();

        cursor.close();
        db.close();
        dbHelper.close();

        if(mAdapter.getItemCount() == 0) {
            Toast.makeText(getActivity(), "수입 내역이 없습니다.", Toast.LENGTH_SHORT).show();
            mRecyclerView.setVisibility(View.GONE);
            notExistText.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            notExistText.setVisibility(View.GONE);
        }
    }
}
