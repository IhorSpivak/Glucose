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
 * Food
 * <p>
 * Created by ivan on 13.04.17.
 */

@AllArgsConstructor
@RealmClass
public class Food implements DataType {

    public String uri;

    public Food(String name) {
        this.name = name;
    }

    @SerializedName("id_mob")
    @Expose
    @PrimaryKey
    private Integer id_mob = -1;
//    @SerializedName("modify_date")
//    @Expose
//    private String modify_date;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private Integer type = 1;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("feeling")
    @Expose
    private Integer feeling = 1;
    @SerializedName("pre_glucose")
    @Expose
    private Integer preGlucose = -1;
    @SerializedName("post_glucose")
    @Expose
    private Integer postGlucose = -1;
    @SerializedName("energy")
    @Expose
    private Double energy = 0d;
    @SerializedName("fibre")
    @Expose
    private Double fibre = 0d;
    @SerializedName("sodium")
    @Expose
    private Double sodium = 0d;

    private boolean mustUpdate = false;

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    @Override
    public int getDataType() {
        return Type.FOOD;
    }

    public Food() {
    }

    @Override
    public int getId() {
        return id_mob;
    }

    @Override
    public void setId(int id) {
        id_mob = id;
    }

    @Override
    public Date getDate() {
        try {
            return TimeUtils.apiStringToDate2(date);
        } catch (ParseException e) {
            return TimeUtils.apiStringToDate(date);
        }
    }

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    public String getSubTitle() {
        return "";
    }

    public void setDate(long mills) {
        date = TimeUtils.millsToApiString(mills);
    }

    public static Food fromType(FoodType food) {
        Food food1 = new Food();
        food1.setName(food.getName());
        food1.setFibre(food.getFibre());
        food1.setEnergy(food.getEnergy());
        food1.setSodium(food.getSodium());
        return food1;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getId_mob() {
        return id_mob;
    }

    public void setId_mob(Integer id_mob) {
        this.id_mob = id_mob;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFeeling() {
        return feeling;
    }

    public void setFeeling(Integer feeling) {
        this.feeling = feeling;
    }

    public Integer getPreGlucose() {
        return preGlucose;
    }

    public void setPreGlucose(Integer preGlucose) {
        this.preGlucose = preGlucose;
    }

    public Integer getPostGlucose() {
        return postGlucose;
    }

    public void setPostGlucose(Integer postGlucose) {
        this.postGlucose = postGlucose;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public double getFibre() {
        return fibre;
    }

    public void setFibre(Double fibre) {
        this.fibre = fibre;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }
}
