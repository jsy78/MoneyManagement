package com.knu.moneymanagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> mFragmentItems = new ArrayList<>();

    ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentItems.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentItems.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    void addFragment(Fragment fragment){
        mFragmentItems.add(fragment);
    }

    void clearFragment() {
        mFragmentItems.clear();
    }

}
