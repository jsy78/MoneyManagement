package com.knu.moneymanagement;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

public class FragmentDiffUtil extends DiffUtil.ItemCallback<Fragment> {
    @Override
    public boolean areItemsTheSame(@NonNull Fragment oldItem, @NonNull Fragment newItem) {
        Log.d("MyTag", oldItem.getClass().getName() + " : oldFragment");
        Log.d("MyTag", newItem.getClass().getName() + " : newFragment");
        Log.d("MyTag", " : oldFragment.getId == newFragment.getId : " + (oldItem.getId() == newItem.getId()));
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Fragment oldItem, @NonNull Fragment newItem) {
        Log.d("MyTag", "oldFragment.equals(newFragment) : " + oldItem.equals(newItem));
        return oldItem.equals(newItem);
        //return Objects.equals(oldItem.getTag(), newItem.getTag());
    }
    //private final List<Fragment> oldList;
    //private final List<Fragment> newList;

    /*public FragmentDiffUtil(List<Fragment> oldList, List<Fragment> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Fragment oldItem = oldList.get(oldItemPosition);
        Fragment newItem = newList.get(newItemPosition);

        return oldItem.equals(newItem);
    }

    //@Nullable
    //@Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }*/
}
