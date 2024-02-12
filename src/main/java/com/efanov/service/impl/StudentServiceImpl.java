package com.efanov.service.impl;

import com.efanov.dto.DeleteResponse;
import com.efanov.dto.ErrorResponse;
import com.efanov.dto.student.StudentRequest;
import com.efanov.dto.student.StudentResponse;
import com.efanov.exception.ModelException;
import com.efanov.mapper.StudentMapper;
import com.efanov.middleware.BirthDayMiddleware;
import com.efanov.middleware.Middleware;
import com.efanov.middleware.PhoneNumberMiddleware;
import com.efanov.model.Student;
import com.efanov.repository.StudentRepository;
import com.efanov.service.JsonParseService;
import com.efanov.service.StudentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.efanov.constant.ExceptionConstan.*;
import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.WebConstant.*;

@Slf4j

public class StudentServiceImpl implements StudentService {
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
    public StudentResponse getStudentBySurname(String surname) {
        try {
            return studentMapper.mapToResponse(studentRepository.getStudentBySurname(surname));
        } catch (ModelException e) {
            log.error(STUDENT_NOT_FOUND_BY_SURNAME, surname);
            return null;
        }
    }


    @Override
    public StudentResponse getStudentById(Long id) {
        try {
            return studentMapper.mapToResponse(studentRepository.getStudentById(id));
        } catch (ModelException e) {
            log.error(STUDENT_NOT_FOUND_BY_ID, id.toString());
            return null;
        }
    }

    public List<StudentResponse> getStudents() {
        log.info(GET_STUDENTS_METHOD_CALL);
        return studentRepository.getAllStudents()
                .stream()
                .map(studentMapper::mapToResponse)
                .toList();
    }

    @Override
    public String save(StudentRequest studentRequest) {
        log.info(SAVE_STUDENT_METHOD_CALL_WITH_VALUE, studentRequest);
        if (!middleware.check(studentRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse(CANNOT_CREATE_STUDENT,BAD_REQUEST));
        }
        Student studentToSave = studentMapper.mapToModel(studentRequest);
        var result = studentRepository.save(studentToSave);
        return jsonParseService.writeToJson(studentMapper.mapToResponse(result));
    }

    @Override
    public String update(StudentRequest studentRequest, Long id) {
        log.info(UPDATE_METHOD_CALL_WITH_VALUE, studentRequest);
        if (!middleware.check(studentRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse(CAN_NOT_CREATE_TEACHER,BAD_REQUEST));
        }
        Student newStudent = studentMapper.mapToModel(studentRequest);
        var result = studentRepository.update(newStudent, id);
        return jsonParseService.writeToJson(studentMapper.mapToResponse(result));
    }

    @Override
    public String delete(Long id) {
        log.info(DELETE_METHOD_CALL_WITH_VALUE, id);
        Student studentToDelete;
        try {
            studentToDelete = studentRepository.getStudentById(id);
        } catch (ModelException e) {
            log.error(STUDENT_NOT_FOUND_BY_ID, id);
            return jsonParseService.writeToJson(new DeleteResponse(NOT_FOUND,false));
        }
        studentRepository.delete(studentToDelete);
        return jsonParseService.writeToJson(new DeleteResponse(NO_CONTENT,true));
    }
}
