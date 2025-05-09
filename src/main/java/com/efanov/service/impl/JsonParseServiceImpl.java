package com.efanov.service.impl;

import com.efanov.service.JsonParseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static com.efanov.constant.LogConstant.*;


public class JsonParseServiceImpl implements JsonParseService {
    private static final Logger log = LoggerFactory.getLogger(JsonParseServiceImpl.class.getName());
    private final ObjectMapper objectMapper;

    public JsonParseServiceImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String writeToJson(Object object) {
        try {
            log.info(TRYING_TO_CONVERT_OBJECT_TO_STRING, object);
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(CANNOT_PARSE_OBJECT_TO_STRING);
            return null;
        }
    }

    @Override
    public Object readObject(InputStream json, Class object) {
        try {
            log.info(TRYING_PARSE_JSON_TO_OBJECT, object);
            return objectMapper.readValue(json, object);
        } catch (IOException e) {
            log.error(CANNOT_PARSE_STRING_TO_OBJECT);
            return null;
        }
    }

}

