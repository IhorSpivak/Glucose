package com.ixbiopharma.glucose.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NewsAdapter
 * <p>
 * Created by ivan on 18.05.17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {

    private List<News> newsList = new ArrayList<>();
    private Listener listener;
    private Context context;

    public  NewsAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_search_news, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        News news = newsList.get(position);

        holder.imageView.getHierarchy()
                .setPlaceholderImage(
                        context.getResources().getDrawable(R.drawable.ic_launcher),
                        ScalingUtils.ScaleType.FIT_CENTER);

        if (!news.getPhotos().isEmpty()) {
            holder.imageView.setImageURI(news.getPhotos().get(0).getPhoto());
        }
        holder.title.setText(news.getHeader());
        holder.description.setText(news.getInfo());
        holder.view.setOnClickListener(v -> listener.onClick(news));
    }

    public void setData(List<News> newsList) {
        this.newsList.clear();
        this.newsList.addAll(newsList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        SimpleDraweeView imageView;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.container)
        View view;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Listener{
        void onClick(News news);
    }
}
