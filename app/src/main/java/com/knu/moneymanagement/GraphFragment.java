package com.knu.moneymanagement;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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
import com.knu.moneymanagement.graph.Graph_AcculBalanceFragment;
import com.knu.moneymanagement.graph.Graph_AccumulateFragment;
import com.knu.moneymanagement.graph.Graph_BalanceFragment;
import com.knu.moneymanagement.graph.Graph_MonthFragment;
import com.knu.moneymanagement.graph.Graph_YearFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GraphFragment extends Fragment implements Constant {

    private Activity activity;
    private ViewPagerFragmentAdapter mFragmentAdapter;
    private LoadingDialog mLoadingDialog;

    /*private static class PageChangeTask extends AsyncTask<Integer, Void, Void> {

        private final WeakReference<GraphFragment> FragmentReference;

        private PageChangeTask(GraphFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            GraphFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("페이지 전환 중...");
            fragment.mLoadingDialog.show();
            fragment.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Integer... voids) {
            final GraphFragment fragment = FragmentReference.get();

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
            GraphFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            fragment.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(result);
        }

    }*/

    private static class RefreshTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<GraphFragment> FragmentReference;

        private RefreshTask(GraphFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            GraphFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("데이터 갱신 중...");
            fragment.mLoadingDialog.show();
            fragment.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final GraphFragment fragment = FragmentReference.get();

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
            GraphFragment fragment = FragmentReference.get();

            if(fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            fragment.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(result);
        }

    }

    private TextView yearText;

    public GraphFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        else
            activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graph, container, false);

        mLoadingDialog = new LoadingDialog(activity, R.style.Loading_Dialog_Theme);

        yearText = root.findViewById(R.id.yearGraphText);
        Button prev = root.findViewById(R.id.btn_prev_graph_year);
        Button next = root.findViewById(R.id.btn_next_graph_year);

        ViewPager2 mViewPager2 = root.findViewById(R.id.viewPager3);
        mViewPager2.setOffscreenPageLimit(4);

        mFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), getLifecycle(), new FragmentDiffUtil());

        mViewPager2.setAdapter(mFragmentAdapter);
        /*mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mViewPager2.setCurrentItem(position);
                //new PageChangeTask(GraphFragment.this).execute();
            }
        });*/

        TabLayout mTabLayout = root.findViewById(R.id.tabLayout3);
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            switch (position) {
                case 0 :
                    tab.setText("월 수입/지출 비율");
                    break;
                case 1 :
                    tab.setText("연 수입/지출 내역");
                    break;
                case 2 :
                    tab.setText("연 수입/지출 누적");
                    break;
                case 3 :
                    tab.setText("연 수입/지출 잔액");
                    break;
                case 4 :
                    tab.setText("연 최종 누적 잔액");
                    break;
                default:
                    Toast.makeText(activity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }).attach();

        prev.setOnClickListener(view -> {
            StaticVariable.year -= 1;
            StaticVariable.day = 1;
            yearText.setText(getString(R.string.contents,""+StaticVariable.year));
            new RefreshTask(GraphFragment.this).execute();
        });

        next.setOnClickListener(view -> {
            StaticVariable.year += 1;
            StaticVariable.day = 1;
            yearText.setText(getString(R.string.contents,""+StaticVariable.year));
            new RefreshTask(GraphFragment.this).execute();
        });

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

        return root;
    }

    private void fragmentRefresh() {
        mFragmentAdapter.submitList(new ArrayList<Fragment>() {
            {
                add(new Graph_MonthFragment());
                add(new Graph_YearFragment());
                add(new Graph_AccumulateFragment());
                add(new Graph_BalanceFragment());
                add(new Graph_AcculBalanceFragment());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        yearText.setText(getString(R.string.contents,""+StaticVariable.year));
        new RefreshTask(GraphFragment.this).execute();
    }
}
