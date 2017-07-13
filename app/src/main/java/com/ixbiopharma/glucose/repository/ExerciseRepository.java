package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.ExerciseType;

import java.util.List;

/**
 * ExerciseRepository
 * <p>
 * Created by ivan on 6/12/17.
 */

public interface ExerciseRepository {

    List<Exercise> getAllActivity();

    void saveExercise(Exercise exercise);

    void deleteExercise(Exercise dataType);

    List<ExerciseType> getExerciseList();

    Exercise getWalkingExercise();

    Exercise getExerciseById(int id);
}
