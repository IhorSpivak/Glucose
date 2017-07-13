package com.ixbiopharma.glucose.food;

import android.support.annotation.Nullable;

import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.repository.FoodRepository;
import com.ixbiopharma.glucose.repository.GlucoseRepository;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FoodPresenter
 * <p>
 * Created by ivan on 22.04.17.
 */

class FoodPresenter implements FoodContract.Presenter {

    private FoodContract.View view;
    private FoodRepository foodRepository;
    private GlucoseRepository glucoseRepository;

    FoodPresenter(FoodContract.View view,
                  FoodRepository foodRepository,
                  GlucoseRepository glucoseRepository) {

        this.glucoseRepository = glucoseRepository;
        this.view = view;
        this.foodRepository = foodRepository;
        fillHints();
    }

    private void fillHints() {
        view.setFoodHintData(foodRepository.getAllFoodTypes());
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void save(int selectedFoodType,
                     int feelingType,
                     long time, List<Food> foodList,
                     @Nullable Glucose preGlucose,
                     @Nullable Glucose postGlucose) {

        if (selectedFoodType == -1) {
            view.showError("Select meal type");
            return;
        }

        if (foodList.isEmpty()) {
            view.showError("Add some food");
            return;
        }

        if (preGlucose != null) {
            long hours = preGlucose.getDate().getTime() - time;

            if (hours > 0) {
                view.showError("Ensure the pre-meal reading time is before your meal.");
                return;
            }
        }

        if (postGlucose != null) {
            float hours = TimeUtils.hoursDifference(postGlucose.getDate(), new Date(time));

            if (!(hours >= 2)) {
                view.showError("Ensure the post-meal reading is at least 2 hours after your meal.");
                return;
            }
        }

        new Thread(() -> {
            view.showLoading();

            if (postGlucose != null) {
                glucoseRepository.saveGlucose(postGlucose);
            }

            if (preGlucose != null) {
                glucoseRepository.saveGlucose(preGlucose);
            }

            List<Food> list = new ArrayList<>();

            for (int i = 0; i < foodList.size(); i++) {
                Food food = foodList.get(i);

                if (!view.hasInternet()) {
                    food.setMustUpdate(true);
                }

                food.setDate(time);
                food.setType(selectedFoodType);
                food.setFeeling(feelingType);

                if (postGlucose != null) {
                    food.setPostGlucose(postGlucose.getId());
                }
                if (preGlucose != null) {
                    food.setPreGlucose(preGlucose.getId());
                }

                list.add(food);
            }


            foodRepository.saveFood(list);

            view.hideLoading();
            view.onFoodSaved();
        }).start();
    }

    @Override
    public void delete(Food food) {
        view.showLoading();
        foodRepository.deleteFood(food);
        view.onDeleteComplete();
        view.hideLoading();
    }
}
