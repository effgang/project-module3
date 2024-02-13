package com.efanov.service;


import java.io.InputStream;
import java.io.OutputStream;

public interface JsonParseService {
    String writeToJson(Object object);

    Object readObject(InputStream json, Class object);
    Object readObject(OutputStream json, Class object);
}
