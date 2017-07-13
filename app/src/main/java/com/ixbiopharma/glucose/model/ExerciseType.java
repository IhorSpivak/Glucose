package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import lombok.Getter;
import lombok.Setter;

/**
 * ExerciseType
 *
 * Created by ivan on 22.05.17.
 */

@Getter
@Setter
@RealmClass
public class ExerciseType implements DataType {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("greenzone_min")
    @Expose
    private String greenzoneMin;
    @SerializedName("greenzone_max")
    @Expose
    private String greenzoneMax;
    @SerializedName("modify_date")
    @Expose
    private String modifyDate;

    @Override
    public int getDataType() {
        return 0;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getSubTitle() {
        return null;
    }
}
