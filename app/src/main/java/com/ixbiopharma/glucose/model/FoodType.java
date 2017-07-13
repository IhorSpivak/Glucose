package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * FoodType
 * <p>
 * Created by ivan on 22.05.17.
 */

@RealmClass
public class FoodType implements DataType {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("energy")
    @Expose
    private Double energy;
    @SerializedName("fibre")
    @Expose
    private Double fibre;
    @SerializedName("sodium")
    @Expose
    private Double sodium;

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
    public String getSubTitle() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
