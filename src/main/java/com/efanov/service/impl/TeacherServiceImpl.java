package com.efanov.service.impl;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.DeleteResponse;
import com.efanov.dto.ErrorResponse;
import com.efanov.dto.teacher.TeacherRequest;
import com.efanov.exception.ModelException;
import com.efanov.mapper.TeacherMapper;
import com.efanov.middleware.BirthDayMiddleware;
import com.efanov.middleware.Middleware;
import com.efanov.middleware.PhoneNumberMiddleware;
import com.efanov.model.Teacher;
import com.efanov.repository.TeacherRepository;
import com.efanov.service.JsonParseService;
import com.efanov.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.efanov.constant.ExceptionConstan.*;
import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.WebConstant.NOT_FOUND;
import static com.efanov.constant.WebConstant.OK;

public class TeacherServiceImpl implements TeacherService {
    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class.getName());
    private final TeacherRepository teacherRepository;
    private final JsonParseService jsonParseService;
    private final TeacherMapper teacherMapper;

    private final Middleware middleware = Middleware.link(
            new PhoneNumberMiddleware(),
            new BirthDayMiddleware()
    );

    public TeacherServiceImpl(TeacherRepository teacherRepository, JsonParseService jsonParseService, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.jsonParseService = jsonParseService;
        this.teacherMapper = teacherMapper;
    }


    @Override
    public DecoratorResponse<String> getTeacherByName(String name) {
        try {
            var result = teacherRepository.getTeacherByName(name);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(teacherMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(TEACHER_NOT_FOUND_BY_NAME);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TEACHER_NOT_FOUND_BY_NAME, NOT_FOUND)));
        }
    }

    @Override
    public DecoratorResponse<String> getTeacherBySurname(String surname) {
        try {
            var result = teacherRepository.getTeacherBySurname(surname);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(teacherMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(TEACHER_NOT_FOUND_BY_SURNAME);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TEACHER_NOT_FOUND_BY_SURNAME, NOT_FOUND)));
        }
    }


    @Override
    public DecoratorResponse<String> getTeacherById(Long id) {
        try {
            var result = teacherRepository.getTeacherById(id);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(teacherMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(TEACHER_NOT_FOUND_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TEACHER_NOT_FOUND_BY_ID, NOT_FOUND)));
        }
    }

    public DecoratorResponse<String> getTeachers() {
        log.info(GET_TEACHERS_METHOD_CALL);
        var list = teacherRepository.getAllTeachers()
                .stream()
                .map(teacherMapper::mapToResponse)
                .toList();
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(list));
    }

    @Override
    public DecoratorResponse<String> save(TeacherRequest teacherRequest) {
        log.info(SAVE_TEACHER_METHOD_CALL_WITH_VALUE, teacherRequest);
        if (!middleware.check(teacherRequest)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_CREATE_TEACHER, NOT_FOUND)));
        }
        Teacher teacherToSave = teacherMapper.mapToModel(teacherRequest);
        var result = teacherRepository.save(teacherToSave);
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(teacherMapper.mapToResponse(result)));

    }

    @Override
    public DecoratorResponse<String> update(TeacherRequest teacherRequest, Long id) {
        log.info(UPDATE_METHOD_CALL_WITH_VALUE, teacherRequest);
        if (!middleware.check(teacherRequest)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_CREATE_TEACHER, NOT_FOUND)));
        }
        Teacher newTeacher = teacherMapper.mapToModel(teacherRequest);
        Teacher result;
        try {
            result = teacherRepository.update(newTeacher, id);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(teacherMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(CANT_UPDATE_TEACHER);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_UPDATE_TEACHER, NOT_FOUND)));
        }

    }

    @Override
    public DecoratorResponse<String> delete(Long id) {
        try {
            log.info(DELETE_METHOD_CALL_WITH_VALUE, id);
            teacherRepository.delete(id);
        } catch (ModelException e) {
            log.error(CANT_DELETE_TEACHER);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CANT_DELETE_TEACHER, NOT_FOUND)));
        }
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(new DeleteResponse(OK)));
    }
}
