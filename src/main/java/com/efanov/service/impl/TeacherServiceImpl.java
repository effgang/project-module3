package com.efanov.service.impl;

import com.efanov.dto.DeleteResponse;
import com.efanov.dto.ErrorResponse;
import com.efanov.dto.teacher.TeacherRequest;
import com.efanov.dto.teacher.TeacherResponse;
import com.efanov.exception.ModelException;
import com.efanov.mapper.TeacherMapper;
import com.efanov.middleware.BirthDayMiddleware;
import com.efanov.middleware.Middleware;
import com.efanov.middleware.PhoneNumberMiddleware;
import com.efanov.model.Teacher;
import com.efanov.repository.TeacherRepository;
import com.efanov.service.JsonParseService;
import com.efanov.service.TeacherService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.efanov.constant.ExceptionConstan.*;
import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.WebConstant.*;

@Slf4j
public class TeacherServiceImpl implements TeacherService {
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
    public TeacherResponse getTeacherByName(String name) {
        try {
            return teacherMapper.mapToResponse(teacherRepository.getTeacherByName(name));
        } catch (ModelException e) {
            log.error(TEACHER_NOT_FOUND_BY_NAME,name);
            return null;
        }
    }


    @Override
    public TeacherResponse getTeacherById(Long id) {
        try {
            return teacherMapper.mapToResponse(teacherRepository.getTeacherById(id));
        } catch (ModelException e) {
            log.error(TEACHER_NOT_FOUND_BY_ID,id);
            return null;
        }
    }

    public List<TeacherResponse> getTeachers() {
        log.info(GET_TEACHERS_METHOD_CALL);
        return teacherRepository.getAllTeachers()
                .stream()
                .map(teacherMapper::mapToResponse)
                .toList();
    }

    @Override
    public String save(TeacherRequest teacherRequest) {
        log.info(SAVE_TEACHER_METHOD_CALL_WITH_VALUE, teacherRequest);
        if (!middleware.check(teacherRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse(CAN_NOT_CREATE_TEACHER,BAD_REQUEST));
        }
        Teacher teacherToSave = teacherMapper.mapToModel(teacherRequest);
        var result = teacherRepository.save(teacherToSave);
        return jsonParseService.writeToJson(teacherMapper.mapToResponse(result));
    }

    @Override
    public String update(TeacherRequest teacherRequest, Long id) {
        log.info(UPDATE_METHOD_CALL_WITH_VALUE, teacherRequest);
        if (!middleware.check(teacherRequest)) {
            return jsonParseService.writeToJson(new ErrorResponse(CAN_NOT_CREATE_TEACHER,BAD_REQUEST));
        }
        Teacher newTeacher = teacherMapper.mapToModel(teacherRequest);
        var result = teacherRepository.update(newTeacher, id);
        return jsonParseService.writeToJson(teacherMapper.mapToResponse(result));
    }

    @Override
    public String delete(Long id) {
        log.info(DELETE_METHOD_CALL_WITH_VALUE, id);
        Teacher teacherToDelete;
        try {
            teacherToDelete = teacherRepository.getTeacherById(id);
            //teacherToDelete = teacherRepository.getAllTeachers().get((int) (id-1));
        } catch (ModelException e) {
            log.error(TEACHER_NOT_FOUND_BY_ID, id);
            return jsonParseService.writeToJson(new DeleteResponse(NOT_FOUND,false));
        }
        teacherRepository.delete(teacherToDelete);
        return jsonParseService.writeToJson(new DeleteResponse(NO_CONTENT,true));
    }

}
