package com.ixbiopharma.glucose.journal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.ixbiopharma.glucose.model.JournalAdvice;
import com.ixbiopharma.glucose.model.JournalHeader;
import com.ixbiopharma.glucose.model.JournalNewsWrapper;
import com.ixbiopharma.glucose.model.Type;
import com.ixbiopharma.glucose.news.detail.NewsDetailActivity;
import com.ixbiopharma.glucose.search.NewsAdapter;
import com.ixbiopharma.glucose.utils.DataTypeUtils;
import com.ixbiopharma.glucose.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * AdapterVH
 *
 * Created by ivan on 6/12/17.
 */

class AdapterVH {

    static class ItemVH extends RecyclerView.ViewHolder {

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

        ItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Context context, DataType dataType, JournalListener listener) {

            String dateFormatted = TimeUtils.dateToTimeFormat(dataType.getDate());

            int color;
            int iconId;
            boolean isFood = false;

            clickable.setOnClickListener(v -> listener.onJournalClick(dataType));
            clickable.setOnLongClickListener(v -> {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.ctx_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.delete) {
                        listener.onDelete(dataType);
                    } else {
                        listener.onEdit(dataType);
                    }

                    return true;
                });
                popup.show();
                return true;
            });

            iconId = DataTypeUtils.getTypeIcon(dataType);
            color = ContextCompat.getColor(context, DataTypeUtils.getTypeColor(dataType));
            dateText.setText(dateFormatted);

            switch (dataType.getDataType()) {
                case Type.FOOD:
                    Food food = (Food) dataType;
                    textView.setText(food.getName());
                    breakfast.setText(DataTypeUtils.getFoodType(food.getType()));
                    breakfast.setVisibility(View.VISIBLE);
                    subt.setVisibility(View.GONE);
                    dot.setVisibility(View.GONE);

                    int[] attrs = new int[]{android.R.attr.selectableItemBackground};
                    TypedArray ta = context.obtainStyledAttributes(attrs);
                    Drawable drawableFromTheme = ta.getDrawable(0);
                    ta.recycle();

                    clickable.setBackground(drawableFromTheme);
                    clickable.setClickable(true);

                    if (food.uri != null && !food.uri.isEmpty()) {
                        isFood = true;
                        icon.setPadding(0, 0, 0, 0);

                        ImageRequest request =
                                ImageRequestBuilder.newBuilderWithSource(Uri.parse(food.uri))
                                .setResizeOptions(new ResizeOptions(600, 400))
                                .setRotationOptions(RotationOptions.autoRotate())
                                .build();


                        icon.setController(Fresco.newDraweeControllerBuilder()
                                .setImageRequest(request)
                                .build());
                    }
                    break;
                default:
                    textView.setText(DataTypeUtils.getValueWithType(dataType));

                    if (dataType.getSubTitle() == null || dataType.getSubTitle().isEmpty()) {
                        dot.setVisibility(View.GONE);
                        subt.setVisibility(View.GONE);
                    } else {
                        subt.setText(dataType.getSubTitle());
                        dot.setVisibility(View.VISIBLE);
                        subt.setVisibility(View.VISIBLE);
                    }

                    breakfast.setVisibility(View.GONE);
                    break;
            }

            color_view.setBackgroundColor(color);
            Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, iconId)).mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            if (!isFood) {
                icon.setImageDrawable(drawable);
            }
        }
    }

    static class NewsVH extends RecyclerView.ViewHolder {

        NewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(JournalNewsWrapper journalNewsWrapper, Context context) {
            RecyclerView newsRv = (RecyclerView) itemView;

            NewsAdapter newsAdapter = new NewsAdapter(news1 ->
                    NewsDetailActivity.start(context, Integer.parseInt(news1.getId())));

            newsAdapter.setData(journalNewsWrapper.getNews());
            newsRv.setAdapter(newsAdapter);
        }
    }

    static class AdviceVH extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView titleT;
        @BindView(R.id.description)
        TextView descriptionT;

        AdviceVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(JournalAdvice advice) {
            descriptionT.setText(advice.getDescription());
            titleT.setText(advice.getAdvice());
        }
    }

    static class HeaderVH extends RecyclerView.ViewHolder {

        @BindView(R.id.adapterPositionTextView)
        TextView adapterPositionTextView;

        @BindView(R.id.textView)
        TextView textView;

        HeaderVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(JournalHeader journalHeader) {
            String dateFormatted = TimeUtils.dateToDateFormat(journalHeader.getDate());
            adapterPositionTextView.setText(dateFormatted);
            textView.setText(journalHeader.getAgo());
        }
    }
}
