package com.ixbiopharma.glucose.food.food_layout;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * ImageAdapter
 * <p>
 * Created by ivan on 23.04.17.
 */

class ImageAdapter extends PagerAdapter {

    private List<Food> data = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private List<Food> d = new ArrayList<>();

    ImageAdapter(List<Food> data, @NonNull View emptyView) {
        mLayoutInflater = (LayoutInflater) emptyView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DataSetObserver dataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (getCount() == 0) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }

            }
        };

        registerDataSetObserver(dataSetObserver);
        dataSetObserver.onChanged();

        this.data = data;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        d.clear();

        for (int i = 0; i < data.size(); i++) {
            Food food = data.get(i);

            if (food.uri != null && !food.uri.isEmpty()) {
                d.add(data.get(i));
            }
        }
        return d.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.food_image_item, container, false);

        SimpleDraweeView imageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);

        String uri = d.get(position).uri;

        if (uri.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_launcher);
        } else {

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                    .setResizeOptions(new ResizeOptions(1280, 800))
                    .build();

            imageView.setController(Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build());

        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((SimpleDraweeView) object);
    }

}
