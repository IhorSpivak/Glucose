package com.ixbiopharma.glucose.search;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.News;

import java.util.List;

/**
 * SearchContract
 *
 * Created by ivan on 17.05.17.
 */

interface SearchContract {
    interface View{
        void onNewsLoaded(List<News> newsList);
        void onJournalLoaded(List<DataType> dataTypes);
    }

    interface Presenter{
        void search(String query);

        void destroy();
    }
}
