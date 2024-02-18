package com.efanov.service;


import java.io.InputStream;

public interface JsonParseService {
    String writeToJson(Object object);

    Object readObject(InputStream json, Class object);
}
