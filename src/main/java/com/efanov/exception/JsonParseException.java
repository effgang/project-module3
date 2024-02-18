package com.efanov.exception;

public class JsonParseException extends Exception {
    public JsonParseException(String message, String param) {
        super(message);
    }

    public JsonParseException(String message) {
        super(message);
    }
}
