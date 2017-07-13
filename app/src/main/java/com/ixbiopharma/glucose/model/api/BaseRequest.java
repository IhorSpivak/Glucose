package com.ixbiopharma.glucose.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixbiopharma.glucose.GlucoseApplication;

/**
 * BaseRequest
 *
 * Created by ivan on 15.05.17.
 */

public class BaseRequest {

    @SerializedName("device_type")
    @Expose
    String device_type = "android";
    @SerializedName("app_version")
    @Expose
    String app_version = GlucoseApplication.APP_VERSION;
    @SerializedName("os_version")
    @Expose
    String os_version = GlucoseApplication.OS_VERSION;
}
