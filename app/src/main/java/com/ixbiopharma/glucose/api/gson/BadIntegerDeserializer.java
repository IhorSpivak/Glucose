package com.ixbiopharma.glucose.api.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * BadDoubleDeserializer
 *
 * Created by ivan on 29.05.17.
 */

public class BadIntegerDeserializer implements JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Integer.parseInt(element.getAsString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
