package com.efanov.service;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.student.StudentRequest;

public interface StudentService {
    DecoratorResponse<String> getStudentsByName(String name);

    DecoratorResponse<String> getStudentBySurname(String surname);

    DecoratorResponse<String> getStudentById(Long id);

    DecoratorResponse<String> getStudents();

    DecoratorResponse<String> save(StudentRequest studentRequest);

    DecoratorResponse<String> update(StudentRequest studentRequest, Long id);

    DecoratorResponse<String> delete(Long id);
}
