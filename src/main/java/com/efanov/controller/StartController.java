package com.efanov.controller;

import com.efanov.mapper.GroupMapper;
import com.efanov.mapper.StudentMapper;
import com.efanov.mapper.TeacherMapper;
import com.efanov.mapper.TimetableMapper;
import com.efanov.repository.GroupRepository;
import com.efanov.repository.StudentRepository;
import com.efanov.repository.TeacherRepository;
import com.efanov.repository.TimetableRepository;
import com.efanov.resourcesParser.YamlParser;
import com.efanov.service.*;
import com.efanov.service.impl.*;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.efanov.constant.LogConstant.SERVER_CANT_START;
import static com.efanov.constant.LogConstant.SERVER_STARTED;
import static com.efanov.constant.WebConstant.*;


public class StartController {
    static Logger log = LoggerFactory.getLogger(StartController.class.getName());

    public void init() {
        JsonParseService jsonParseService = new JsonParseServiceImpl();
        YamlParser yamlParser = new YamlParser();

        StudentMapper studentMapper = StudentMapper.INSTANCE;
        StudentRepository studentRepository = GroupMapper.studentRepo;
        StudentService studentService = new StudentServiceImpl(studentRepository, jsonParseService, studentMapper);
        StudentController studentController = new StudentController(studentService, jsonParseService);

        GroupMapper groupMapper = GroupMapper.INSTANCE;
        GroupRepository groupRepository = TimetableMapper.groupRepo;
        GroupService groupService = new GroupServiceImpl(groupRepository, jsonParseService, groupMapper, studentRepository, yamlParser);
        GroupController groupController = new GroupController(jsonParseService, groupService);

        TeacherMapper teacherMapper = TeacherMapper.INSTANCE;
        TeacherRepository teacherRepository = TimetableMapper.teacherRepo;
        TeacherService teacherService = new TeacherServiceImpl(teacherRepository, jsonParseService, teacherMapper);
        TeacherController teacherController = new TeacherController(jsonParseService, teacherService);

        TimetableMapper timetableMapper = TimetableMapper.INSTANCE;
        TimetableRepository timetableRepository = new TimetableRepository();
        TimetableService timetableService = new TimetableServiceImpl(timetableRepository, jsonParseService, timetableMapper, teacherRepository, groupRepository, yamlParser);
        TimetableController timetableController = new TimetableController(jsonParseService, timetableService);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(LOCALHOST, 8080), 0);
            log.info(SERVER_STARTED);
            server.createContext(STUDENTS, studentController);
            server.createContext(TEACHERS, teacherController);
            server.createContext(GROUPS, groupController);
            server.createContext(TIMETABLES, timetableController);
            server.start();
        } catch (IOException e) {
            log.error(SERVER_CANT_START, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
