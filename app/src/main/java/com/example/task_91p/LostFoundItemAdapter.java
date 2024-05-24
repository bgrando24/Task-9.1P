package com.example.task_91p;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LostFoundItemAdapter extends RecyclerView.Adapter<LostFoundItemAdapter.LostFoundItemViewHolder> {

    private List<LostFoundItem> lostFoundItemList;
    private Context context;
    private OnItemClickListener onItemClickListener;


//    required for setting onclick listener for each item in recycler view
    public interface OnItemClickListener {
        void onItemClick(LostFoundItem item);
    }

    public LostFoundItemAdapter(Context context, List<LostFoundItem> lostFoundItemList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.lostFoundItemList = lostFoundItemList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public LostFoundItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lost_found_item, parent, false);
        return new LostFoundItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LostFoundItemViewHolder holder, int position) {
        LostFoundItem item = lostFoundItemList.get(position);
        holder.lostFoundItemTextView.setText(item.getName());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return lostFoundItemList.size();
    }

    public static class LostFoundItemViewHolder extends RecyclerView.ViewHolder {
        TextView lostFoundItemTextView;

        public LostFoundItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lostFoundItemTextView = itemView.findViewById(R.id.lostFoundItemTextView);
        }
    }
}
