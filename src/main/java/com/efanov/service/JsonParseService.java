package com.efanov.service;

import com.efanov.exception.JsonParseException;

import java.io.InputStream;

public interface JsonParseService {
    String writeToJson(Object object) throws JsonParseException;

    Object readObject(InputStream json, Class object) throws JsonParseException;
}
