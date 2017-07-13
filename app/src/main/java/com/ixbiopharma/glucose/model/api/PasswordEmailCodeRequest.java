package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/**
 * PasswordEmailCodeRequest
 *
 * Created by ivan on 26.05.17.
 */

@AllArgsConstructor
public class PasswordEmailCodeRequest {
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("password")
    @Expose
    String password;
}
