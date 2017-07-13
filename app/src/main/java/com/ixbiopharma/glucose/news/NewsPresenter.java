package com.ixbiopharma.glucose.news;

import com.ixbiopharma.glucose.repository.NewsRepository;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * NewsPresenter
 * <p>
 * Created by ivan on 14.05.17.
 */

class NewsPresenter implements NewsContract.Presenter {

    private NewsContract.View view;
    private NewsRepository newsRepository;

    NewsPresenter(NewsContract.View view, NewsRepository repository) {
        this.view = view;
        this.newsRepository = repository;
        Date currentDate = new Date();
        loadNews(currentDate, TimeUtils.getPrevYearDate(currentDate));
    }

    private void loadNews(Date to, Date from) {
        view.showLoading();

        newsRepository.getNews(from, to)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(response -> view.onNewsLoaded(response), t->{
                    t.printStackTrace();
                    view.showError("No data!");
                });
    }
}
