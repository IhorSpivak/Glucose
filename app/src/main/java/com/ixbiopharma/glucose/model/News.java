package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

/**
 * News
 * <p>
 * Created by ivan on 14.05.17.
 */

@ToString
public class News {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("photos")
    @Expose
    private List<NewsPhoto> photos = new ArrayList<>();
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("date")
    @Expose
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
