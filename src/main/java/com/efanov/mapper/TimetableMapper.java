package com.efanov.mapper;

import com.efanov.dto.timetable.TimetableRequest;
import com.efanov.dto.timetable.TimetableResponse;
import com.efanov.exception.ModelException;
import com.efanov.model.Group;
import com.efanov.model.Teacher;
import com.efanov.model.Timetable;
import com.efanov.repository.GroupRepository;
import com.efanov.repository.TeacherRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface TimetableMapper {
    GroupRepository groupRepo = new GroupRepository();
    TeacherRepository teacherRepo = new TeacherRepository();

    TimetableMapper INSTANCE = Mappers.getMapper(TimetableMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(target = "group", expression = "java(getGroup(dto.getGroupID()))")
    @Mapping(target = "teacher", expression = "java(getTeacher(dto.getTeacherID()))")
    Timetable mapToModel(TimetableRequest dto);

    @Mapping(target = "groupNumber", expression = "java(timetable.getGroup().getGroupNumber())")
    @Mapping(target = "teacherID", expression = "java(timetable.getTeacher().getId())")
    @Mapping(target = "subject", expression = "java(timetable.getSubject())")
    TimetableResponse mapToResponse(Timetable timetable);

    default Group getGroup(Long id) {
        try {
            return groupRepo.getGroupById(id);
        } catch (ModelException e) {
            return new Group();
        }
    }

    default Teacher getTeacher(Long id) {
        try {
            return teacherRepo.getTeacherById(id);
        } catch (ModelException e) {
            return new Teacher();
        }
    }
}
