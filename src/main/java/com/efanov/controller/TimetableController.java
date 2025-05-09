package com.efanov.controller;

import com.efanov.dto.timetable.TimetableRequest;
import com.efanov.service.JsonParseService;
import com.efanov.service.TimetableService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import static com.efanov.constant.WebConstant.*;

public class TimetableController extends UtilityController implements HttpHandler {
    private final JsonParseService jsonParseService;
    private final TimetableService timetableService;

    public TimetableController(JsonParseService jsonParseService, TimetableService timetableService) {
        this.jsonParseService = jsonParseService;
        this.timetableService = timetableService;
    }

    @Override
    public void handle(HttpExchange exchange) {
        var requestType = exchange.getRequestMethod();
        var timetableRequest = jsonParseService.readObject(exchange.getRequestBody(), TimetableRequest.class);
        switch (requestType) {
            case POST -> {
                var save = timetableService.save((TimetableRequest) timetableRequest);
                sendStatus(exchange, save.getEntity(), save.getStatusCode());
            }
            case GET -> {
                var requestParam = exchange.getRequestURI().getQuery();
                var queryMap = queryToMap(requestParam);

                if (requestParam == null) {
                    if (getIdFromPath(exchange) != 0) {
                        var response = timetableService.getTimetableById(getIdFromPath(exchange));
                        sendStatus(exchange, response.getEntity(), response.getStatusCode());
                    }
                    var response = timetableService.getTimetables();
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());
                } else if (requestParam.contains(STUDENT_SURNAME)) {
                    var surname = queryMap.get(STUDENT_SURNAME);
                    var response = timetableService.getTimetableByStudentSurname(surname);
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());
                } else if (requestParam.contains(TEACHER_SURNAME)) {
                    var surname = queryMap.get(TEACHER_SURNAME);
                    var response = timetableService.getTimetableByTeacherSurname(surname);
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());
                } else if (requestParam.contains(GROUP_NUMBER)) {
                    var number = queryMap.get(GROUP_NUMBER);
                    var response = timetableService.getTimetableByGroupNumber(Integer.valueOf(number));
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());
                }
            }
            case PUT -> {
                var id = getIdFromPath(exchange);
                var update = timetableService.update((TimetableRequest) timetableRequest, id);
                sendStatus(exchange, update.getEntity(), update.getStatusCode());
            }
            case DELETE -> {
                var id = getIdFromPath(exchange);
                var delete = timetableService.delete(id);
                sendStatus(exchange, delete.getEntity(), delete.getStatusCode());
            }
        }
    }
}
