package com.efanov.controller;

import com.efanov.dto.teacher.TeacherRequest;
import com.efanov.service.JsonParseService;
import com.efanov.service.TeacherService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import static com.efanov.constant.WebConstant.*;

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

        switch (requestType) {
            case POST -> {
                var teacherRequest = jsonParseService.readObject(exchange.getRequestBody(), TeacherRequest.class);
                var save = teacherService.save((TeacherRequest) teacherRequest);
                sendStatus(exchange, save.getEntity(), save.getStatusCode());
            }
            case GET -> {
                var requestParam = exchange.getRequestURI().getQuery();
                var queryMap = queryToMap(requestParam);
                if (requestParam == null) {
                    if (getIdFromPath(exchange) != 0) {
                        var response = teacherService.getTeacherById(getIdFromPath(exchange));
                        sendStatus(exchange, response.getEntity(), response.getStatusCode());
                    }
                    var response = teacherService.getTeachers();
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());

                } else if (requestParam.contains(SURNAME)) {
                    var surname = queryMap.get(SURNAME);
                    var response = teacherService.getTeacherBySurname(surname);
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());

                } else if (requestParam.contains(NAME)) {
                    var name = queryMap.get(NAME);
                    var response = teacherService.getTeacherByName(name);
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());
                }
            }
            case PUT -> {
                var id = getIdFromPath(exchange);
                var teacherRequest = jsonParseService.readObject(exchange.getRequestBody(), TeacherRequest.class);
                var update = teacherService.update((TeacherRequest) teacherRequest, id);
                sendStatus(exchange, update.getEntity(), update.getStatusCode());
            }
            case DELETE -> {
                var id = getIdFromPath(exchange);
                var delete = teacherService.delete(id);
                sendStatus(exchange, delete.getEntity(), delete.getStatusCode());
            }
        }

    }
}

