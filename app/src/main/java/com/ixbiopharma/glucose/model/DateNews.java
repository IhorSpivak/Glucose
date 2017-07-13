package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * DateNews
 *
 * Created by ivan on 15.05.17.
 */

@EqualsAndHashCode
@ToString
public class DateNews {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("advice_header")
    @Expose
    private String advice_header;
    @SerializedName("advice_description")
    @Expose
    private String advice_description;
    @SerializedName("news")
    @Expose
    private List<News> news = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdvice_header() {
        return advice_header;
    }

    public void setAdvice_header(String advice_header) {
        this.advice_header = advice_header;
    }

    public String getAdvice_description() {
        return advice_description;
    }

    public void setAdvice_description(String advice_description) {
        this.advice_description = advice_description;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
