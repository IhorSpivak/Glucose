package com.ixbiopharma.glucose.utils;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.model.Type;

/**
 * DataTypeUtils
 * <p>
 * Created by ivan on 22.04.17.
 */

public class DataTypeUtils {

    public static int glucoseMeasureType = 0;


    public static int getTypeColor(DataType dataType) {
        switch (dataType.getDataType()) {
            case Type.WIGHT:
                return R.color.color_salad;
            case Type.GLUCOSE:
                return GlucoseUtils.getGlucoseColor();
            case Type.FOOD:
                return R.color.color_purple;
            default :
                return R.color.color_orange;
        }
    }

    public static String getTypeName(DataType dataType) {
        if (dataType.getDataType() == Type.GLUCOSE) return "Glucose";
        if (dataType.getDataType() == Type.EXERCISE) return "Exercise";
        return "Weight";
    }

    public static int getTypeIcon(DataType dataType) {
        switch (dataType.getDataType()) {
            case Type.WIGHT:
                return R.mipmap.ic_weight;
            case Type.GLUCOSE:
                return GlucoseUtils.getGlucoseIcon();
            case Type.FOOD:
                return R.mipmap.ic_food;
            default:
                return R.mipmap.ic_excersice;
        }
    }

    public static String getValueWithType(DataType dataType) {
        return dataType.getValue() + " " + getValueType(dataType.getDataType());
    }

    public static String getValueWithType(Glucose glucose) {
        return glucose.getValue() + " " + getValueType(glucose.getDataType());
    }

    public static int getGlucoseMeasureCoef(){
        return (glucoseMeasureType == 0) ? 1 : 18;
    }

    public static String getValueType(int type) {
        switch (type) {
            case Type.WIGHT:
                return "Kg";
            case Type.GLUCOSE:
                return (glucoseMeasureType == 0) ? "mmol/L" : "mg/Dl";
            case Type.FOOD:
                return "";
            default:
                return "Mins";
        }
    }

    public static String getFoodType(int type) {
        switch (type){
            case 1: return "Breakfast";
            case 3: return "Dinner";
            case 2:return "Lunch";
            default: return "Snacks";
        }
    }
}
