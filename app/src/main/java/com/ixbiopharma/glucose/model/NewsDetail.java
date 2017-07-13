package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * NewsDetail
 *
 * Created by ivan on 15.05.17.
 */

public class NewsDetail {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("photos")
    @Expose
    private List<NewsPhoto> photos = new ArrayList<>();
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("html")
    @Expose
    private String html;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("date")
    @Expose
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<NewsPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<NewsPhoto> photos) {
        this.photos = photos;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
