package com.ixbiopharma.glucose.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.TabDelegate;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.menu.MenuResultDelegate;
import com.ixbiopharma.glucose.model.News;
import com.ixbiopharma.glucose.news.detail.NewsDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NewsListActivity
 * <p>
 * Created by ivan on 07.05.17.
 */

public class NewsListActivity extends BaseActivity
        implements NewsListAdapter.Listener, NewsContract.View {

    @BindView(R.id.newsList)
    RecyclerView newsList;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.tab)
    TabLayout tabLayout;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, NewsListActivity.class));
    }

    private NewsListAdapter newsListAdapter;
    private NewsContract.Presenter presenter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this);
        newsList.setLayoutManager(layoutManager);
        newsListAdapter = new NewsListAdapter(this);
        newsList.setAdapter(newsListAdapter);


        toolbarView.showHomeArrow(true);
        toolbarView.setHomeArrowIcon(R.drawable.ic_menu);
        toolbarView.showAction(false);
        toolbarView.setHomeClickListener(v -> showMenu(2));
        toolbarView.setTitle("News");

        new TabDelegate().init(tabLayout, position ->
                MenuResultDelegate.handleMenuResult(
                        NewsListActivity.this,
                        position), 2);

        presenter = new NewsPresenter(this, RepositoryComponent.provideNewsRepository());
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void onNewsLoaded(List<News> news) {
        progressDialog.hide();
        newsListAdapter.setData(news);
    }

    @Override
    public void onClick(News news) {
        NewsDetailActivity.start(this, Integer.parseInt(news.getId()));
    }

    @Override
    public void showError(String message) {
        progressDialog.hide();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
