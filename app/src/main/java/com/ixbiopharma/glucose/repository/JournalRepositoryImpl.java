package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.api.ApiImpl;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.model.Weight;
import com.ixbiopharma.glucose.model.api.DayAdviceResponse;
import com.ixbiopharma.glucose.repository.storage.AppPrefs;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * JournalRepositoryImpl
 * <p>
 * Created by ivan on 21.05.17.
 */

public class JournalRepositoryImpl implements JournalRepository {

    private RealmHelper realmHelper;
    private ApiImpl api;
    private AppPrefs appPrefs;
    private SyncAdapter syncAdapter;

    public JournalRepositoryImpl(RealmHelper realmHelper,
                                 ApiImpl api,
                                 AppPrefs appPrefs,
                                 SyncAdapter syncAdapter) {

        this.realmHelper = realmHelper;
        this.api = api;
        this.appPrefs = appPrefs;
        this.syncAdapter = syncAdapter;
    }

    @Override
    public List<DataType> getJournalSliderLastItems() {
        List<DataType> dataList = new ArrayList<>();

        Glucose last1 = realmHelper.getLastItem(Glucose.class);
        if (last1 != null){
            dataList.add(last1);
        }
        Weight last2 = realmHelper.getLastItem(Weight.class);
        if (last2 != null){
            dataList.add(last2);
        }
        Exercise last3 = realmHelper.getLastItem(Exercise.class);
        if (last3 != null){
            dataList.add(last3);
        }

        return dataList;
    }

    @Override
    public List<DataType> getJournalDataList() {
        List<DataType> dataList = new ArrayList<>();
        dataList.addAll(realmHelper.getAll(Glucose.class));
        dataList.addAll(realmHelper.getAll(Exercise.class));
        dataList.addAll(realmHelper.getAll(Food.class));
        dataList.addAll(realmHelper.getAll(Weight.class));
        return dataList;
    }

    @Override
    public Observable<DayAdviceResponse> getDayAdvice() {
        return api.getDayAdvice(appPrefs.getAuthToken());
    }

    @Override
    public Completable sync() {
        return Completable.fromObservable(
                syncAdapter.sync())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }

    @Override
    public Date getLastWalkingUpdateDate() {
        return new Date(appPrefs.getLastWalking());
    }

    @Override
    public void setLastWalkingUpdate(long l) {
        appPrefs.setLastWalking(l);
    }

}
