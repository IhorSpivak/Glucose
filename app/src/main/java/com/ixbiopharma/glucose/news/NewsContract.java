package com.ixbiopharma.glucose.news;

import com.ixbiopharma.glucose.model.News;

import java.util.List;

/**
 * NewsContract
 *
 * Created by ivan on 14.05.17.
 */

interface NewsContract {
    interface View{
        void onNewsLoaded(List<News> news);
        void showLoading();
        void showError(String message);
    }

    interface Presenter{

    }
}
