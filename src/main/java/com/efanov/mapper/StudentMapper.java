package com.efanov.mapper;

import com.efanov.dto.student.StudentRequest;
import com.efanov.dto.student.StudentResponse;
import com.efanov.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    Student mapToModel(StudentRequest dto);

    StudentResponse mapToResponse(Student student);
}
