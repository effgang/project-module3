package com.efanov.service;

import com.efanov.dto.student.StudentRequest;
import com.efanov.dto.student.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse getStudentBySurname(String surname);

    StudentResponse getStudentById(Long id);

    List<StudentResponse> getStudents();

    String save(StudentRequest studentRequest);

    String update(StudentRequest studentRequest, Long id);

    String delete(Long id);
}
