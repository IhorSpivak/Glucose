package com.ixbiopharma.glucose.repository;

import android.support.annotation.NonNull;

import com.ixbiopharma.glucose.api.ApiImpl;
import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.ExerciseType;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.FoodType;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.model.SyncArray;
import com.ixbiopharma.glucose.model.SyncData;
import com.ixbiopharma.glucose.model.Weight;
import com.ixbiopharma.glucose.repository.storage.AppPrefs;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * SyncAdapter
 * <p>
 * Created by ivan on 6/12/17.
 */

public class SyncAdapter {

    private RealmHelper realmHelper;
    private ApiImpl api;
    private AppPrefs appPrefs;

    public SyncAdapter(RealmHelper realmHelper, ApiImpl api, AppPrefs appPrefs) {
        this.realmHelper = realmHelper;
        this.api = api;
        this.appPrefs = appPrefs;
    }

    void syncInBackground(SyncData syncData) {
        syncData.setModify_date(appPrefs.getModifyDate());

        api.sync(appPrefs.getAuthToken(), syncData)
                .map(syncDataApi -> {

//   todo                 if (syncDataApi != null && syncDataApi.getUpdatedData() != null) {
//                        saveData(syncDataApi.getUpdatedData());
//                    }
//
//                    if (syncDataApi != null && syncDataApi.getDeletedData() != null) {
//                        deleteData(syncDataApi.getDeletedData());
//                    }

                    return syncDataApi;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(syncData1 ->
                                appPrefs.setModifyDate(
                                        TimeUtils.millsToApiString(System.currentTimeMillis())),
                        Throwable::printStackTrace);
    }

    private SyncData getUpdateSyncData() {
        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();

        List<Exercise> exercises = new ArrayList<>();
        exercises.addAll(realmHelper.getUpdateItems(Exercise.class));
        syncArray.setExerciseHistory(exercises);

        List<Food> foods = new ArrayList<>();
        foods.addAll(realmHelper.getUpdateItems(Food.class));
        syncArray.setFoodHistory(foods);

        syncData.setUpdatedData(syncArray);

        return syncData;
    }

    Observable<SyncData> sync() {
        SyncData syncData = getUpdateSyncData();

        syncData.setModify_date(appPrefs.getModifyDate());

        return api.sync(appPrefs.getAuthToken(), syncData)
                .doFinally(() -> appPrefs.setModifyDate(
                        TimeUtils.millsToApiString(System.currentTimeMillis())))
                .map(syncDataApi -> {

                    if (syncDataApi != null && syncDataApi.getUpdatedData() != null) {
                        saveData(syncDataApi.getUpdatedData());
                    }

                    if (syncDataApi != null && syncDataApi.getDeletedData() != null) {
                        deleteData(syncDataApi.getDeletedData());
                    }

                    realmHelper.clearMustUpdateItems();

                    return syncDataApi;
                });
    }

    private void deleteData(@NonNull SyncArray syncArray) {
        if (syncArray.getGlucoseHistory() != null) {

            for (int i = 0; i < syncArray.getGlucoseHistory().size(); i++) {
                Glucose glucose = syncArray.getGlucoseHistory().get(i);
                realmHelper.remove(glucose, Glucose.class);
            }
        }

        if (syncArray.getWeightHistory() != null) {

            for (int i = 0; i < syncArray.getWeightHistory().size(); i++) {
                Weight weight = syncArray.getWeightHistory().get(i);
                realmHelper.remove(weight, Weight.class);
            }
        }

        if (syncArray.getFoodHistory() != null) {

            for (int i = 0; i < syncArray.getFoodHistory().size(); i++) {
                Food food = syncArray.getFoodHistory().get(i);
                realmHelper.remove(food, Food.class);
            }
        }

        if (syncArray.getExerciseHistory() != null) {

            for (int i = 0; i < syncArray.getExerciseHistory().size(); i++) {
                Exercise exercise = syncArray.getExerciseHistory().get(i);
                realmHelper.remove(exercise, Exercise.class);
            }
        }
    }

    private void saveData(@NonNull SyncArray syncArray) {

        if (syncArray.getGlucoseHistory() != null) {
            for (int i = 0; i < syncArray.getGlucoseHistory().size(); i++) {
                Glucose glucose = syncArray.getGlucoseHistory().get(i);
                realmHelper.save(glucose, Glucose.class);
            }
        }

        if (syncArray.getWeightHistory() != null) {

            for (int i = 0; i < syncArray.getWeightHistory().size(); i++) {
                Weight weight = syncArray.getWeightHistory().get(i);
                realmHelper.save(weight, Weight.class);
            }
        }

        if (syncArray.getFoodHistory() != null) {

            for (int i = 0; i < syncArray.getFoodHistory().size(); i++) {
                Food food = syncArray.getFoodHistory().get(i);
                realmHelper.save(food, Food.class);
            }
        }

        if (syncArray.getExerciseHistory() != null) {
            for (int i = 0; i < syncArray.getExerciseHistory().size(); i++) {
                Exercise exercise = syncArray.getExerciseHistory().get(i);
                realmHelper.save(exercise, Exercise.class);
            }
        }

        if (syncArray.getExerciseTypes() != null) {
            for (int i = 0; i < syncArray.getExerciseTypes().size(); i++) {
                ExerciseType exerciseType = syncArray.getExerciseTypes().get(i);
                realmHelper.save(exerciseType, ExerciseType.class);
            }
        }

        if (syncArray.getFoodTypes() != null) {
            for (int i = 0; i < syncArray.getFoodTypes().size(); i++) {
                FoodType foodType = syncArray.getFoodTypes().get(i);
                realmHelper.save(foodType, FoodType.class);
            }
        }
    }
}