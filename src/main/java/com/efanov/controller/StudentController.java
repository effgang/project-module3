package com.efanov.controller;

import com.efanov.dto.student.StudentRequest;
import com.efanov.exception.ModelException;
import com.efanov.repository.StudentRepository;
import com.efanov.service.JsonParseService;
import com.efanov.service.StudentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import static com.efanov.constant.WebConstant.*;


public class StudentController extends UtilityController implements HttpHandler {

    private final JsonParseService jsonParseService;
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, JsonParseService jsonParseService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.jsonParseService = jsonParseService;
        this.studentRepository = studentRepository;
    }

    @Override
    public void handle(HttpExchange exchange) {
        var requestType = exchange.getRequestMethod();
        var requestParam = exchange.getRequestURI().getQuery();
        var allStudents = studentRepository.getAllStudents();
        var queryMap = queryToMap(requestParam);
        switch (requestType) {
            case POST -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                sendStatus(exchange, studentService.save((StudentRequest) studentRequest), OK);
            }
            case GET -> {
                if (requestParam == null) {
                    var response = studentService.getStudents();
                    sendStatus(exchange, jsonParseService.writeToJson(response), OK);
                } else if (requestParam.contains(ID)) {
                    var id = queryMap.get(ID);
                    var response = studentService.getStudentById(Long.valueOf(id));
                    sendStatus(exchange, jsonParseService.writeToJson(response), OK);
                } else if (requestParam.contains(SURNAME)) {
                    var surname = queryMap.get(SURNAME);
                    var response = studentService.getStudentBySurname(surname);
                    sendStatus(exchange, jsonParseService.writeToJson(response), OK);
                }
            }
            case PUT -> {
                var studentRequest = jsonParseService.readObject(exchange.getRequestBody(), StudentRequest.class);
                var id = getIdFromPath(exchange);
                sendStatus(exchange, studentService.update((StudentRequest) studentRequest, id), OK);
            }
            case DELETE -> {
                var id = getIdFromPath(exchange);
                try {
                    if (allStudents.contains(studentRepository.getStudentById(id))) {
                        sendStatus(exchange, studentService.delete(id), OK);
                    }
                } catch (ModelException e) {
                    sendStatus(exchange, studentService.delete(id), NOT_FOUND);
                }
            }
        }
    }
}
