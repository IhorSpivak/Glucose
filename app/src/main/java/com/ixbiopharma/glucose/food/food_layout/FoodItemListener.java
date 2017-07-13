package com.ixbiopharma.glucose.food.food_layout;

import com.ixbiopharma.glucose.model.Food;

/**
 * FoodItemListener
 * <p>
 * Created by ivan on 23.04.17.
 */

interface FoodItemListener {

    void onUploadClick(Food food);

    void onRemoveClick(Food food);

    void onAddClick();

}
