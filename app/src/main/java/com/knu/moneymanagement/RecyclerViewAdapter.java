package com.knu.moneymanagement;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends ListAdapter<RecyclerViewItem, RecyclerViewAdapter.MyViewHolder> {

    //private final ArrayList<RecyclerViewItem> recyclerItem = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
//    public interface OnItemTouchListener {
//        void onItemTouch(View view, MotionEvent event, int position);
//    }

    private OnItemClickListener mClickListener = null;
    private OnItemLongClickListener mLongClickListener = null;
//    private OnItemTouchListener mTouchListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.mLongClickListener = listener;
    }
//    public void setOnItemTouchListener(OnItemTouchListener listener){
//        this.mTouchListener = listener;
//    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView imageView;
        TextView textDate;
        TextView textDetail;
        TextView textMoney;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            linearLayout = itemView.findViewById(R.id.linearView);
            imageView = itemView.findViewById(R.id.imageView);
            textDate = itemView.findViewById(R.id.dateView);
            textDetail = itemView.findViewById(R.id.detailView);
            textMoney = itemView.findViewById(R.id.moneyView);

//            itemView.setOnTouchListener(new View.OnTouchListener() {
//                @SuppressLint("ClickableViewAccessibility")
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION) {
//                        if (mTouchListener != null) {
//                            mTouchListener.onItemTouch(v, event, pos);
//                        }
//                    }
//                    return false;
//                }
//            });

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, pos);
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (mLongClickListener != null) {
                        mLongClickListener.onItemLongClick(v, pos);
                    }
                }
                return false;
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    /*public RecyclerViewAdapter(ArrayList<RecyclerViewItem> list) {
        this.recyclerItem = list;
    }*/
    public RecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<RecyclerViewItem> diffCallback) {
        super(diffCallback);
    }

    @Override
    public void submitList(@Nullable List<RecyclerViewItem> list) {
        //super.submitList(list != null ? new ArrayList<>(list) : null);
        super.submitList(list);
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_custom, parent, false);

        return new MyViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        RecyclerViewItem item = getItem(position);

        if(item.isChecked())
            viewHolder.linearLayout.setBackgroundColor(0xFFC0C0C0);
        else
            viewHolder.linearLayout.setBackgroundColor(0x00000000);

        viewHolder.imageView.setImageBitmap(item.getIcon());
        viewHolder.textDate.setText(item.getDate());
        viewHolder.textDetail.setText(item.getDetail());
        viewHolder.textMoney.setText(item.getMoneyStr());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public ArrayList<RecyclerViewItem> getSelectedItem() {
        ArrayList<RecyclerViewItem> selected = new ArrayList<>();

        for (int i = 0; i < getCurrentList().size(); i++) {
            if (getCurrentList().get(i).isChecked()) {
                selected.add(getCurrentList().get(i));
            }
        }
        return selected;
    }

    /*public int getSelectedItemCount() {
        int count = 0;

        for (int i = 0; i < getCurrentList().size(); i++) {
            if (getCurrentList().get(i).isChecked()) {
                count += 1;
            }
        }
        return count;
    }*/

    //public void clearSelectedItem() {
        /*int positionStart = -1;

        for (int i = 0; i < getCurrentList().size(); i++) {
            if (getCurrentList().get(i).isChecked()) {
                positionStart = i;
                break;
            }
        }

        if (positionStart >= 0) {
            int count = 0;

            for (int i = positionStart; i < getCurrentList().size(); i++) {
                if (getCurrentList().get(i).isChecked()) {
                    getCurrentList().get(i).setCheck(false);
                    count += 1;
                }
            }
            notifyItemRangeChanged(positionStart, count);
        }*/

        /*for (int i = 0; i < getCurrentList().size(); i++) {
            if (getCurrentList().get(i).isChecked()) {
                getCurrentList().get(i).setCheck(false);
                notifyItemChanged(i);
            }
        }*/

        /*ArrayList<RecyclerViewItem> newList = new ArrayList<>();
        for (RecyclerViewItem item : getCurrentList()) {
            newList.add(item.clone());
        }

        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i).isChecked()) {
                newList.get(i).setCheck(false);
            }
        }
        submitList(newList);*/
    //}

    /*public void updateList(ArrayList<RecyclerViewItem> newList) {
        ItemDiffCallback callback = new ItemDiffCallback(this.recyclerItem, newList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback, false);

        this.recyclerItem.clear();
        this.recyclerItem.addAll(newList);
        //new Handler(Looper.getMainLooper()).post(() -> diffResult.dispatchUpdatesTo(RecyclerViewAdapter.this));
        diffResult.dispatchUpdatesTo(this);
    }*/
}


