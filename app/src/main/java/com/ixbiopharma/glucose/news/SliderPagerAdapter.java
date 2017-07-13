package com.ixbiopharma.glucose.news;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.NewsPhoto;

import java.util.List;

/**
 * SliderPagerAdapter
 * <p>
 * Created by ivan on 07.05.17.
 */

public class SliderPagerAdapter extends PagerAdapter {

    private List<NewsPhoto> pics;
    private View.OnClickListener listener;

    public SliderPagerAdapter(List<NewsPhoto> pics, View.OnClickListener listener) {
        this.pics = pics;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return pics.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater  mLayoutInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = mLayoutInflater.inflate(R.layout.news_pager_item, container, false);

        SimpleDraweeView imageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);

        imageView.getHierarchy()
                .setPlaceholderImage(
                        container.getContext().getResources().getDrawable(R.drawable.ic_launcher),
                        ScalingUtils.ScaleType.FIT_CENTER);

        imageView.setImageURI(pics.get(position).getPhoto());

        imageView.setOnClickListener(view -> listener.onClick(view));
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}