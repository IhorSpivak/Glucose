package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.FoodType;
import com.ixbiopharma.glucose.model.SyncArray;
import com.ixbiopharma.glucose.model.SyncData;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * FoodRepositoryImpl
 * <p>
 * Created by ivan on 6/12/17.
 */

public class FoodRepositoryImpl implements FoodRepository {

    private RealmHelper realmHelper;
    private SyncAdapter syncAdapter;

    public FoodRepositoryImpl(RealmHelper realmHelper, SyncAdapter syncAdapter) {
        this.realmHelper = realmHelper;
        this.syncAdapter = syncAdapter;
    }

    @Override
    public void saveFood(List<Food> list) {

        for (int i = 0; i < list.size(); i++) {
            realmHelper.save(list.get(i), Food.class);
        }

        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        syncArray.setFoodHistory(list);
        syncData.setUpdatedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public void deleteFood(Food dataType) {
        realmHelper.delete(dataType, Food.class);

        SyncData syncData = new SyncData();
        SyncArray syncArray = new SyncArray();
        List<Food> glucoseList = new ArrayList<>();
        glucoseList.add(dataType);
        syncArray.setFoodHistory(glucoseList);
        syncData.setDeletedData(syncArray);

        syncAdapter.syncInBackground(syncData);
    }

    @Override
    public List<FoodType> getAllFoodTypes() {
        return realmHelper.getAll(FoodType.class);
    }

    @Override
    public List<Food> getAllFood() {
        return realmHelper.getAll(Food.class);
    }

}
