package com.ixbiopharma.glucose.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * JournalNewsWrapper
 *
 * Created by ivan on 27.05.17.
 */

public class JournalNewsWrapper {
    private List<News> news;

    public JournalNewsWrapper(List<News> news) {
        this.news = news;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
