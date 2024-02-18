package com.efanov.service.impl;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.DeleteResponse;
import com.efanov.dto.ErrorResponse;
import com.efanov.dto.group.GroupRequest;
import com.efanov.exception.ModelException;
import com.efanov.mapper.GroupMapper;
import com.efanov.model.Group;
import com.efanov.repository.GroupRepository;
import com.efanov.repository.StudentRepository;
import com.efanov.resourcesParser.YamlParser;
import com.efanov.service.GroupService;
import com.efanov.service.JsonParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.efanov.constant.ExceptionConstan.GROUP_NOT_FOUND_BY_NUMBER;
import static com.efanov.constant.ExceptionConstan.GROUP_NOT_FOUND_BY_SURNAME;
import static com.efanov.constant.LogConstant.*;
import static com.efanov.constant.WebConstant.*;


public class GroupServiceImpl implements GroupService {
    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class.getName());
    private final GroupRepository groupRepository;
    private final JsonParseService jsonParseService;
    private final GroupMapper groupMapper;
    private final StudentRepository studentRepository;
    private final YamlParser parser;

    public GroupServiceImpl(GroupRepository groupRepository, JsonParseService jsonParseService, GroupMapper groupMapper, StudentRepository studentRepository, YamlParser parser) {
        this.groupRepository = groupRepository;
        this.jsonParseService = jsonParseService;
        this.groupMapper = groupMapper;
        this.studentRepository = studentRepository;
        this.parser = parser;
    }

    @Override
    public DecoratorResponse<String> getAllGroups() {
        var list = groupRepository.getAllGroups()
                .stream()
                .map(groupMapper::mapToResponse)
                .toList();
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(list));
    }

    @Override
    public DecoratorResponse<String> getGroupByNumber(Integer number) {
        try {
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(groupMapper.mapToResponse(groupRepository.getGroupByNumber(number))));
        } catch (ModelException e) {
            log.error(GROUP_NOT_FOUND_BY_NUMBER);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(GROUP_NOT_FOUND_BY_NUMBER, NOT_FOUND)));
        }
    }

    @Override
    public DecoratorResponse<String> getGroupBySurname(String surname) {
        if (groupRepository.getGroupBySurname(surname) != null)
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(groupMapper.mapToResponse(groupRepository.getGroupBySurname(surname))));
        log.error(GROUP_NOT_FOUND_BY_SURNAME);
        return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(GROUP_NOT_FOUND_BY_SURNAME, NOT_FOUND)));
    }

    @Override
    public DecoratorResponse<String> getGroupById(Long id) {
        try {
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(groupMapper.mapToResponse(groupRepository.getGroupById(id))));
        } catch (ModelException e) {
            log.error(GROUP_NOT_FOUND_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(GROUP_NOT_FOUND_BY_ID, NOT_FOUND)));
        }
    }

    @Override
    public DecoratorResponse<String> delete(Long id) {
        try {
            groupRepository.delete(id);
        } catch (ModelException e) {
            log.error(CAN_T_DELETE_GROUP_BY_ID);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CAN_T_DELETE_GROUP_BY_ID, NOT_FOUND)));
        }
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(new DeleteResponse(OK)));
    }

    @Override
    public DecoratorResponse<String> update(GroupRequest groupRequest, Long id) {
        try {
            if (groupRequest.getStudentsIds().length < parser.getYmlMap().get(MIN_STUDENT_IN_GROUP) || groupRequest.getStudentsIds().length > parser.getYmlMap().get(MAX_STUDENT_IN_GROUP)) {
                return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(FEW_OR_MANY_STUDENTS_IN_THE_GROUP, NOT_FOUND)));
            }
            var newGroup = groupMapper.mapToModel(groupRequest);
            var result = groupRepository.update(newGroup, id);
            return new DecoratorResponse<>(OK, jsonParseService.writeToJson(groupMapper.mapToResponse(result)));
        } catch (ModelException e) {
            log.error(CAN_T_UPDATE_GROUP);
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(CAN_T_UPDATE_GROUP, NOT_FOUND)));
        }
    }

    @Override
    public DecoratorResponse<String> save(GroupRequest groupRequest) {
        log.info(SAVE_GROUP_METHOD_CALL_WITH_VALUE, groupRequest);
        for (Group group : groupRepository.getAllGroups()) {
            if (group.getGroupNumber() == groupRequest.getGroupNumber()) {
                return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(SAME_NUMBER_HAS_ALREADY_BEEN_CREATED, NOT_FOUND)));
            }
        }
        if (groupRequest.getStudentsIds().length < parser.getYmlMap().get(MIN_STUDENT_IN_GROUP) || groupRequest.getStudentsIds().length > parser.getYmlMap().get(MAX_STUDENT_IN_GROUP)) {
            return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(FEW_OR_MANY_STUDENTS_IN_THE_GROUP, NOT_FOUND)));
        }
        for (Long id : groupRequest.getStudentsIds()) {
            try {
                studentRepository.getStudentById(id);
            } catch (ModelException e) {
                return new DecoratorResponse<>(NOT_FOUND, jsonParseService.writeToJson(new ErrorResponse(THERE_IS_NO_STUDENT_OR_STUDENTS_WITH_SUCH_IDS, NOT_FOUND)));
            }
        }
        var result = groupRepository.save(groupMapper.mapToModel(groupRequest));
        return new DecoratorResponse<>(OK, jsonParseService.writeToJson(groupMapper.mapToResponse(result)));
    }
}

