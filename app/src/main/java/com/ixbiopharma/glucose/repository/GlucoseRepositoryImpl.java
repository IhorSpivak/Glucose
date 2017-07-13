package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.model.SyncArray;
import com.ixbiopharma.glucose.model.SyncData;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;

/**
 * GlucoseRepositoryImpl
 *
 * Created by ivan on 6/12/17.
 */

public class GlucoseRepositoryImpl implements GlucoseRepository {

    private SyncAdapter syncAdapter;
    private RealmHelper realmHelper;

    public GlucoseRepositoryImpl(SyncAdapter syncAdapter, RealmHelper realmHelper) {
        this.syncAdapter = syncAdapter;
        this.realmHelper = realmHelper;
    }

    @Override
    public void saveGlucose(Glucose glucose) {
        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        List<Glucose> glucoseList = new ArrayList<>();
        glucoseList.add(realmHelper.save(glucose, Glucose.class));
        syncArray.setGlucoseHistory(glucoseList);
        syncData.setUpdatedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public List<Glucose> getAllGlucose() {
        return realmHelper.getAll(Glucose.class);
    }

    @Override
    public void deleteGlucose(Glucose dataType) {
        realmHelper.delete(dataType, Glucose.class);

        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        List<Glucose> glucoseList = new ArrayList<>();
        glucoseList.add(dataType);
        syncArray.setGlucoseHistory(glucoseList);
        syncData.setDeletedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public double getGlucoseLastValue() {
        return realmHelper.getLatItemValue(Glucose.class, 60);
    }

    @Override
    public DataType getGlucoseById(int id) {
        return realmHelper.getGlucoseById(id);
    }
}
