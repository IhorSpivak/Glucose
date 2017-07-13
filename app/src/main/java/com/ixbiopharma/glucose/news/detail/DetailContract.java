package com.ixbiopharma.glucose.news.detail;

import com.ixbiopharma.glucose.model.NewsDetail;

import java.util.List;

/**
 * DetailContract
 * <p>
 * Created by ivan on 15.05.17.
 */

interface DetailContract {
    interface View {
        void showNews(NewsDetail newsDetail);
        void showNext(String title, int id);
        void showPrev(String title, int id);
        void showRelated(List<NewsDetail> related);
        void showLoading();
    }

    interface Presenter {
        void getDetails(int id);
    }
}
