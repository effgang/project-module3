package com.efanov.controller;

import com.efanov.dto.DeleteResponse;
import com.efanov.dto.teacher.TeacherRequest;
import com.efanov.service.JsonParseService;
import com.efanov.service.TeacherService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;

import static com.efanov.constant.WebConstant.*;

@Slf4j
public class TeacherController extends UtilityController implements HttpHandler {
    private final JsonParseService jsonParseService;
    private final TeacherService teacherService;

    public TeacherController(JsonParseService jsonParseService, TeacherService teacherService) {
        this.jsonParseService = jsonParseService;
        this.teacherService = teacherService;
    }

    @Override
    public void handle(HttpExchange exchange) {
        var requestType = exchange.getRequestMethod();
        var requestParam = exchange.getRequestURI().getQuery();
        var queryMap = queryToMap(requestParam);

        switch (requestType) {
            case POST -> {
                var teacherRequest = jsonParseService.readObject(exchange.getRequestBody(), TeacherRequest.class);
                sendStatus(exchange, teacherService.save((TeacherRequest) teacherRequest), CREATED);
            }
            case GET -> {
                if (requestParam == null) {
                    var response = teacherService.getTeachers();
                    sendStatus(exchange, jsonParseService.writeToJson(response), OK);
                } else if (requestParam.contains(ID)) {
                    var id = queryMap.get(ID);
                    var response = teacherService.getTeacherById(Long.valueOf(id));
                    sendStatus(exchange, jsonParseService.writeToJson(response), OK);
                } else if (requestParam.contains(NAME)) {
                    var name = queryMap.get(NAME);
                    var response = teacherService.getTeacherByName(name);
                    sendStatus(exchange, jsonParseService.writeToJson(response), OK);
                }
            }
            case PUT -> {
                var teacherRequest = jsonParseService.readObject(exchange.getRequestBody(), TeacherRequest.class);
                var id = getIdFromPath(exchange);
                sendStatus(exchange, teacherService.update((TeacherRequest) teacherRequest, id), OK);
            }
            case DELETE -> {
                var id = getIdFromPath(exchange);
//                var response = jsonParseService.readObject(exchange.getResponseBody(), DeleteResponse.class);
//                System.out.println(response);
                sendStatus(exchange, teacherService.delete(id), OK);
            }
        }

    }
}

