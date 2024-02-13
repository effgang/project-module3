package com.efanov.service;

import com.efanov.dto.teacher.TeacherRequest;
import com.efanov.dto.teacher.TeacherResponse;
import com.efanov.exception.ModelException;

import java.util.List;

public interface TeacherService {
    TeacherResponse getTeacherByName(String name);

    TeacherResponse getTeacherById(Long id);

    List<TeacherResponse> getTeachers();

    String save(TeacherRequest teacherRequest);

    String update(TeacherRequest teacherRequest, Long id);

    String delete(Long id);
}
