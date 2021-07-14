package com.knu.moneymanagement.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.knu.moneymanagement.ItemDiffUtil;
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

public class List_AllFragment extends Fragment implements Constant {

    private ConstraintLayout constraintLayout;
    private boolean isSingleSelectionMode = true;
    private int selectedCount = 0;
    private TextView notExistText;
    private RecyclerView mRecyclerView = null;
    private RecyclerViewAdapter mAdapter = null;

    public List_AllFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d("MyTag", "List_AllFragment onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("MyTag", "List_AllFragment onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MyTag", "List_AllFragment onCreateView");
        // Inflate the layout for this fragment
        if(getActivity() != null) {
            View root = inflater.inflate(R.layout.fragment_list_all, container, false);

            constraintLayout = root.findViewById(R.id.listAllView);
            notExistText = root.findViewById(R.id.allRecyclerNotExist);
            mRecyclerView = root.findViewById(R.id.allRecyclerView);
            mAdapter = new RecyclerViewAdapter(new ItemDiffUtil());
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

                    int i = mAdapter.getCurrentList().get(position).getId();
                    String category = mAdapter.getCurrentList().get(position).getCategory();
                    int year = mAdapter.getCurrentList().get(position).getYear();
                    int month = mAdapter.getCurrentList().get(position).getMonth();
                    int day = mAdapter.getCurrentList().get(position).getDay();
                    String detail = mAdapter.getCurrentList().get(position).getDetail();
                    int money = mAdapter.getCurrentList().get(position).getMoney();

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
                    ArrayList<RecyclerViewItem> newList = new ArrayList<RecyclerViewItem>() {
                        {
                            for (RecyclerViewItem item : mAdapter.getCurrentList())
                                add(item.clone());
                        }
                    };
                    newList.get(position).setCheck(!newList.get(position).isChecked());
                    if(newList.get(position).isChecked())
                        selectedCount += 1;
                    else
                        selectedCount -= 1;
                    //mAdapter.notifyItemChanged(position);
                    mAdapter.submitList(newList);
                    if(getParentFragment() != null)
                        ((ListFragment)getParentFragment()).mToolbar.setTitle("선택된 항목 (" + selectedCount + " / " + mAdapter.getItemCount() + ")");
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
    public void onStart() {
        Log.d("MyTag", "List_AllFragment onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("MyTag", "List_AllFragment onResume");
        super.onResume();
        constraintLayout.setVisibility(View.VISIBLE);
        createAllList(StaticVariable.year, StaticVariable.month, StaticVariable.sort);

        if(getParentFragment() != null) {
            ((ListFragment)getParentFragment()).mToolbar.setTitle("선택된 항목 (" + selectedCount + " / " + mAdapter.getItemCount() + ")");

            ((ListFragment)getParentFragment()).delete.setOnClickListener(v -> {
                if (selectedCount <= 0) {
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
                    isSingleSelectionMode = true;
                    selectedCount = 0;
                    ((ListFragment) getParentFragment()).mToolbar.setTitle("선택된 항목 (" + selectedCount + " / " + mAdapter.getItemCount() + ")");
                    ((ListFragment) getParentFragment()).mViewPager2.setUserInputEnabled(true);
                    ((ListFragment) getParentFragment()).singleSelectionMode();
                    ArrayList<RecyclerViewItem> newList = new ArrayList<RecyclerViewItem>() {
                        {
                            for (RecyclerViewItem item : mAdapter.getCurrentList())
                                add(item.clone());
                        }
                    };
                    for (RecyclerViewItem item : newList)
                        if (item.isChecked())
                            item.setCheck(false);
                    mAdapter.submitList(newList);
                    Toast.makeText(getActivity(), "선택 모드 해제", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onPause() {
        Log.d("MyTag", "List_AllFragment onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("MyTag", "List_AllFragment onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("MyTag", "List_AllFragment onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("MyTag", "List_AllFragment onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("MyTag", "List_AllFragment onDetach");
        super.onDetach();
    }

    public void createAllList(int yearValue, int monthValue, int sort) {
        Log.d("MyTag", "List_AllFragment createAllList");
        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlSelect;
        Cursor cursor;

        Bitmap incomeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_income);
        Bitmap expenIcon  = BitmapFactory.decodeResource(getResources(), R.drawable.ic_expen);

        ArrayList<RecyclerViewItem> mItemList = new ArrayList<>();
        switch (sort) {
            case DESC_DAY:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_DAY + " DESC";
                break;
            case ASC_MONEY:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_MONEY + " ASC";
                break;
            case DESC_MONEY:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_MONEY + " DESC";
                break;
            case ASC_DAY:
            default:
                sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue + " ORDER BY " + MoneyDBCtrct.COL_DAY + " ASC";
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

            item.setCheck(false);
            item.setId(id);
            item.setCategory(category);
            item.setYear(year);
            item.setMonth(month);
            item.setDay(day);
            item.setDate(year, month, day);
            item.setDetail(detail);
            item.setMoney(money);
            item.setMoneyStr(money);

            if(category.equals("수입"))
                item.setIcon(incomeIcon);
            else
                item.setIcon(expenIcon);

            mItemList.add(item);
        }
        //mAdapter.notifyItemInserted(mItemList.size()-1);
        mAdapter.submitList(mItemList);

        cursor.close();
        db.close();
        dbHelper.close();

        if(mAdapter.getItemCount() == 0) {
            Toast.makeText(getActivity(), "수입 및 지출 내역이 없습니다.", Toast.LENGTH_SHORT).show();
            mRecyclerView.setVisibility(View.GONE);
            notExistText.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            notExistText.setVisibility(View.GONE);
        }
    }
}
