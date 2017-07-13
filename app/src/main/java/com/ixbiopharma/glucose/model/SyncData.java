package com.ixbiopharma.glucose.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixbiopharma.glucose.model.api.Response;

/**
 * SyncData
 * <p>
 * Created by ivan on 21.05.17.
 */

public class SyncData extends Response {
    @SerializedName("authorization_key")
    @Expose
    private String authorization_key;
    @SerializedName("updated_data")
    @Expose
    @Nullable
    private SyncArray updatedData;
    @SerializedName("deleted_data")
    @Expose
    @Nullable
    private SyncArray deletedData;
    @SerializedName("modify_date")
    @Expose
    private String modify_date;

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    @Nullable
    public SyncArray getUpdatedData() {
        return updatedData;
    }

    public void setUpdatedData(@Nullable SyncArray updatedData) {
        this.updatedData = updatedData;
    }

    @Nullable
    public SyncArray getDeletedData() {
        return deletedData;
    }

    public void setDeletedData(@Nullable SyncArray deletedData) {
        this.deletedData = deletedData;
    }

    @Nullable
    public String getModify_date() {
        return modify_date;
    }

    public void setModify_date(@Nullable String modify_date) {
        this.modify_date = modify_date;
    }
}
