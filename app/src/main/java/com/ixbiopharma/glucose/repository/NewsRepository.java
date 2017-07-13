package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.DateNews;
import com.ixbiopharma.glucose.model.News;
import com.ixbiopharma.glucose.model.NewsDetailWrapper;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

/**
 * NewsRepository
 *
 * Created by ivan on 14.05.17.
 */

public interface NewsRepository {

    Observable<List<News>> getNews(Date from, Date to);
    Observable<List<DateNews>> getDateNews(Date from, Date to);
    Observable<NewsDetailWrapper> getNewsDetails(int id);
    Observable<List<News>> search(String query);
}
