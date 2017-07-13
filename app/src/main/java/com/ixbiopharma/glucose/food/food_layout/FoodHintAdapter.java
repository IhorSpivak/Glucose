package com.ixbiopharma.glucose.food.food_layout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.FoodType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FoodHintAdapter
 * <p>
 * Created by ivan on 23.04.17.
 */

class FoodHintAdapter extends RecyclerView.Adapter<FoodHintAdapter.VH> {

    private List<FoodType> data = new ArrayList<>();
    private FoodHintListener foodHintListener;

    FoodHintAdapter(FoodHintListener foodHintListener) {
        this.foodHintListener = foodHintListener;
    }

    public void setData(List<FoodType> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){
        this.data.clear();
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_food_hint_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.title.setText(data.get(position).getName());
        holder.title.setOnClickListener(v -> foodHintListener.onHintClick(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
