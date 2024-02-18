package com.efanov.controller;

import com.efanov.dto.student.StudentRequest;
import com.efanov.service.JsonParseService;
import com.efanov.service.StudentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import static com.efanov.constant.WebConstant.*;


public class StudentController extends UtilityController implements HttpHandler {

    private final JsonParseService jsonParseService;
    private final StudentService studentService;

    public StudentController(StudentService studentService, JsonParseService jsonParseService) {
        this.studentService = studentService;
        this.jsonParseService = jsonParseService;
    }

    @Override
    public void handle(HttpExchange exchange) {
        var requestType = exchange.getRequestMethod();
        switch (requestType) {
            case POST -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                var save = studentService.save((StudentRequest) studentRequest);
                sendStatus(exchange, save.getEntity(), save.getStatusCode());
            }
            case GET -> {
                var requestParam = exchange.getRequestURI().getQuery();
                var queryMap = queryToMap(requestParam);
                if (requestParam == null) {
                    if (getIdFromPath(exchange) != 0) {
                        var response = studentService.getStudentById(getIdFromPath(exchange));
                        sendStatus(exchange, response.getEntity(), response.getStatusCode());
                    }
                    var response = studentService.getStudents();
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());

                } else if (requestParam.contains(SURNAME)) {
                    var surname = queryMap.get(SURNAME);
                    var response = studentService.getStudentBySurname(surname);
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());

                } else if (requestParam.contains(NAME)) {
                    var name = queryMap.get(NAME);
                    var response = studentService.getStudentsByName(name);
                    sendStatus(exchange, response.getEntity(), response.getStatusCode());

                }
            }
            case PUT -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                var id = getIdFromPath(exchange);
                var update = studentService.update((StudentRequest) studentRequest, id);
                sendStatus(exchange, update.getEntity(), update.getStatusCode());
            }
            case DELETE -> {
                var id = getIdFromPath(exchange);
                var delete = studentService.delete(id);
                sendStatus(exchange, delete.getEntity(), delete.getStatusCode());

            }
        }
    }
}
