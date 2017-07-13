package com.ixbiopharma.glucose.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.Type;
import com.ixbiopharma.glucose.utils.DataTypeUtils;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * JournalItemsAdapter
 * <p>
 * Created by ivan on 27.05.17.
 */

class JournalItemsAdapter extends RecyclerView.Adapter<JournalItemsAdapter.VH> {

    private List<DataType> dataTypes = new ArrayList<>();
    private Context context;

    void setData(List<DataType> data) {
        dataTypes.clear();
        dataTypes.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_simple_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        DataType dataType = dataTypes.get(position);
        String dateFormatted = TimeUtils.dateToTimeFormat(dataType.getDate());

        int color;
        int iconId;
        boolean isFood = false;

        iconId = DataTypeUtils.getTypeIcon(dataType);
        color = ContextCompat.getColor(context, DataTypeUtils.getTypeColor(dataType));
        holder.dateText.setText(dateFormatted);

        switch (dataType.getDataType()) {
            case Type.FOOD:
                Food food = (Food) dataType;
                holder.textView.setText(food.getName());
                holder.breakfast.setText(DataTypeUtils.getFoodType(food.getType()));
                holder.breakfast.setVisibility(View.VISIBLE);
                holder.subt.setVisibility(View.GONE);
                holder.dot.setVisibility(View.GONE);

                int[] attrs = new int[]{android.R.attr.selectableItemBackground};
                TypedArray ta = context.obtainStyledAttributes(attrs);
                Drawable drawableFromTheme = ta.getDrawable(0);
                ta.recycle();

                holder.clickable.setBackground(drawableFromTheme);
                holder.clickable.setClickable(true);

                if (food.uri != null && !food.uri.isEmpty()) {
                    isFood = true;
                    holder.icon.setPadding(0, 0, 0, 0);

                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(food.uri))
                            .setResizeOptions(new ResizeOptions(600, 400))
                            .setRotationOptions(RotationOptions.autoRotate())
                            .build();


                    holder.icon.setController(Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .build());
                }
                break;
            default:
                holder.textView.setText(DataTypeUtils.getValueWithType(dataType));

                if (dataType.getSubTitle() == null || dataType.getSubTitle().isEmpty()) {
                    holder.dot.setVisibility(View.GONE);
                } else {
                    holder.subt.setText(dataType.getSubTitle());
                }

                holder.breakfast.setVisibility(View.GONE);
                holder.subt.setVisibility(View.VISIBLE);
                break;
        }

        holder.color_view.setBackgroundColor(color);
        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, iconId)).mutate();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        if (!isFood) {
            holder.icon.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return dataTypes.size();
    }

    class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.clickable)
        View clickable;

        @BindView(R.id.dot)
        View dot;

        @BindView(R.id.color_view)
        View color_view;

        @BindView(R.id.textView)
        TextView textView;

        @BindView(R.id.adapterPositionTextView)
        TextView dateText;

        @BindView(R.id.subt)
        TextView subt;

        @BindView(R.id.breakfast)
        TextView breakfast;

        @BindView(R.id.icon)
        SimpleDraweeView icon;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
