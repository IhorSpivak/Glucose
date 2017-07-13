package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * NewsRequest
 *
 * Created by ivan on 15.05.17.
 */

public class NewsRequest {

    @SerializedName("authorization_key")
    @Expose
    String authorization_key;

    @SerializedName("from")
    @Expose
    String from;

    @SerializedName("to")
    @Expose
    String to;

    public NewsRequest(String authorization_key, String from, String to) {
        this.authorization_key = authorization_key;
        this.from = from;
        this.to = to;
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
