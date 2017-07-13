package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixbiopharma.glucose.utils.TimeUtils;

import org.parceler.Parcel;

import java.text.ParseException;
import java.util.Date;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import lombok.AllArgsConstructor;

/**
 * Weight model
 * <p>
 * Created by ivan on 09.04.17.
 */

@Parcel
@AllArgsConstructor
@RealmClass
public class Weight implements DataType {

    public double value;

    @SerializedName("id_mob")
    @Expose
    @PrimaryKey
    private int id_mob = -1;

    @SerializedName("date")
    @Expose
    private String date;

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

    public Weight(){}

    @Override
    public int getDataType() {
        return Type.WIGHT;
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
    public double getValue() {
        return value;
    }

    @Override
    public String getSubTitle() {
        return "";
    }
}
