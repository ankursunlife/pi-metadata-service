package com.aarete.pi.metadata.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mpalla
 */
public class JsonParser {

    private JsonParser() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    /**
     * This method takes string, remove escape characters and returns JSON string
     */
    public static Object convertToJson(String value) {
        Object object;
        try {
            object = gson.fromJson(value, Object.class).getClass();
            if (object.equals(String.class)) {
                return value;
            } else if (object.equals(LinkedTreeMap.class)) {
                return readValue(value, Map.class );
            } else if (object.equals(ArrayList.class)) {
                return readValue(value, List.class);
            }
        } catch (JsonSyntaxException | JsonProcessingException e) {
            return  value;
        }
        return value;
    }

    /**
     * This method converts JSON string to specific Class format
     */
    private static <T> T readValue(final String json,final Class<T> classType) throws JsonProcessingException {
        return objectMapper.readValue(json, classType);
    }

}
