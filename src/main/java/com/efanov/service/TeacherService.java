package com.efanov.service;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.teacher.TeacherRequest;

public interface TeacherService {
    DecoratorResponse<String> getTeacherByName(String name);

    DecoratorResponse<String> getTeacherBySurname(String surname);

    DecoratorResponse<String> getTeacherById(Long id);

    DecoratorResponse<String> getTeachers();

    DecoratorResponse<String> save(TeacherRequest teacherRequest);

    DecoratorResponse<String> update(TeacherRequest teacherRequest, Long id);

    DecoratorResponse<String> delete(Long id);
}
