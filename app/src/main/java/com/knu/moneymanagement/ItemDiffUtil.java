package com.knu.moneymanagement;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ItemDiffUtil extends DiffUtil.ItemCallback<RecyclerViewItem> {

    @Override
    public boolean areItemsTheSame(@NonNull RecyclerViewItem oldItem, @NonNull RecyclerViewItem newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull RecyclerViewItem oldItem, @NonNull RecyclerViewItem newItem) {
        return oldItem.equals(newItem);
    }

    /*private final List<RecyclerViewItem> oldList;
    private final List<RecyclerViewItem> newList;

    public ItemDiffUtil(List<RecyclerViewItem> oldList, List<RecyclerViewItem> newList) {
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
        RecyclerViewItem oldItem = oldList.get(oldItemPosition);
        RecyclerViewItem newItem = newList.get(newItemPosition);

        return oldItem.equals(newItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }*/
}
