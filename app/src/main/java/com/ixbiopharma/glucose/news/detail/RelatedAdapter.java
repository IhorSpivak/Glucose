package com.ixbiopharma.glucose.news.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.NewsDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NewsListAdapter
 * <p>
 * Created by ivan on 07.05.17.
 */

class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.VH> {

    private Listener listener;
    private List<NewsDetail> news = new ArrayList<>();

    RelatedAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_news_related_item, parent, false);

        return new VH(v);
    }

    void setData(List<NewsDetail> related) {
        this.news.clear();
        this.news.addAll(related);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        NewsDetail newsItem = news.get(position);
        holder.itemView.setOnClickListener(v -> listener.onClick(newsItem));
        holder.title.setText(String.format(Locale.getDefault(), "%d. %s", position + 1, newsItem.getHeader()));
        holder.subtitle.setText(String.format("%s ? %s", newsItem.getDate(), newsItem.getAuthor()));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.subtitle)
        TextView subtitle;

        VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Listener {

        void onClick(NewsDetail news);
    }
}
