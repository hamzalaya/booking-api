package com.booking.utils;

import com.booking.exceptions.TechnicalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String asJsonString(Object o) {
        try {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    public static <T> T readValue(String jsonString, Class<T> type) {
        try {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());

            return objectMapper.readValue(jsonString, type);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot convert json string " + jsonString + "to " + type.getName());
        }
    }

    public static <T> T readValue(String jsonString, TypeReference<T> valueTypeRef) {
        try {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Cannot convert json string " + jsonString + "to " + valueTypeRef);
        }
    }

}
