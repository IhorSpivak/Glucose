package com.ixbiopharma.glucose.exercise;

import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.repository.ExerciseRepository;

/**
 * ExercisePresenter
 * <p>
 * Created by ivan on 22.04.17.
 */

class ExercisePresenter implements ExerciseContract.Presenter {

    private ExerciseContract.View view;
    private int id = -1;
    private ExerciseRepository exerciseRepository;
    private Exercise exercise;

    ExercisePresenter(ExerciseContract.View view,
                      int id,
                      ExerciseRepository exerciseRepository) {

        this.view = view;
        this.id = id;
        this.exerciseRepository = exerciseRepository;

        exercise = exerciseRepository.getExerciseById(id);

        view.setExerciseList(exerciseRepository.getExerciseList());

        if (exercise != null){
            view.showExercise(exercise);
        }
    }

    @Override
    public void save(String name, double value, long time) {
        if (exercise == null){
            exercise = new Exercise();
        }

        exercise.setId(id);
        exercise.exercise = name;
        exercise.value = value;
        exercise.setDate(time);

        if (!view.hasInternet()){
            exercise.setMustUpdate(true);
        }

        exerciseRepository.saveExercise(exercise);
        view.onSaved();
    }

    @Override
    public void destroy() {
        view = null;
    }
}
