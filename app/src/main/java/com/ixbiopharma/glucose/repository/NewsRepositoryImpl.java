package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.api.ApiImpl;
import com.ixbiopharma.glucose.model.DateNews;
import com.ixbiopharma.glucose.model.News;
import com.ixbiopharma.glucose.model.NewsDetailWrapper;
import com.ixbiopharma.glucose.repository.storage.AppPrefs;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

/**
 * NewsRepositoryImpl
 * <p>
 * Created by ivan on 14.05.17.
 */

public class NewsRepositoryImpl implements NewsRepository {

    private ApiImpl api;
    private AppPrefs prefs;

    public NewsRepositoryImpl(ApiImpl api, AppPrefs prefs) {
        this.api = api;
        this.prefs = prefs;
    }

    @Override
    public Observable<List<DateNews>> getDateNews(Date from, Date to) {
        return api.getNewsAndAdvice(prefs.getAuthToken(), from, to);
    }

    @Override
    public Observable<List<News>> getNews(Date from, Date to) {
        return api.getNewsList(prefs.getAuthToken(), from, to);
    }

    @Override
    public Observable<NewsDetailWrapper> getNewsDetails(int id) {
        return api.getNewsDetails(prefs.getAuthToken(), id);
    }

    @Override
    public Observable<List<News>> search(String query) {
        return api.search(prefs.getAuthToken(), query);
    }
}