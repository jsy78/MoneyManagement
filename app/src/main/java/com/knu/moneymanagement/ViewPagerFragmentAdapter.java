package com.knu.moneymanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.knu.moneymanagement.calendar.Calendar_AllFragment;
import com.knu.moneymanagement.calendar.Calendar_ExpenFragment;
import com.knu.moneymanagement.calendar.Calendar_IncomeFragment;
import com.knu.moneymanagement.database.StaticVariable;
import com.knu.moneymanagement.graph.Graph_AcculBalanceFragment;
import com.knu.moneymanagement.graph.Graph_AccumulateFragment;
import com.knu.moneymanagement.graph.Graph_BalanceFragment;
import com.knu.moneymanagement.graph.Graph_MonthFragment;
import com.knu.moneymanagement.graph.Graph_YearFragment;
import com.knu.moneymanagement.list.List_AllFragment;
import com.knu.moneymanagement.list.List_ExpenFragment;
import com.knu.moneymanagement.list.List_IncomeFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private final FragmentManager mFragmentManager;
    private final AsyncListDiffer<Fragment> mDiffer;

    public ViewPagerFragmentAdapter(FragmentManager fm, Lifecycle lc, DiffUtil.ItemCallback<Fragment> diffCallback) {
        super(fm, lc);
        mFragmentManager = fm;
        mDiffer = new AsyncListDiffer<>(this, diffCallback);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }
        else {
            String tag = "f" + holder.getItemId();
            Fragment fragment = this.mFragmentManager.findFragmentByTag(tag);

            if (fragment == null) {
                super.onBindViewHolder(holder, position, payloads);
            }
            else {
                /*for (int i = 0; i < getItemCount(); i++) {
                    if (createFragment(i).equals(fragment)) {
                        notifyItemChanged(i);
                        break;
                    }
                }*/
                //this.submitList(this.getCurrentList());
                if (fragment instanceof Calendar_AllFragment)
                    ((Calendar_AllFragment) fragment).createAllInfo(StaticVariable.year, StaticVariable.month, StaticVariable.day);
                else if (fragment instanceof Calendar_IncomeFragment)
                    ((Calendar_IncomeFragment) fragment).createIncomeInfo(StaticVariable.year, StaticVariable.month, StaticVariable.day);
                else if (fragment instanceof Calendar_ExpenFragment)
                    ((Calendar_ExpenFragment) fragment).createExpenInfo(StaticVariable.year, StaticVariable.month, StaticVariable.day);
                else if (fragment instanceof List_AllFragment)
                    ((List_AllFragment) fragment).createAllList(StaticVariable.year, StaticVariable.month, StaticVariable.sort);
                else if (fragment instanceof List_IncomeFragment)
                    ((List_IncomeFragment) fragment).createIncomeList(StaticVariable.year, StaticVariable.month, StaticVariable.sort);
                else if (fragment instanceof List_ExpenFragment)
                    ((List_ExpenFragment) fragment).createExpenList(StaticVariable.year, StaticVariable.month, StaticVariable.sort);
                else if (fragment instanceof Graph_MonthFragment)
                    ((Graph_MonthFragment) fragment).createChart(StaticVariable.year, StaticVariable.month);
                else if (fragment instanceof Graph_YearFragment)
                    ((Graph_YearFragment) fragment).createLine(StaticVariable.year);
                else if (fragment instanceof Graph_AccumulateFragment)
                    ((Graph_AccumulateFragment) fragment).createLine(StaticVariable.year);
                else if (fragment instanceof Graph_BalanceFragment)
                    ((Graph_BalanceFragment) fragment).createLine(StaticVariable.year);
                else if (fragment instanceof Graph_AcculBalanceFragment)
                    ((Graph_AcculBalanceFragment) fragment).createLine(StaticVariable.year);
                else
                    super.onBindViewHolder(holder, position, payloads);
            }
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mDiffer.getCurrentList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDiffer.getCurrentList().get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        for (Fragment fragment : mDiffer.getCurrentList()) {
            if (fragment.hashCode() == itemId)
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    /*@NonNull
    public List<Fragment> getCurrentList() {
        return mDiffer.getCurrentList();
    }*/

    public void submitList(@Nullable List<Fragment> newList) {
        mDiffer.submitList(newList != null ? new ArrayList<>(newList) : null);
        //mDiffer.submitList(null);
        //mDiffer.submitList(newList);
    }
}
