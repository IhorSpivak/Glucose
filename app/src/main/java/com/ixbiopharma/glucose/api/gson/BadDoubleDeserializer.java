package com.ixbiopharma.glucose.api.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * BadDoubleDeserializer
 * <p>
 * Created by ivan on 29.05.17.
 */

public class BadDoubleDeserializer implements JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Double.parseDouble(element.getAsString().replace(',', '.'));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0d;
        }
    }

}
