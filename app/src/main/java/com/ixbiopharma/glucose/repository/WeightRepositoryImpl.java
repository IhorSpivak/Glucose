package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.SyncArray;
import com.ixbiopharma.glucose.model.SyncData;
import com.ixbiopharma.glucose.model.Weight;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * WeightRepositoryImpl
 * <p>
 * Created by ivan on 6/12/17.
 */

public class WeightRepositoryImpl implements WeightRepository {

    private SyncAdapter syncAdapter;
    private RealmHelper realmHelper;

    public WeightRepositoryImpl(SyncAdapter syncAdapter, RealmHelper realmHelper) {
        this.syncAdapter = syncAdapter;
        this.realmHelper = realmHelper;
    }

    @Override
    public void saveWeight(Weight weight) {
        realmHelper.save(weight, Weight.class);

        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        List<Weight> glucoseList = new ArrayList<>();
        glucoseList.add(weight);
        syncArray.setWeightHistory(glucoseList);
        syncData.setUpdatedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public List<Weight> getAllWeight() {
        return realmHelper.getAll(Weight.class);
    }

    @Override
    public void deleteWeight(Weight dataType) {
        realmHelper.delete(dataType, Weight.class);

        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        List<Weight> glucoseList = new ArrayList<>();
        glucoseList.add(dataType);
        syncArray.setWeightHistory(glucoseList);
        syncData.setDeletedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public double getWeightLastValue() {
        return realmHelper.getLatItemValue(Weight.class, 60);
    }

    @Override
    public DataType getWeightById(int id) {
        return realmHelper.getWeightById(id);
    }
}