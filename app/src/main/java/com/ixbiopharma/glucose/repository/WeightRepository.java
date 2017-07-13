package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Weight;

import java.util.List;

/**
 * WeightRepository
 * <p>
 * Created by ivan on 6/12/17.
 */

public interface WeightRepository {

    void saveWeight(Weight weight);

    List<Weight> getAllWeight();

    void deleteWeight(Weight dataType);

    DataType getWeightById(int id);

    double getWeightLastValue();

}
