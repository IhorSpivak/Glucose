package com.ixbiopharma.glucose.value_picker;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.model.Type;
import com.ixbiopharma.glucose.model.Weight;
import com.ixbiopharma.glucose.repository.GlucoseRepository;
import com.ixbiopharma.glucose.repository.WeightRepository;
import com.ixbiopharma.glucose.utils.DataTypeUtils;
import com.ixbiopharma.glucose.utils.GlucoseUtils;

import org.parceler.Parcels;

/**
 * Presenter
 * <p>
 * Created by ivan on 22.04.17.
 */

class ValuePresenter implements ValueContract.Presenter {

    private ValueContract.View view;
    private final int type;
    private int id = -1;
    private WeightRepository weightRepository;
    private GlucoseRepository glucoseRepository;

    ValuePresenter(ValueContract.View view,
                   int type,
                   int id,
                   WeightRepository weightRepository,
                   GlucoseRepository glucoseRepository, boolean isEdit) {

        this.glucoseRepository = glucoseRepository;
        this.weightRepository = weightRepository;
        this.view = view;
        this.type = type;

        this.id = id;

        if (id == -1) {
            setItemType(!isEdit);
        } else {

            DataType dataType;

            if (type == Type.GLUCOSE) {
                dataType = glucoseRepository.getGlucoseById(id);
            } else {
                dataType = weightRepository.getWeightById(id);
            }
            setItemType(dataType, !isEdit);
        }
    }

    @Override
    public int getValueColor(float current) {
        if (type == Type.WIGHT) return R.color.color_green;

        return GlucoseUtils.getValueColor(current);
    }

    private void setItemType(DataType dataType, boolean showPlaceholder) {
        view.showDate(dataType.getDate());

        switch (dataType.getDataType()) {
            case Type.GLUCOSE:
                view.setItemTypeData(
                        "New Glucose Log",
                        "Glucose",
                        DataTypeUtils.getValueType(type),
                        300,
                        dataType.getValue(),
                        10,
                        DataTypeUtils.getGlucoseMeasureCoef(),
                        showPlaceholder);
                break;
            default:
                view.setItemTypeData(
                        "New Weight Log",
                        "Weight",
                        DataTypeUtils.getValueType(type),
                        200,
                        dataType.getValue(),
                        1,
                        1,
                        showPlaceholder);
                break;
        }
    }

    private void setItemType(boolean showPlaceholder) {
        switch (type) {
            case Type.GLUCOSE:
                view.setItemTypeData(
                        "New Glucose Log",
                        "Glucose",
                        DataTypeUtils.getValueType(type),
                        100,
                        glucoseRepository.getGlucoseLastValue(),
                        10,
                        DataTypeUtils.getGlucoseMeasureCoef(),
                        showPlaceholder);
                break;
            default:
                view.setItemTypeData(
                        "New Weight Log",
                        "Weight",
                        DataTypeUtils.getValueType(type),
                        200,
                        weightRepository.getWeightLastValue(),
                        1,
                        1,
                        showPlaceholder);
                break;
        }
    }

    private void createGlucose(String value, long timeMills) {
        Glucose glucose = new Glucose();

        glucose.setValue(Double.parseDouble(value));
        glucose.setDate(timeMills);

        if (id != -1) {
            glucose.setId(id);
        }
        view.showLoading();

        glucoseRepository.saveGlucose(glucose);
        view.hideLoading();
        view.onRecordSaved(Parcels.wrap(glucose));
    }

    private void createWeight(String value, long timeMills) {
        Weight weight = new Weight();

        weight.value = Double.parseDouble(value);
        weight.setDate(timeMills);

        if (id != -1) {
            weight.setId(id);
        }

        view.showLoading();
        weightRepository.saveWeight(weight);
        view.hideLoading();
        view.onRecordSaved(Parcels.wrap(weight));

    }

    @Override
    public void save(String value, long timeMills) {
        if (type == Type.GLUCOSE) {
            createGlucose(value, timeMills);
        } else {
            createWeight(value, timeMills);
        }
    }

    @Override
    public void destroy() {
        view = null;
    }
}
