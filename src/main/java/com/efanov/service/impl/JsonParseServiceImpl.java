package com.efanov.service.impl;

import com.efanov.dto.DeleteResponse;
import com.efanov.service.JsonParseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.WebConstant.BAD_REQUEST;
import static com.efanov.constant.WebConstant.NOT_FOUND;

@Slf4j
public class JsonParseServiceImpl implements JsonParseService {

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

