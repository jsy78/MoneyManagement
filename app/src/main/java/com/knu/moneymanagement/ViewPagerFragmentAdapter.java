package com.knu.moneymanagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> mFragmentItems = new ArrayList<>();
    private final ArrayList<Long> mFragmentID = new ArrayList<>();

    public ViewPagerFragmentAdapter(FragmentManager fm, Lifecycle lc) {
        super(fm, lc);
    }

    void addFragment(Fragment fragment){
        mFragmentItems.add(fragment);
        mFragmentID.add((long)fragment.hashCode());
    }

    void clearFragment() {
        mFragmentItems.clear();
        mFragmentID.clear();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)mFragmentItems.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return mFragmentID.contains(itemId);
    }

    @Override
    public int getItemCount() {
        return mFragmentItems.size();
    }
}
