package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/**
 * DayAdviceRequest
 *
 * Created by ivan on 25.05.17.
 */

public class DayAdviceRequest {

    @SerializedName("authorization_key")
    @Expose
    private
    String authorization_key;

    public DayAdviceRequest(String authorization_key) {
        this.authorization_key = authorization_key;
    }
}
