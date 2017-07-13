package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.text.ParseException;
import java.util.Date;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import lombok.AllArgsConstructor;

/**
 * Exercise
 * Created by ivan on 10.04.17.
 */

@AllArgsConstructor
@RealmClass
public class Exercise implements DataType {

    @SerializedName("id_mob")
    @Expose
    @PrimaryKey
    private int id_mob = -1;

    @SerializedName("name")
    @Expose
    public String exercise;

    public double value;

    @SerializedName("date")
    @Expose
    private String date;

    private boolean mustUpdate = false;

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    public Exercise() {
    }

    @Override
    public Date getDate() {
        try {
            return TimeUtils.apiStringToDate2(date);
        } catch (ParseException e) {
            return TimeUtils.apiStringToDate(date);
        }
    }

    public void setDate(long mills) {
        date = TimeUtils.millsToApiString(mills);
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String getSubTitle() {
        return exercise;
    }

    @Override
    public int getId() {
        return id_mob;
    }

    @Override
    public void setId(int id) {
        this.id_mob = id;
    }

    @Override
    public int getDataType() {
        return Type.EXERCISE;
    }
}



