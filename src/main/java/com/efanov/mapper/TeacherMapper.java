package com.efanov.mapper;

import com.efanov.dto.teacher.TeacherRequest;
import com.efanov.dto.teacher.TeacherResponse;
import com.efanov.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    Teacher mapToModel(TeacherRequest dto);

    TeacherResponse mapToResponse(Teacher teacher);
}
