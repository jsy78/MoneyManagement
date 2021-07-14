package com.knu.moneymanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;

import com.knu.moneymanagement.database.Constant;
import com.knu.moneymanagement.database.StaticVariable;
import com.knu.moneymanagement.list.List_AllFragment;
import com.knu.moneymanagement.list.List_ExpenFragment;
import com.knu.moneymanagement.list.List_IncomeFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ListFragment extends Fragment implements Constant {

    private Activity activity;
    private ViewPagerFragmentAdapter mFragmentAdapter;
    private LoadingDialog mLoadingDialog;

    private TextView yearText, monthText;
    private Button prev, next, add, order;
    private TabLayout mTabLayout;

    public ViewPager2 mViewPager2;
    public Toolbar mToolbar;
    public Button delete, cancel;

    /*private static class PageChangeTask extends AsyncTask<Integer, Void, Void> {

        private final WeakReference<ListFragment> FragmentReference;

        private PageChangeTask(ListFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ListFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("페이지 전환 중...");
            fragment.mLoadingDialog.show();
            fragment.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Integer... voids) {
            final ListFragment fragment = FragmentReference.get();

            if(fragment != null && fragment.activity != null && !fragment.activity.isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //  fragment.mViewPager2.setCurrentItem(voids[0]);
                fragment.activity.runOnUiThread(fragment::fragmentRefresh);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            fragment.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(result);
        }

    }*/

    private static class RefreshTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<ListFragment> FragmentReference;

        private RefreshTask(ListFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            ListFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("데이터 갱신 중...");
            fragment.mLoadingDialog.show();
            fragment.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final ListFragment fragment = FragmentReference.get();

            if(fragment != null && fragment.activity != null && !fragment.activity.isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fragment.activity.runOnUiThread(fragment::fragmentRefresh);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            fragment.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(result);
        }

    }

    public ListFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d("MyTag", "ListFragment onAttach");
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        else
            activity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("MyTag", "ListFragment onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MyTag", "ListFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        mLoadingDialog = new LoadingDialog(activity, R.style.Loading_Dialog_Theme);

        mTabLayout = root.findViewById(R.id.tabLayout2);

        mToolbar = root.findViewById(R.id.selectToolbar);

        yearText = root.findViewById(R.id.yearListAllText);
        monthText = root.findViewById(R.id.monListAllText);
        prev = root.findViewById(R.id.btn_prev_list_all);
        next = root.findViewById(R.id.btn_next_list_all);
        add = root.findViewById(R.id.btn_list_add);
        order = root.findViewById(R.id.btn_list_order);
        delete = root.findViewById(R.id.btn_list_delete);
        cancel = root.findViewById(R.id.btn_list_cancel);

        mViewPager2 = root.findViewById(R.id.viewPager2);
        mViewPager2.setOffscreenPageLimit(2);

        mFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), getLifecycle(), new FragmentDiffUtil());

        mViewPager2.setAdapter(mFragmentAdapter);
        /*mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mViewPager2.setCurrentItem(position);
                //new PageChangeTask(ListFragment.this).execute();
            }
        });*/
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            switch (position) {
                case 0 :
                    tab.setText("전체 보기");
                    break;
                case 1 :
                    tab.setText("수입 보기");
                    break;
                case 2 :
                    tab.setText("지출 보기");
                    break;
                default:
                    Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }).attach();

