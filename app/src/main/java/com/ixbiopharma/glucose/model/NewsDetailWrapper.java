package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * NewsDetailWrapper
 *
 * Created by ivan on 15.05.17.
 */

public class NewsDetailWrapper {

    @SerializedName("news")
    @Expose
    private NewsDetail news;
    @SerializedName("related")
    @Expose
    private List<NewsDetail> related = new ArrayList<>();
    @SerializedName("previous")
    @Expose
    private NewsDetail previous;
    @SerializedName("next")
    @Expose
    private NewsDetail next;

    public NewsDetail getNews() {
        return news;
    }

    public void setNews(NewsDetail news) {
        this.news = news;
    }

    public List<NewsDetail> getRelated() {
        return related;
    }

    public void setRelated(List<NewsDetail> related) {
        this.related = related;
    }

    public NewsDetail getPrevious() {
        return previous;
    }

    public void setPrevious(NewsDetail previous) {
        this.previous = previous;
    }

    public NewsDetail getNext() {
        return next;
    }

    public void setNext(NewsDetail next) {
        this.next = next;
    }
}
