package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixbiopharma.glucose.model.News;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * NewsWrapper
 *
 * Created by ivan on 17.05.17.
 */

public class NewsWrapper {

    @SerializedName("news")
    @Expose
    private List<News> news = new ArrayList<>();

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
