package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * LoginResponse
 *
 * Created by ivan on 14.05.17.
 */

@Getter
@Setter
public class LoginResponse {

    @SerializedName("authorization_key")
    @Expose
    private String authorization_key;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("user_id")
    @Expose
    private int user_id;
}
