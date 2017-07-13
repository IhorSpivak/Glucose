package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * UserProfileRequest
 *
 * Created by ivan on 17.05.17.
 */

public class UserProfileRequest {

    @SerializedName("authorization_key")
    @Expose
    String authorization_key;

    public UserProfileRequest(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }
}
