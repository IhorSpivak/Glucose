package com.ixbiopharma.glucose.food.food_layout;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.Food;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FoodItemAdapter
 * <p>
 * Created by ivan on 23.04.17.
 */

class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.VH> {

    private List<Food> data = new ArrayList<>();
    private static final int ITEM = 0;
    private static final int FOOTER = 1;
    private FoodItemListener itemListener;
    private boolean withInfo = false;
    private boolean withoutEditInfo = false;

    FoodItemAdapter(List<Food> data, FoodItemListener itemListener) {
        this.itemListener = itemListener;
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_food_footer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Food food = data.get(position);

        if (food.uri != null && !food.uri.isEmpty()) {
            holder.upload.setText("Change Photo");
        } else {
            holder.upload.setText("Upload Photo");
        }

        holder.title.setText(food.getName());

        if (withoutEditInfo) {
            holder.remove.setVisibility(View.GONE);
            holder.upload.setVisibility(View.GONE);
        }

        holder.remove.setPaintFlags(holder.remove.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.remove.setOnClickListener(v -> itemListener.onRemoveClick(food));

        holder.upload.setPaintFlags(holder.upload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.upload.setOnClickListener(v -> itemListener.onUploadClick(food));

        if (getItemViewType(position) == FOOTER) {
            if (withInfo) {
                holder.image_action.setVisibility(View.GONE);
            } else {
                holder.image_action.setVisibility(View.VISIBLE);
            }
            holder.image_action.setOnClickListener(v -> itemListener.onAddClick());
        } else {
            holder.image_action.setVisibility(View.GONE);
        }

        if (position == 0 && withInfo) {
            holder.info.setVisibility(View.VISIBLE);
            holder.energy.setText(food.getEnergy() + "Calories");
            holder.fibre.setText(food.getFibre() + "g");
            holder.sodium.setText(food.getSodium() + "mg");
        } else {
            holder.info.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size() - 1) return FOOTER;
        return ITEM;
    }

    void setWithInfo(boolean withInfo) {
        this.withInfo = withInfo;
    }

    void setWithoutEditInfo(boolean withInfo) {
        this.withoutEditInfo = withInfo;
    }

    class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.image_action)
        ImageView image_action;

        @BindView(R.id.upload)
        TextView upload;

        @BindView(R.id.remove)
        TextView remove;

        @BindView(R.id.energy)
        TextView energy;
        @BindView(R.id.fibre)
        TextView fibre;
        @BindView(R.id.sodium)
        TextView sodium;
        @BindView(R.id.info)
        View info;

        VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}