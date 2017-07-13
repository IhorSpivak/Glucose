package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * NewsDetailRequest
 *
 * Created by ivan on 15.05.17.
 */

public class NewsDetailRequest {

    @SerializedName("authorization_key")
    @Expose
    String authorization_key;

    @SerializedName("id")
    @Expose
    int id;

    public NewsDetailRequest(String authorization_key, int id) {
        this.authorization_key = authorization_key;
        this.id = id;
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
