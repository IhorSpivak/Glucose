package com.ixbiopharma.glucose.news.detail;

import com.ixbiopharma.glucose.repository.NewsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * DetailPresenter
 * <p>
 * Created by ivan on 15.05.17.
 */

class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private NewsRepository repository;

    DetailPresenter(DetailContract.View view, NewsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getDetails(int id) {
        view.showLoading();
        repository.getNewsDetails(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(newsDetailWrapper -> {
                    view.showNews(newsDetailWrapper.getNews());

                    view.showNext(
                            newsDetailWrapper.getNext().getHeader(),
                            newsDetailWrapper.getNext().getId());

                    view.showPrev(
                            newsDetailWrapper.getPrevious().getHeader(),
                            newsDetailWrapper.getPrevious().getId());

                    view.showRelated(newsDetailWrapper.getRelated());
                }, Throwable::printStackTrace);
    }
}
