package com.ixbiopharma.glucose.food;

import android.support.annotation.Nullable;

import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.FoodType;
import com.ixbiopharma.glucose.model.Glucose;

import java.util.List;

/**
 * FoodContract
 * <p>
 * Created by ivan on 22.04.17.
 */

interface FoodContract {

    interface View {
        void setFoodHintData(List<FoodType> foods);

        void showError(String message);

        void showLoading();

        void hideLoading();

        void onFoodSaved();

        void onDeleteComplete();

        boolean hasInternet();
    }

    interface Presenter {
        void destroy();

        void save(int selectedFoodType,
                  int feelingType,
                  long time,
                  List<Food> foodList,
                  @Nullable Glucose preGlucose,
                  @Nullable Glucose postGlucose);

        void delete(Food food);
    }
}