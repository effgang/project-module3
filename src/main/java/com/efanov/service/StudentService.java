package com.efanov.service;

import com.efanov.dto.student.StudentRequest;
import com.efanov.dto.student.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse getStudentById(Long id);

    List<StudentResponse> getStudents();

    String save(StudentRequest studentRequest);
}
