package com.efanov.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AnswerController {

    public static void sendOk(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
