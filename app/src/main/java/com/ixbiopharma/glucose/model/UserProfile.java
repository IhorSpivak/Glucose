package com.ixbiopharma.glucose.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;

/**
 * UserProfile
 *
 * Created by ivan on 17.05.17.
 */

@EqualsAndHashCode
public class UserProfile {

    @SerializedName("authorization_key")
    @Expose
    private String authorization_key;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("glucose_unit")
    @Expose
    private int glucoseUnit;
    @SerializedName("measurement_unit")
    @Expose
    private int measurementUnit;
    @SerializedName("enable_news")
    @Expose
    private int enableNews;
    @SerializedName("enable_tips")
    @Expose
    private int enableTips;
    @SerializedName("enable_notifications")
    @Expose
    private int enableNotifications;
    @SerializedName("save_photos")
    @Expose
    private int savePhotos;
    
    public boolean settingsEqual(UserProfile userProfile){
        if (userProfile.getEnableTips() != enableTips) return false;
        if (userProfile.getEnableNews() != enableNews) return false;
        if (userProfile.getEnableNotifications() != enableNotifications) return false;
        if (userProfile.getSavePhotos() != savePhotos) return false;
        if (userProfile.getGlucoseUnit() != glucoseUnit) return false;
        if (userProfile.getMeasurementUnit() != measurementUnit) return false;

        return true;
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getGlucoseUnit() {
        return glucoseUnit;
    }

    public void setGlucoseUnit(int glucoseUnit) {
        this.glucoseUnit = glucoseUnit;
    }

    public int getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(int measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public int getEnableNews() {
        return enableNews;
    }

    public void setEnableNews(int enableNews) {
        this.enableNews = enableNews;
    }

    public int getEnableTips() {
        return enableTips;
    }

    public void setEnableTips(int enableTips) {
        this.enableTips = enableTips;
    }

    public int getEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(int enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public int getSavePhotos() {
        return savePhotos;
    }

    public void setSavePhotos(int savePhotos) {
        this.savePhotos = savePhotos;
    }
}
