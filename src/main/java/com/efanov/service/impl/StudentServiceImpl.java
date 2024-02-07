package com.efanov.service.impl;

import com.efanov.dto.ErrorResponse;
import com.efanov.dto.student.StudentRequest;
import com.efanov.dto.student.StudentResponse;
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

import static com.efanov.constant.LogConstant.GET_STUDENTS_METHOD_CALL;
import static com.efanov.constant.LogConstant.SAVE_STUDENT_METHOD_CALL_WITH_VALUE;
import static com.efanov.constant.WebConstant.CANNOT_CREATE_STUDENT;

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

    @SneakyThrows
    @Override
    public StudentResponse getStudentById(Long id) {
        return studentMapper.mapToResponse(studentRepository.getStudentById(id));
    }

    public List<StudentResponse> getStudents() {
        log.info(GET_STUDENTS_METHOD_CALL);
        return studentRepository.getAllStudents()
                .stream()
                .map(studentMapper::mapToResponse)
                .toList();
    }

    @Override
    @SneakyThrows
    public String save(StudentRequest studentRequest) {
        log.info(SAVE_STUDENT_METHOD_CALL_WITH_VALUE, studentRequest);
        if (!middleware.check(studentRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse(CANNOT_CREATE_STUDENT));
        }
        Student studentToSave = studentMapper.mapToModel(studentRequest);
        var result = studentRepository.save(studentToSave);
        return jsonParseService.writeToJson(studentMapper.mapToResponse(result));
    }
}
