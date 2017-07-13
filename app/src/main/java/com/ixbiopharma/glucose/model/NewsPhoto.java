package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * NewsPhoto
 *
 * Created by ivan on 15.05.17.
 */

public class NewsPhoto {

    @SerializedName("photo")
    @Expose
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
