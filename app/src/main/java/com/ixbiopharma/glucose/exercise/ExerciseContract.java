package com.ixbiopharma.glucose.exercise;

import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.ExerciseType;

import java.util.List;

/**
 * ExerciseContract
 * <p>
 * Created by ivan on 22.04.17.
 */

interface ExerciseContract {

    interface View {
        void setExerciseList(List<ExerciseType> exerciseList);

        void showLoading();

        void hideLoading();

        void onSaved();

        void onError(String message);

        void showExercise(Exercise exercise);

        boolean hasInternet();
    }

    interface Presenter {
        void destroy();

        void save(String exercise, double value, long time);
    }
}
