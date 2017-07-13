package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/**
 * PasswordSetRequest
 *
 * Created by ivan on 26.05.17.
 */

public class PasswordSetRequest {
    @SerializedName("authorization_key")
    @Expose
    String authorization_key;
    @SerializedName("password")
    @Expose
    String password;

    public PasswordSetRequest(String authorization_key, String password) {
        this.authorization_key = authorization_key;
        this.password = password;
    }
}
