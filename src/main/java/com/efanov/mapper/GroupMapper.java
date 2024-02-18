package com.efanov.mapper;

import com.efanov.dto.group.GroupRequest;
import com.efanov.dto.group.GroupResponse;
import com.efanov.exception.ModelException;
import com.efanov.model.Group;
import com.efanov.model.Student;
import com.efanov.repository.StudentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);
    StudentRepository studentRepo = new StudentRepository();

    @Mapping(target = "studentsInGroup", expression = "java(getStudentsByIds(dto.getStudentsIds()))")
    @Mapping(ignore = true, target = "id")
    Group mapToModel(GroupRequest dto);

    GroupResponse mapToResponse(Group group);

    default List<Student> getStudentsByIds(Long[] id) {
        try {
            var students = new ArrayList<Student>();
            for (Long l : id) {
                students.add(studentRepo.getStudentById(l));
            }
            return students;
        } catch (ModelException e) {
            return new ArrayList<>();
        }
    }
}
