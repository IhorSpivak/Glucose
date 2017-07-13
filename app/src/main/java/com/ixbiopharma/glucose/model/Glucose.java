package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ixbiopharma.glucose.utils.TimeUtils;

import org.parceler.Parcel;

import java.text.ParseException;
import java.util.Date;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Glucose
 * <p>
 * Created by ivan on 09.04.17.
 */

@Parcel
@RealmClass
public class Glucose implements DataType {

    @SerializedName("id_mob")
    @Expose
    @PrimaryKey
    private int id_mob = -1;

    private double value;
    public String meal;

    public Glucose() {
    }

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

    @Override
    public double getValue() {
        return value;
    }

    public double getMgDlValue() {
        return value * 18;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setMgDlValue(double value) {
        this.value = value / 18;
    }

    @Override
    public int getDataType() {
        return Type.GLUCOSE;
    }

    @Override
    public String getSubTitle() {
        return meal;
    }

    @Override
    public int getId() {
        return id_mob;
    }

    @Override
    public void setId(int id) {
        this.id_mob = id;
    }
}