//        prev.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    prev.setBackgroundColor(0xFFC0C0C0);
//                }
//                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    prev.setBackgroundColor(0x00FF0000);
//                }
//                return false;
//            }
//        });
//
//        next.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    next.setBackgroundColor(0xFFC0C0C0);
//                }
//                else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    next.setBackgroundColor(0x00FF0000);
//                }
//                return false;
//            }
//        });

        prev.setOnClickListener(view -> {
            StaticVariable.day = 1;
            switch (StaticVariable.month){
                case 1:
                    StaticVariable.year -= 1;
                    StaticVariable.month = 12;
                    yearText.setText(getString(R.string.contents,""+StaticVariable.year));
                    monthText.setText(getString(R.string.contents,""+StaticVariable.month));
                    new RefreshTask(ListFragment.this).execute();
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
                    monthText.setText(getString(R.string.contents,"0"+StaticVariable.month));
                    new RefreshTask(ListFragment.this).execute();
                    break;
                case 11:
                case 12:
                    StaticVariable.month -= 1;
                    monthText.setText(getString(R.string.contents,""+StaticVariable.month));
                    new RefreshTask(ListFragment.this).execute();
                    break;
                default:
                    Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        next.setOnClickListener(view -> {
            StaticVariable.day = 1;
            switch (StaticVariable.month){
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    StaticVariable.month += 1;
                    monthText.setText(getString(R.string.contents,"0"+StaticVariable.month));
                    new RefreshTask(ListFragment.this).execute();
                    break;
                case 9:
                case 10:
                case 11:
                    StaticVariable.month += 1;
                    monthText.setText(getString(R.string.contents,""+StaticVariable.month));
                    new RefreshTask(ListFragment.this).execute();
                    break;
                case 12:
                    StaticVariable.year += 1;
                    StaticVariable.month = 1;
                    yearText.setText(getString(R.string.contents,""+StaticVariable.year));
                    monthText.setText(getString(R.string.contents,"0"+StaticVariable.month));
                    new RefreshTask(ListFragment.this).execute();
                    break;
                default:
                    Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        ActivityResultLauncher<Intent> addActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == INSERT_SUCCESS) {
                        Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == ADD_CANCEL) {
                        Toast.makeText(activity, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        add.setOnClickListener(view -> {
            Intent it = new Intent(getActivity(), AddActivity.class);
            switch (mViewPager2.getCurrentItem()){
                case 0:
                case 2:
                    it.putExtra("category", "지출");
                    break;
                case 1:
                    it.putExtra("category", "수입");
                    break;
                default:
                    Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            addActivityResult.launch(it);
        });

        order.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Loading_Dialog_Theme);
            builder.setItems(R.array.order, (dialog, pos) -> {
                switch (pos) {
                    case 0:
                        if(StaticVariable.sort != ASC_DAY) {
                            StaticVariable.sort = ASC_DAY;
                            new RefreshTask(ListFragment.this).execute();
                        }
                        break;
                    case 1:
                        if(StaticVariable.sort != DESC_DAY) {
                            StaticVariable.sort = DESC_DAY;
                            new RefreshTask(ListFragment.this).execute();
                        }
                        break;
                    case 2:
                        if(StaticVariable.sort != ASC_MONEY) {
                            StaticVariable.sort = ASC_MONEY;
                            new RefreshTask(ListFragment.this).execute();
                        }
                        break;
                    case 3:
                        if(StaticVariable.sort != DESC_MONEY) {
                            StaticVariable.sort = DESC_MONEY;
                            new RefreshTask(ListFragment.this).execute();
                        }
                        break;
                    default:
                        Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        return root;
    }

    private void fragmentRefresh() {
        Log.d("MyTag", "ListFragment fragmentRefresh");
        mFragmentAdapter.submitList(new ArrayList<Fragment>() {
            {
                add(new List_AllFragment());
                add(new List_IncomeFragment());
                add(new List_ExpenFragment());
            }
        });
    }

    public void singleSelectionMode() {
        mTabLayout.setVisibility(View.VISIBLE);
        mToolbar.setVisibility(View.GONE);
        prev.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        order.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
    }

    public void multiSelectionMode() {
        mTabLayout.setVisibility(View.GONE);
        mToolbar.setVisibility(View.VISIBLE);
        prev.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        add.setVisibility(View.GONE);
        order.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        Log.d("MyTag", "ListFragment onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("MyTag", "ListFragment onResume");
        super.onResume();
        singleSelectionMode();
        mViewPager2.setUserInputEnabled(true);
        yearText.setText(getString(R.string.contents,""+StaticVariable.year));
        if(StaticVariable.month < 10)
            monthText.setText(getString(R.string.contents,"0"+StaticVariable.month));
        else
            monthText.setText(getString(R.string.contents,""+StaticVariable.month));
        new RefreshTask(ListFragment.this).execute();
    }

    @Override
    public void onPause() {
        Log.d("MyTag", "ListFragment onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("MyTag", "ListFragment onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("MyTag", "ListFragment onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("MyTag", "ListFragment onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("MyTag", "ListFragment onDetach");
        super.onDetach();
    }
}
