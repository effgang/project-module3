package com.efanov;


import com.efanov.controller.StudentController;
import com.efanov.mapper.StudentMapper;
import com.efanov.repository.StudentRepository;
import com.efanov.service.JsonParseService;
import com.efanov.service.StudentService;
import com.efanov.service.impl.JsonParseServiceImpl;
import com.efanov.service.impl.StudentServiceImpl;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.efanov.constant.LogConstant.SERVER_CANT_START;
import static com.efanov.constant.LogConstant.SERVER_STARTED;
import static com.efanov.constant.WebConstant.LOCALHOST;
import static com.efanov.constant.WebConstant.STUDENTS;

@Slf4j

public class Main {

    public static void main(String[] args) {

        JsonParseService jsonParseService = new JsonParseServiceImpl();

        StudentMapper studentMapper = StudentMapper.INSTANCE;
        StudentRepository studentRepository = new StudentRepository();
        StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);
        StudentController studentController = new StudentController(studentService, jsonParseService);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(LOCALHOST, 8080), 0);
            log.info(SERVER_STARTED);
            server.createContext(STUDENTS, studentController);
            server.start();
        } catch (IOException e) {
            log.error(SERVER_CANT_START, e.getMessage(), e);
            throw new RuntimeException(e);
        }


    }
}
