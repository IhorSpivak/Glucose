package com.ixbiopharma.glucose.repository.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.model.Weight;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Realm helper class
 * <p>
 * Created by ivan on 11.04.17.
 */

public class RealmHelper {

    public <T extends RealmModel> List<T> getAll(Class<T> type) {
        return Realm.getDefaultInstance().copyFromRealm(
                Realm.getDefaultInstance().where(type).findAll());
    }

    private <T extends RealmModel> int generateId(Class<T> type) {
        int key;

        try (Realm realm = Realm.getDefaultInstance()) {
            key = realm.where(type).max("id_mob").intValue() + 1;
        } catch (Exception e) {
            key = 1;
        }

        return key;
    }

    public <T extends DataType> void save(@NonNull List<T> data, Class<T> type) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(data));
        realm.close();
    }

    public <T extends DataType> T save(@NonNull T data, Class<T> type) {

        if (data.getId() == -1) {
            data.setId(generateId(type));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(data));
        realm.close();

        return data;
    }

    public <T extends DataType & RealmModel> void remove(@NonNull T data, Class<T> type) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<T> realmResults = realm1.where(type).equalTo("id_mob", data.getId()).findAll();
            realmResults.deleteAllFromRealm();
        });
        realm.close();
    }

    @Nullable
    public <T extends RealmModel> T getLastItem(Class<T> type) {
        T item;
        try (Realm realm = Realm.getDefaultInstance()) {
            T r = realm.where(type).findAll().last();
            item = realm.copyFromRealm(r);
        } catch (Exception e) {
            item = null;
        }

        return item;
    }

    @Nullable
    public Exercise getExerciseById(int id) {
        Exercise item;
        try (Realm realm = Realm.getDefaultInstance()) {
            Exercise r = realm.where(Exercise.class).equalTo("id_mob", id).findFirst();
            item = realm.copyFromRealm(r);
        } catch (Exception e) {
            item = null;
        }

        return item;
    }

    public <T extends DataType> double getLatItemValue(Class<T> type, double defaultValue) {
        T item = getLastItem(type);
        if (item == null) {
            return defaultValue;
        }
        return item.getValue();
    }

    public <T extends DataType & RealmModel> List<T> getUpdateItems(Class<T> type) {
        List<T> items = new ArrayList<>();

        try (Realm realm = Realm.getDefaultInstance()) {
            List<T> realmResults = realm.where(type).equalTo("mustUpdate", true).findAll();
            items.addAll(realm.copyFromRealm(realmResults));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public void removeAllData() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.deleteAll());
        realm.close();
    }

    public <T extends DataType & RealmModel> void delete(@NonNull T data, Class<T> type) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<T> realmResults = realm1.where(type).equalTo("id_mob", data.getId()).findAll();
            realmResults.deleteFirstFromRealm();
        });
        realm.close();
    }

    @Nullable
    public Glucose getGlucoseById(Integer id) {
        if (id == null) return null;

        Glucose item;
        try (Realm realm = Realm.getDefaultInstance()) {
            Glucose r = realm.where(Glucose.class).equalTo("id_mob", id).findFirst();
            item = realm.copyFromRealm(r);
        } catch (Exception e) {
            item = null;
        }

        return item;
    }

    public Weight getWeightById(int id) {
        Weight item;
        try (Realm realm = Realm.getDefaultInstance()) {
            Weight r = realm.where(Weight.class).equalTo("id_mob", id).findFirst();
            item = realm.copyFromRealm(r);
        } catch (Exception e) {
            item = null;
        }

        return item;
    }

    public Food getFoodById(int id) {
        Food item;
        try (Realm realm = Realm.getDefaultInstance()) {
            Food r = realm.where(Food.class).equalTo("id_mob", id).findFirst();
            item = realm.copyFromRealm(r);
        } catch (Exception e) {
            item = null;
        }

        return item;
    }

    public void clearMustUpdateItems() {
        List<Food> foods = getUpdateItems(Food.class);
        List<Exercise> exercises = getUpdateItems(Exercise.class);

        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            food.setMustUpdate(false);
        }

        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            exercise.setMustUpdate(false);
        }

        save(exercises, Exercise.class);
        save(foods, Food.class);
    }
}
