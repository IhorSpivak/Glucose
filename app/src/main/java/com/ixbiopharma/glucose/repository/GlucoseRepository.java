package com.ixbiopharma.glucose.repository;

import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Glucose;

import java.util.List;

/**
 * GlucoseRepository
 * <p>
 * Created by ivan on 6/12/17.
 */

public interface GlucoseRepository {

    void deleteGlucose(Glucose dataType);

    List<Glucose> getAllGlucose();

    DataType getGlucoseById(int id);

    void saveGlucose(Glucose glucose);

    double getGlucoseLastValue();

}
