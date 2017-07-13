package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.ExerciseType;
import com.ixbiopharma.glucose.model.SyncArray;
import com.ixbiopharma.glucose.model.SyncData;
import com.ixbiopharma.glucose.repository.storage.AppPrefs;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ExerciseRepositoryImpl
 * <p>
 * Created by ivan on 6/12/17.
 */

public class ExerciseRepositoryImpl implements ExerciseRepository {

    private RealmHelper realmHelper;
    private AppPrefs appPrefs;
    private SyncAdapter syncAdapter;

    public ExerciseRepositoryImpl(RealmHelper realmHelper, AppPrefs appPrefs, SyncAdapter syncAdapter) {
        this.realmHelper = realmHelper;
        this.appPrefs = appPrefs;
        this.syncAdapter = syncAdapter;
    }

    @Override
    public List<ExerciseType> getExerciseList() {
        return realmHelper.getAll(ExerciseType.class);
    }

    @Override
    public Exercise getWalkingExercise() {
        return realmHelper.getExerciseById(appPrefs.getWalking_id());
    }

    @Override
    public void deleteExercise(Exercise dataType) {
        realmHelper.delete(dataType, Exercise.class);

        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        List<Exercise> glucoseList = new ArrayList<>();
        glucoseList.add(dataType);
        syncArray.setExerciseHistory(glucoseList);
        syncData.setDeletedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public List<Exercise> getAllActivity() {
        return realmHelper.getAll(Exercise.class);
    }

    @Override
    public void saveExercise(Exercise exercise) {
        realmHelper.save(exercise, Exercise.class);
        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        List<Exercise> glucoseList = new ArrayList<>();
        glucoseList.add(exercise);
        syncArray.setExerciseHistory(glucoseList);
        syncData.setUpdatedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public Exercise getExerciseById(int id) {
        return realmHelper.getExerciseById(id);
    }
}
