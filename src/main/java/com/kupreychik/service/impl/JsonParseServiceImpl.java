package com.kupreychik.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kupreychik.exception.JsonParseException;
import com.kupreychik.service.JsonParseService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class JsonParseServiceImpl implements JsonParseService {
    private final ObjectMapper objectMapper;

    public JsonParseServiceImpl(){
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String writeToJson(Object object) throws JsonParseException {
        try {
            log.info("Trying to convert object {} to string", object);
            //следующие 2 строчки для класса LocalDate
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Cannot parse object to string");
            throw new JsonParseException();
        }
    }

    @Override
    public Object readObject(InputStream inputStream, Class object) {
        try {
            log.info("Trying to convert json {} to object", object);
            return objectMapper.readValue(inputStream, object);
        }  catch (IOException e) {
            log.error("Cannot parse string to object");
            throw new RuntimeException(e);
        }
    }
}
