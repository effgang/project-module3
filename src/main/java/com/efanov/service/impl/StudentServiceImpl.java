package com.efanov.service.impl;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.DeleteResponse;
import com.efanov.dto.ErrorResponse;
import com.efanov.dto.student.StudentRequest;
import com.efanov.exception.ModelException;
import com.efanov.mapper.StudentMapper;
import com.efanov.middleware.BirthDayMiddleware;
import com.efanov.middleware.Middleware;
import com.efanov.middleware.PhoneNumberMiddleware;
import com.efanov.model.Student;
import com.efanov.repository.StudentRepository;
import com.efanov.service.JsonParseService;
import com.efanov.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.efanov.constant.ExceptionConstan.*;
import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.WebConstant.NOT_FOUND;
import static com.efanov.constant.WebConstant.OK;


public class StudentServiceImpl implements StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class.getName());
    private final StudentRepository studentRepository;
    private final JsonParseService jsonParseService;
    private final StudentMapper studentMapper;

    private final Middleware middleware = Middleware.link(
            new PhoneNumberMiddleware(),
            new BirthDayMiddleware()
    );

    public StudentServiceImpl(StudentRepository studentRepository, JsonParseService jsonParseService, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.jsonParseService = jsonParseService;
        this.studentMapper = studentMapper;
    }

    @Override
    public DecoratorResponse<String> getStudentsByName(String name) {
        try {
            var result = studentRepository.getStudentByName(name);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(studentMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(STUDENT_NOT_FOUND_BY_NAME);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(STUDENT_NOT_FOUND_BY_NAME, NOT_FOUND)));
        }
    }

    @Override
    public DecoratorResponse<String> getStudentBySurname(String surname) {
        try {
            var result = studentRepository.getStudentBySurname(surname);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(studentMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(STUDENT_NOT_FOUND_BY_SURNAME);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(STUDENT_NOT_FOUND_BY_SURNAME, NOT_FOUND)));
        }
    }


    @Override
    public DecoratorResponse<String> getStudentById(Long id) {
        try {
            var result = studentRepository.getStudentById(id);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(studentMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(STUDENT_NOT_FOUND_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(STUDENT_NOT_FOUND_BY_ID, NOT_FOUND)));
        }
    }

    public DecoratorResponse<String> getStudents() {
        log.info(GET_STUDENTS_METHOD_CALL);
        var list = studentRepository.getAllStudents()
                .stream()
                .map(studentMapper::mapToResponse)
                .toList();
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(list));
    }

    @Override
    public DecoratorResponse<String> save(StudentRequest studentRequest) {
        log.info(SAVE_STUDENT_METHOD_CALL_WITH_VALUE, studentRequest);
        if (!middleware.check(studentRequest)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_CREATE_STUDENT, NOT_FOUND)));
        }
        Student studentToSave = studentMapper.mapToModel(studentRequest);
        var result = studentRepository.save(studentToSave);
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(studentMapper.mapToResponse(result)));
    }

    @Override
    public DecoratorResponse<String> update(StudentRequest studentRequest, Long id) {
        log.info(UPDATE_METHOD_CALL_WITH_VALUE, studentRequest);
        if (!middleware.check(studentRequest)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_UPDATE_STUDENT, NOT_FOUND)));
        }
        Student newStudent = studentMapper.mapToModel(studentRequest);
        try {
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(studentMapper.mapToResponse(studentRepository.update(newStudent, id))));
        } catch (ModelException e) {
            log.error(CANT_UPDATE_TEACHER);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_UPDATE_STUDENT, NOT_FOUND)));
        }
    }

    @Override
    public DecoratorResponse<String> delete(Long id) {
        log.info(DELETE_METHOD_CALL_WITH_VALUE, id);
        try {
            studentRepository.delete(id);
        } catch (ModelException e) {
            log.error(STUDENT_NOT_FOUND_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_DELETE_STUDENT, NOT_FOUND)));
        }
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(new DeleteResponse(OK)));
    }
}
