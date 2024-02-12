package com.efanov;


import com.efanov.controller.StudentController;
import com.efanov.controller.TeacherController;
import com.efanov.mapper.StudentMapper;
import com.efanov.mapper.TeacherMapper;
import com.efanov.repository.StudentRepository;
import com.efanov.repository.TeacherRepository;
import com.efanov.service.JsonParseService;
import com.efanov.service.StudentService;
import com.efanov.service.TeacherService;
import com.efanov.service.impl.JsonParseServiceImpl;
import com.efanov.service.impl.StudentServiceImpl;
import com.efanov.service.impl.TeacherServiceImpl;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.efanov.constant.LogConstant.SERVER_CANT_START;
import static com.efanov.constant.LogConstant.SERVER_STARTED;
import static com.efanov.constant.WebConstant.*;

@Slf4j

public class Main {

    public static void main(String[] args) {

        JsonParseService jsonParseService = new JsonParseServiceImpl();

        StudentMapper studentMapper = StudentMapper.INSTANCE;
        StudentRepository studentRepository = new StudentRepository();
        StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);
        StudentController studentController = new StudentController(studentService, jsonParseService, studentRepository);

        TeacherMapper teacherMapper = TeacherMapper.INSTANCE;
        TeacherRepository teacherRepository = new TeacherRepository();
        TeacherService teacherService = new TeacherServiceImpl(teacherRepository, jsonParseService, teacherMapper);
        TeacherController teacherController = new TeacherController(jsonParseService, teacherService);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(LOCALHOST, 8080), 0);
            log.info(SERVER_STARTED);
            server.createContext(STUDENTS, studentController);
            server.createContext(TEACHERS, teacherController);
            server.start();
        } catch (IOException e) {
            log.error(SERVER_CANT_START, e.getMessage(), e);
            throw new RuntimeException(e);
        }


    }
}
