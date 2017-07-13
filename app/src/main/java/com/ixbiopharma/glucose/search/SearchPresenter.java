package com.ixbiopharma.glucose.search;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.repository.NewsRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * SearchPresenter
 * <p>
 * Created by ivan on 17.05.17.
 */

class SearchPresenter implements SearchContract.Presenter {

    private NewsRepository repository;
    private SearchContract.View view;
    private CompositeDisposable compositeDisposable;

    SearchPresenter(NewsRepository repository, SearchContract.View view) {
        this.repository = repository;
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void destroy() {
        compositeDisposable.dispose();
    }

    @Override
    public void search(String query) {

        compositeDisposable.add(
                repository.search(query)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(searchNews -> view.onNewsLoaded(searchNews), Throwable::printStackTrace)
        );

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(realm1 -> {
            List<DataType> dataTypes = new ArrayList<>();
            RealmResults<Food> realmResults1 = realm1.where(Food.class).contains("name", query, Case.INSENSITIVE).findAll();
            RealmResults<Exercise> realmResults2 = realm1.where(Exercise.class).contains("exercise", query, Case.INSENSITIVE).findAll();

            dataTypes.addAll(realm1.copyFromRealm(realmResults1));
            dataTypes.addAll(realm1.copyFromRealm(realmResults2));

            view.onJournalLoaded(dataTypes);
        });
    }
}
