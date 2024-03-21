package com.example.demo.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

public class ObjectMapperProvider {
    private static final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";
    public static ObjectMapper mapper = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat(dateTimeFormat))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());
}
