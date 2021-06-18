package com.knu.moneymanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.knu.moneymanagement.calendar.Calendar_AllFragment;
import com.knu.moneymanagement.calendar.Calendar_ExpenFragment;
import com.knu.moneymanagement.calendar.Calendar_IncomeFragment;
import com.knu.moneymanagement.calendarDecorator.DoubleDecorator;
import com.knu.moneymanagement.calendarDecorator.SingleDecorator;
import com.knu.moneymanagement.database.Constant;
import com.knu.moneymanagement.database.StaticVariable;
import com.knu.moneymanagement.database.MoneyDBCtrct;
import com.knu.moneymanagement.database.MoneyDBHelper;
import com.knu.moneymanagement.calendarDecorator.OneDayDecorator;
import com.knu.moneymanagement.calendarDecorator.SaturdayDecorator;
import com.knu.moneymanagement.calendarDecorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class CalendarFragment extends Fragment implements Constant {

    private Activity activity;
    private LoadingDialog mLoadingDialog;
    private ViewPager mViewPager;
    private ViewPagerFragmentAdapter mFragmentAdapter;
    private MaterialCalendarView calendarView;
    private LinearLayout addLinear;
    private LinearLayout resetLinear;
    private boolean fabExpanded = false;

    private static class PageChangeTask extends AsyncTask<Integer, Void, Void> {

        private final WeakReference<CalendarFragment> FragmentReference;

        private PageChangeTask(CalendarFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            CalendarFragment fragment = FragmentReference.get();

            if (fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("페이지 전환 중...");
            fragment.mLoadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Integer... voids) {
            final CalendarFragment fragment = FragmentReference.get();

            if (fragment != null && fragment.activity != null && !fragment.activity.isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fragment.activity.runOnUiThread(() -> {
                    fragment.mViewPager.setCurrentItem(voids[0]);
                    fragment.fragmentRefresh();
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            CalendarFragment fragment = FragmentReference.get();

            if (fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            super.onPostExecute(result);
        }

    }

    private static class RefreshTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<CalendarFragment> FragmentReference;

        private RefreshTask(CalendarFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            CalendarFragment fragment = FragmentReference.get();

            if (fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("데이터 갱신 중...");
            fragment.mLoadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final CalendarFragment fragment = FragmentReference.get();

            if (fragment != null && fragment.activity != null && !fragment.activity.isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fragment.activity.runOnUiThread(() -> {
                    fragment.fragmentRefresh();
                    fragment.calendarView.setSelectedDate(CalendarDay.from(StaticVariable.year, StaticVariable.month, StaticVariable.day));
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            CalendarFragment fragment = FragmentReference.get();

            if (fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            super.onPostExecute(result);
        }

    }

    private static class DecorateTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<CalendarFragment> FragmentReference;

        private DecorateTask(CalendarFragment context) {
            FragmentReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            CalendarFragment fragment = FragmentReference.get();

            if (fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.setTitle("달력 갱신 중...");
            fragment.mLoadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final CalendarFragment fragment = FragmentReference.get();

            if (fragment != null && fragment.activity != null && !fragment.activity.isFinishing()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fragment.activity.runOnUiThread(() -> {
                    fragment.fragmentRefresh();
                    fragment.calendarView.removeDecorators();
                    fragment.calendarView.addDecorator(new SundayDecorator());
                    fragment.calendarView.addDecorator(new SaturdayDecorator());
                    fragment.calendarView.addDecorator(new OneDayDecorator());
                    fragment.dayDots(StaticVariable.year, StaticVariable.month);
                    fragment.calendarView.setSelectedDate(CalendarDay.from(StaticVariable.year, StaticVariable.month, StaticVariable.day));
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            CalendarFragment fragment = FragmentReference.get();

            if (fragment == null || fragment.activity == null || fragment.activity.isFinishing())
                return;

            fragment.mLoadingDialog.dismiss();
            super.onPostExecute(result);
        }

    }

    private final ArrayList<CalendarDay> incomeDays = new ArrayList<>();
    private final ArrayList<CalendarDay> expenDays = new ArrayList<>();
    private final ArrayList<CalendarDay> inexpDays = new ArrayList<>();

    public CalendarFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        } else
            activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        mLoadingDialog = new LoadingDialog(activity, R.style.Loading_Dialog_Theme);

        TabLayout mTabLayout = root.findViewById(R.id.tabLayout);

        mTabLayout.addTab(mTabLayout.newTab().setText("전체 보기"));
        mTabLayout.addTab(mTabLayout.newTab().setText("수입 보기"));
        mTabLayout.addTab(mTabLayout.newTab().setText("지출 보기"));

        calendarView = root.findViewById(R.id.calendarView);

        calendarView.setDynamicHeightEnabled(false);
        calendarView.setAllowClickDaysOutsideCurrentMonth(false);

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (date.getYear() != StaticVariable.year || date.getMonth() != StaticVariable.month || date.getDay() != StaticVariable.day) {
                if (StaticVariable.month != date.getMonth()) {
                    calendarView.setSelectedDate(CalendarDay.from(StaticVariable.year, StaticVariable.month, StaticVariable.day));
                } else {
                    StaticVariable.year = date.getYear();
                    StaticVariable.month = date.getMonth();
                    StaticVariable.day = date.getDay();
                    new RefreshTask(CalendarFragment.this).execute();
                }
            }
        });

        calendarView.setOnMonthChangedListener((widget, date) -> {
            StaticVariable.year = date.getYear();
            StaticVariable.month = date.getMonth();
            StaticVariable.day = date.getDay();
            new DecorateTask(CalendarFragment.this).execute();
        });

        NestedScrollView scrollView = root.findViewById(R.id.nestedScrollView);
        scrollView.setFillViewport(true);

        mViewPager = root.findViewById(R.id.viewPager);

        mFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                new PageChangeTask(CalendarFragment.this).execute(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        addLinear = root.findViewById(R.id.add_linear);
        resetLinear = root.findViewById(R.id.reset_linear);
        FloatingActionButton main = root.findViewById(R.id.main_fab);
        FloatingActionButton addFab = root.findViewById(R.id.add_fab);
        FloatingActionButton resetFab = root.findViewById(R.id.reset_fab);
        CardView addCard = root.findViewById(R.id.add_card);
        CardView resetCard = root.findViewById(R.id.reset_card);

        main.setOnClickListener(v -> {
            if (fabExpanded) {
                Animation animation = new RotateAnimation(0f, 90f);
                animation.setDuration(100);

                addLinear.setVisibility(View.INVISIBLE);
                resetLinear.setVisibility(View.INVISIBLE);
                addLinear.setAnimation(animation);
                resetLinear.setAnimation(animation);
                fabExpanded = false;
            } else {
                Animation animation = new RotateAnimation(90f, 0f);
                animation.setDuration(100);

                addLinear.setVisibility(View.VISIBLE);
                resetLinear.setVisibility(View.VISIBLE);
                addLinear.setAnimation(animation);
                resetLinear.setAnimation(animation);
                fabExpanded = true;
            }
        });

        addCard.setOnClickListener(v -> {
            Intent it = new Intent(getActivity(), AddActivity.class);
            switch (mViewPager.getCurrentItem()) {
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
            startActivityForResult(it, ACT_ADD);
        });

        addFab.setOnClickListener(v -> {
            Intent it = new Intent(getActivity(), AddActivity.class);
            switch (mViewPager.getCurrentItem()) {
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
            startActivityForResult(it, ACT_ADD);
        });


        resetCard.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            CalendarDay calendarDay = calendarView.getCurrentDate();

            if (StaticVariable.year != cal.get(Calendar.YEAR) || StaticVariable.month != cal.get(Calendar.MONTH) + 1 || StaticVariable.day != cal.get(Calendar.DATE)) {
                StaticVariable.year = cal.get(Calendar.YEAR);
                StaticVariable.month = cal.get(Calendar.MONTH) + 1;
                StaticVariable.day = cal.get(Calendar.DATE);

                if (calendarDay.getYear() == StaticVariable.year && calendarDay.getMonth() == StaticVariable.month) {
                    new RefreshTask(CalendarFragment.this).execute();
                } else {
                    calendarView.setCurrentDate(CalendarDay.from(StaticVariable.year, StaticVariable.month, StaticVariable.day), true);
                }
            }
        });

        resetFab.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            CalendarDay calendarDay = calendarView.getCurrentDate();

            if (StaticVariable.year != cal.get(Calendar.YEAR) || StaticVariable.month != cal.get(Calendar.MONTH) + 1 || StaticVariable.day != cal.get(Calendar.DATE)) {
                StaticVariable.year = cal.get(Calendar.YEAR);
                StaticVariable.month = cal.get(Calendar.MONTH) + 1;
                StaticVariable.day = cal.get(Calendar.DATE);

                if (calendarDay.getYear() == StaticVariable.year && calendarDay.getMonth() == StaticVariable.month) {
                    new RefreshTask(CalendarFragment.this).execute();
                } else {
                    calendarView.setCurrentDate(CalendarDay.from(StaticVariable.year, StaticVariable.month, StaticVariable.day), true);
                }
            }
        });

        return root;
    }

    private void dayDots(int yearValue, int monthValue) {
        MoneyDBHelper dbHelper = new MoneyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlSelect;
        Cursor cursor;

        incomeDays.clear();
        expenDays.clear();
        inexpDays.clear();

        sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'수입'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue +
                " GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH + ", " + MoneyDBCtrct.COL_DAY;
        cursor = db.rawQuery(sqlSelect, null);
        while (cursor.moveToNext()) {
            int y = cursor.getInt(2);
            int m = cursor.getInt(3);
            int d = cursor.getInt(4);
            incomeDays.add(CalendarDay.from(y, m, d));
        }

        sqlSelect = MoneyDBCtrct.SQL_SELECT + " WHERE " + MoneyDBCtrct.COL_CATEGORY + "=" + "'지출'" + " AND " + MoneyDBCtrct.COL_YEAR + "=" + yearValue + " AND " + MoneyDBCtrct.COL_MONTH + "=" + monthValue +
                " GROUP BY " + MoneyDBCtrct.COL_YEAR + ", " + MoneyDBCtrct.COL_MONTH + ", " + MoneyDBCtrct.COL_DAY;
        cursor = db.rawQuery(sqlSelect, null);
        while (cursor.moveToNext()) {
            int y = cursor.getInt(2);
            int m = cursor.getInt(3);
            int d = cursor.getInt(4);
            expenDays.add(CalendarDay.from(y, m, d));
        }

        cursor.close();
        db.close();
        dbHelper.close();

        for (Iterator<CalendarDay> i = incomeDays.iterator(); i.hasNext(); ) {
            CalendarDay income = i.next();

            for (Iterator<CalendarDay> j = expenDays.iterator(); j.hasNext(); ) {
                CalendarDay expen = j.next();

                if (income.equals(expen)) {
                    inexpDays.add(income);
                    i.remove();
                    j.remove();
                }
            }
        }

        if (incomeDays.size() > 0)
            calendarView.addDecorator(new SingleDecorator(incomeDays, Color.BLUE));
        if (expenDays.size() > 0)
            calendarView.addDecorator(new SingleDecorator(expenDays, Color.RED));
        if (inexpDays.size() > 0)
            calendarView.addDecorator(new DoubleDecorator(inexpDays, Color.BLUE, Color.RED));

    }

    private void fragmentRefresh() {
        mFragmentAdapter.clearFragment();
        mFragmentAdapter.addFragment(new Calendar_AllFragment());
        mFragmentAdapter.addFragment(new Calendar_IncomeFragment());
        mFragmentAdapter.addFragment(new Calendar_ExpenFragment());
        mFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACT_ADD) {
            if (resultCode == INSERT_SUCCESS) {
                Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == ADD_CANCEL) {
                Toast.makeText(activity, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        calendarView.setCurrentDate(CalendarDay.from(StaticVariable.year, StaticVariable.month, StaticVariable.day), false);
        new DecorateTask(CalendarFragment.this).execute();
    }
}
