package com.efanov.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UtilityController {


    protected void sendStatus(HttpExchange exchange, String response, int code) {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        try {
            exchange.sendResponseHeaders(code, response.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            exchange.getResponseBody().write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        exchange.close();
    }

    protected long getIdFromPath(HttpExchange exchange) {
        var requestURI = exchange.getRequestURI().getPath();
        var split = requestURI.split("/");
        switch (split.length) {
            case 3 -> {
                return Long.parseLong(extractInt(requestURI).trim());
            }
            default -> {
                return 0;
            }
        }
    }

    protected Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    protected String extractInt(String str) {
        str = str.replaceAll("[^0-9]", " ");
        str = str.replaceAll(" +", " ");
        if (str.equals(""))
            return "-1";
        return str;
    }

}
