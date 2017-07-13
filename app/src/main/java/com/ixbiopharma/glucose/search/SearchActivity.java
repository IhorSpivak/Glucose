package com.ixbiopharma.glucose.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.News;
import com.ixbiopharma.glucose.news.detail.NewsDetailActivity;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SearchActivity
 * <p>
 * Created by ivan on 17.05.17.
 */

public class SearchActivity extends BaseActivity implements SearchContract.View {

    public static void start(Activity activity, String query) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra("query", query);
        activity.startActivity(intent);
    }

    private SearchContract.Presenter presenter;
    private NewsAdapter newsAdapter;
    private JournalItemsAdapter itemsAdapter;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.news_rv)
    RecyclerView newsRv;

    @BindView(R.id.item_rv)
    RecyclerView itemRv;

    @BindView(R.id.news)
    TextView news;

    @BindView(R.id.items)
    TextView items;

    @BindView(R.id.query)
    TextView query;

    @BindView(R.id.placeholder_rv1)
    View pv1;
    @BindView(R.id.placeholder_rv2)
    View pv2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        toolbarView.showHomeArrow(true);
        toolbarView.setTitle("Search");
        toolbarView.showAction(false);
        toolbarView.setHomeClickListener(v -> finish());

        newsRv.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        newsAdapter = new NewsAdapter(news ->
                NewsDetailActivity.start(SearchActivity.this, Integer.parseInt(news.getId())));
        newsRv.setAdapter(newsAdapter);

        newsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (newsAdapter.getItemCount() == 0) {
                    newsRv.setVisibility(View.GONE);
                    pv1.setVisibility(View.VISIBLE);
                } else {
                    newsRv.setVisibility(View.VISIBLE);
                    pv1.setVisibility(View.GONE);
                }
            }
        });

        itemRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        itemsAdapter = new JournalItemsAdapter();
        itemRv.setAdapter(itemsAdapter);
        itemsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (itemsAdapter.getItemCount() == 0) {
                    itemRv.setVisibility(View.GONE);
                    pv2.setVisibility(View.VISIBLE);
                } else {
                    itemRv.setVisibility(View.VISIBLE);
                    pv2.setVisibility(View.GONE);
                }
            }
        });

        presenter = new SearchPresenter(RepositoryComponent.provideNewsRepository(), this);
        presenter.search(getIntent().getStringExtra("query"));
        query.setText(getIntent().getStringExtra("query"));
    }

    @Override
    public void onNewsLoaded(List<News> newsList) {
        news.setText(String.format(Locale.getDefault(), "News (%d)", newsList.size()));
        newsAdapter.setData(newsList);
    }

    @Override
    public void onJournalLoaded(List<DataType> dataTypes) {
        itemsAdapter.setData(dataTypes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
