package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * SearchRequest
 *
 * Created by ivan on 17.05.17.
 */

public class SearchRequest {

    @SerializedName("authorization_key")
    @Expose
    private
    String authorization_key;
    @SerializedName("search")
    @Expose
    private
    String search;

    public SearchRequest(String authorization_key, String search) {
        this.authorization_key = authorization_key;
        this.search = search;
    }

    public SearchRequest() {
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
