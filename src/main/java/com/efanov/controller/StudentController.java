package com.efanov.controller;

import com.efanov.dto.student.StudentRequest;
import com.efanov.service.JsonParseService;
import com.efanov.service.StudentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.efanov.constant.WebConstant.GET;
import static com.efanov.constant.WebConstant.POST;
import static com.efanov.controller.AnswerController.sendOk;


public class StudentController implements HttpHandler {

    private final JsonParseService jsonParseService;
    private final StudentService studentService;

    public StudentController(StudentService studentService, JsonParseService jsonParseService) {
        this.studentService = studentService;
        this.jsonParseService = jsonParseService;
    }

    @SneakyThrows
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var requestType = exchange.getRequestMethod();
        switch (requestType) {
            case POST -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                sendOk(exchange, studentService.save((StudentRequest) studentRequest));
            }
            case GET -> {
                var response = studentService.getStudents();
                var responseAsString = jsonParseService.writeToJson(response);
                sendOk(exchange, responseAsString);
            }
        }
    }
}
