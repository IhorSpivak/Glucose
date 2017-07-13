package com.ixbiopharma.glucose.news.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.model.NewsDetail;
import com.ixbiopharma.glucose.news.SliderPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NewsDetailActivity
 * <p>
 * Created by ivan on 12.05.17.
 */

public class NewsDetailActivity extends BaseActivity implements DetailContract.View {

    private static final String ID = "id";

    public static void start(Context activity, int id) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra(ID, id);
        activity.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.prev)
    View prev;
    @BindView(R.id.next)
    View next;
    @BindView(R.id.prev_title)
    TextView prevT;
    @BindView(R.id.next_title)
    TextView nextT;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private DetailContract.Presenter presenter;
    private RelatedAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        toolbarView.showHomeArrow(true);
        toolbarView.setHomeArrowIcon(R.drawable.ic_back);
        toolbarView.showAction(true);
        toolbarView.setActionTitle("Share");
        toolbarView.setActionOnClickListener(v -> {
            //todo share
        });

        toolbarView.setHomeClickListener(v -> finish());
        toolbarView.setTitle("News");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new DetailPresenter(this, RepositoryComponent.provideNewsRepository());

        int newsID = getIntent().getIntExtra(ID, 0);
        presenter.getDetails(newsID);

        adapter = new RelatedAdapter(news -> presenter.getDetails(news.getId()));

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void showNews(NewsDetail newsDetail) {
        progressDialog.hide();
        viewPager.setAdapter(new SliderPagerAdapter(newsDetail.getPhotos(), view -> {
        }));
        title.setText(newsDetail.getHeader());
        date.setText((newsDetail.getDate()));
        webView.loadData(newsDetail.getHtml(), "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public void showNext(String title, int id) {
        nextT.setText(title);
        next.setOnClickListener(v -> presenter.getDetails(id));
    }

    @Override
    public void showPrev(String title, int id) {
        prevT.setText(title);
        prev.setOnClickListener(v -> presenter.getDetails(id));
    }

    @Override
    public void showRelated(List<NewsDetail> related) {
        adapter.setData(related);
    }
}
