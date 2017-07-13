package com.ixbiopharma.glucose.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.News;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * NewsListAdapter
 * <p>
 * Created by ivan on 07.05.17.
 */

class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.VH> {

    private final int HEADER = 0;
    private final int ITEM = 1;
    private Listener listener;
    private List<News> news = new ArrayList<>();
    private Context context;

    NewsListAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v;
        if (viewType == ITEM) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_news_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_news_header_item, parent, false);
        }
        return new VH(v);
    }

    void setData(List<News> news) {
        this.news.clear();
        this.news.addAll(news);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        News newsItem = news.get(position);

        holder.container.setOnClickListener(v -> listener.onClick(newsItem));

        if (getItemViewType(position) == HEADER) {
            holder.viewPager.setAdapter(
                    new SliderPagerAdapter(newsItem.getPhotos(),
                            (View.OnClickListener) view -> listener.onClick(newsItem)));
            
            holder.viewPager.setOnClickListener(v -> listener.onClick(newsItem));
            holder.circleIndicator.setViewPager(holder.viewPager);
            holder.circleIndicator.setViewPager(holder.viewPager);
            holder.title.setText(newsItem.getHeader());
            holder.date.setText(formatDate(newsItem.getDate()));
            holder.description.setText(newsItem.getInfo());
        } else {
            holder.title.setText(newsItem.getHeader());
            holder.date.setText(formatDate(newsItem.getDate()));
            if (holder.simpleDraweeView != null) {

                holder.simpleDraweeView.getHierarchy()
                        .setPlaceholderImage(
                                context.getResources().getDrawable(R.drawable.ic_launcher),
                                ScalingUtils.ScaleType.FIT_CENTER);

                if (!newsItem.getPhotos().isEmpty()) {
                    holder.simpleDraweeView.setImageURI(newsItem.getPhotos().get(0).getPhoto());
                }
            }
        }
    }

    private String formatDate(String date) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yyyy");
        String result = "";
        try {

            result = myFormat.format(fromUser.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? HEADER : ITEM;
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.viewPager)
        @Nullable
        ViewPager viewPager;

        @BindView(R.id.indicator)
        @Nullable
        CircleIndicator circleIndicator;

        @BindView(R.id.description)
        @Nullable
        TextView description;

        @BindView(R.id.icon)
        @Nullable
        SimpleDraweeView simpleDraweeView;

        @BindView(R.id.container)
        View container;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.title)
        TextView title;

        VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Listener {

        void onClick(News news);
    }
}
