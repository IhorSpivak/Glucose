package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.FoodType;

import java.util.List;

/**
 * FoodRepository
 *
 * Created by ivan on 6/12/17.
 */

public interface FoodRepository {

    List<Food> getAllFood();

    List<FoodType> getAllFoodTypes();

    void deleteFood(Food dataType);

    void saveFood(List<Food> list);
}
