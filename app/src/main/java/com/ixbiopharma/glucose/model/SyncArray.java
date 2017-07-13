package com.ixbiopharma.glucose.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * SyncArray
 *
 * Created by ivan on 21.05.17.
 */

public class SyncArray {

    @SerializedName("glucose_history")
    @Expose
    @Nullable
    private List<Glucose> glucoseHistory;
    @SerializedName("weight_history")
    @Expose
    @Nullable
    private List<Weight> weightHistory;
    @SerializedName("exercise_history")
    @Expose
    @Nullable
    private List<Exercise> exerciseHistory;
    @SerializedName("food_history")
    @Expose
    @Nullable
    private List<Food> foodHistory;
    @SerializedName("exercise_types")
    @Expose
    private List<ExerciseType> exerciseTypes = null;
    @SerializedName("food_types")
    @Expose
    private List<FoodType> foodTypes = null;

    @Nullable
    public List<Glucose> getGlucoseHistory() {
        return glucoseHistory;
    }

    public void setGlucoseHistory(@Nullable List<Glucose> glucoseHistory) {
        this.glucoseHistory = glucoseHistory;
    }

    @Nullable
    public List<Weight> getWeightHistory() {
        return weightHistory;
    }

    public void setWeightHistory(@Nullable List<Weight> weightHistory) {
        this.weightHistory = weightHistory;
    }

    @Nullable
    public List<Exercise> getExerciseHistory() {
        return exerciseHistory;
    }

    public void setExerciseHistory(@Nullable List<Exercise> exerciseHistory) {
        this.exerciseHistory = exerciseHistory;
    }

    @Nullable
    public List<Food> getFoodHistory() {
        return foodHistory;
    }

    public void setFoodHistory(@Nullable List<Food> foodHistory) {
        this.foodHistory = foodHistory;
    }

    public List<ExerciseType> getExerciseTypes() {
        return exerciseTypes;
    }

    public void setExerciseTypes(List<ExerciseType> exerciseTypes) {
        this.exerciseTypes = exerciseTypes;
    }

    public List<FoodType> getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(List<FoodType> foodTypes) {
        this.foodTypes = foodTypes;
    }
}
