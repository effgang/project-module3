package com.efanov.service.impl;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.DeleteResponse;
import com.efanov.dto.ErrorResponse;
import com.efanov.dto.timetable.TimetableRequest;
import com.efanov.exception.ModelException;
import com.efanov.mapper.TimetableMapper;
import com.efanov.middleware.TimeMiddleware;
import com.efanov.model.Timetable;
import com.efanov.repository.GroupRepository;
import com.efanov.repository.TeacherRepository;
import com.efanov.repository.TimetableRepository;
import com.efanov.resourcesParser.YamlParser;
import com.efanov.service.JsonParseService;
import com.efanov.service.TimetableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.efanov.constant.ExceptionConstan.TEACHER_NOT_FOUND_BY_ID;
import static com.efanov.constant.ExceptionConstan.TIMETABLE_NOT_FOUND_BY_ID;
import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.WebConstant.*;


public class TimetableServiceImpl implements TimetableService {
    private static final Logger log = LoggerFactory.getLogger(TimetableServiceImpl.class.getName());
    private final TimetableRepository timetableRepository;
    private final JsonParseService jsonParseService;
    private final TimetableMapper timetableMapper;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final TimeMiddleware middleware = new TimeMiddleware();
    private final YamlParser yamlParser;

    public TimetableServiceImpl(TimetableRepository timetableRepository, JsonParseService jsonParseService, TimetableMapper timetableMapper, TeacherRepository teacherRepository, GroupRepository groupRepository, YamlParser yamlParser) {
        this.timetableRepository = timetableRepository;
        this.jsonParseService = jsonParseService;
        this.timetableMapper = timetableMapper;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.yamlParser = yamlParser;
    }

    @Override
    public DecoratorResponse<String> getTimetables() {
        var list = timetableRepository.getTimetables()
                .stream()
                .map(timetableMapper::mapToResponse)
                .toList();
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(list));

    }

    @Override
    public DecoratorResponse<String> getTimetableByGroupNumber(Integer number) {
        if (timetableRepository.getTimetableByGroupNumber(number) != null)
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(timetableMapper.mapToResponse(timetableRepository.getTimetableByGroupNumber(number))));
        log.error(TIMETABLE_NOT_FOUND_BY_GROUP_NUMBER);
        return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TIMETABLE_NOT_FOUND_BY_GROUP_NUMBER, NOT_FOUND)));

    }

    @Override
    public DecoratorResponse<String> getTimetableByStudentSurname(String surname) {
        if (timetableRepository.getTimetableByStudentSurname(surname) != null)
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(timetableMapper.mapToResponse(timetableRepository.getTimetableByStudentSurname(surname))));
        log.error(TIMETABLE_NOT_FOUND_BY_STUDENT_SURNAME);
        return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TIMETABLE_NOT_FOUND_BY_STUDENT_SURNAME, NOT_FOUND)));
    }

    @Override
    public DecoratorResponse<String> getTimetableByTeacherSurname(String surname) {
        if (timetableRepository.getTimetableByTeacherSurname(surname) != null)
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(timetableMapper.mapToResponse(timetableRepository.getTimetableByTeacherSurname(surname))));
        log.error(TIMETABLE_NOT_FOUND_BU_TEACHER_SURNAME);
        return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TIMETABLE_NOT_FOUND_BU_TEACHER_SURNAME, NOT_FOUND)));
    }

    @Override
    public DecoratorResponse<String> getTimetableById(Long id) {
        try {
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(timetableMapper.mapToResponse(timetableRepository.getTimetableById(id))));
        } catch (ModelException e) {
            log.error(TIMETABLE_NOT_FOUND_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TIMETABLE_NOT_FOUND_BY_ID, NOT_FOUND)));
        }
    }

    @Override
    public DecoratorResponse<String> save(TimetableRequest timetableRequest) {
        log.info(SAVE_METHOD_CALL_WITH_VALUE, timetableRequest);
        if (middleware.check(timetableRequest)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(INVALID_TIME, NOT_FOUND)));
        }
        var lessonCount = timetableRepository.getTimetables().stream()
                .filter(el -> el.getGroup().getId().equals(timetableRequest.getGroupID()))
                .count();
        if (lessonCount + 1 > yamlParser.getYmlMap().get(MAX_COUNT_LESSON_FOR_GROUP)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TO_MANY_LESSON_COUNT_FOR_THIS_GROUP, NOT_FOUND)));
        }
        try {
            groupRepository.getGroupById(timetableRequest.getGroupID());
        } catch (ModelException e) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(GROUP_NOT_FOUND_BY_ID, NOT_FOUND)));
        }
        try {
            teacherRepository.getTeacherById(timetableRequest.getTeacherID());
        } catch (ModelException e) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TEACHER_NOT_FOUND_BY_ID, NOT_FOUND)));
        }
        for (Timetable timetable : timetableRepository.getTimetables()) {
            if (timetable.getGroup().getId().equals(timetableRequest.getGroupID()) && timetable.getTimeStart().equals(timetableRequest.getTimeStart())) {
                return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(AT_THIS_TIME_THE_GROUP_WITH_SUCH_ID_ALREADY_HAS_A_LESSON, NOT_FOUND)));
            } else if (timetable.getTeacher().getId().equals(timetableRequest.getTeacherID()) && timetable.getTimeStart().equals(timetableRequest.getTimeStart())) {
                return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(AT_THIS_TIME_THE_TEACHER_WITH_SUCH_ID_ALREADY_HAS_A_LESSON, NOT_FOUND)));
            }
        }
        Timetable timetable = timetableMapper.mapToModel(timetableRequest);
        var result = timetableRepository.save(timetable);
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(timetableMapper.mapToResponse(result)));

    }

    @Override
    public DecoratorResponse<String> update(TimetableRequest timetableRequest, Long id) {
        log.info(UPDATE_METHOD_CALL_WITH_VALUE, timetableRequest);
        if (middleware.check(timetableRequest)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(INVALID_TIME, NOT_FOUND)));
        }
        var lessonCount = timetableRepository.getTimetables().stream()
                .filter(el -> el.getGroup().getId().equals(timetableRequest.getGroupID()))
                .count();
        if (lessonCount + 1 > yamlParser.getYmlMap().get(MAX_COUNT_LESSON_FOR_GROUP)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(TO_MANY_LESSON_COUNT_FOR_THIS_GROUP, NOT_FOUND)));
        }
        Timetable timetable = timetableMapper.mapToModel(timetableRequest);
        Timetable result;
        try {
            result = timetableRepository.update(timetable, id);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(timetableMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(CAN_T_UPDATE_TIMETABLE_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CAN_T_UPDATE_TIMETABLE_BY_ID, NOT_FOUND)));
        }

    }

    @Override
    public DecoratorResponse<String> delete(Long id) {
        try {
            log.info(DELETE_METHOD_CALL_WITH_VALUE, id);
            timetableRepository.delete(id);
        } catch (ModelException e) {
            log.error(CAN_T_DELETE_TIMETABLE_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CAN_T_DELETE_TIMETABLE_BY_ID, NOT_FOUND)));
        }
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(new DeleteResponse(OK)));
    }
}
