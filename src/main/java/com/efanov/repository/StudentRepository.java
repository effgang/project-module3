package com.efanov.repository;

import com.efanov.exception.ModelException;
import com.efanov.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.efanov.constant.ExceptionConstan.STUDENT_NOT_FOUND_BY_ID;
import static com.efanov.constant.ExceptionConstan.STUDENT_NOT_FOUND_BY_NAME;

public class StudentRepository {

    private final CopyOnWriteArrayList<Student> students = new CopyOnWriteArrayList<>();

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getStudentByName(String name) throws ModelException {
        return students.stream()
                .filter(el -> name.equals(el.getName()))
                .findFirst()
                .orElseThrow(() -> new ModelException(STUDENT_NOT_FOUND_BY_NAME, name));
    }

    public Student getStudentById(Long id) throws ModelException {
        return students.stream()
                .filter(el -> el.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ModelException(STUDENT_NOT_FOUND_BY_ID, id.toString()));
    }

    public Student save(Student student) {
        student.setId((long) (students.size() + 1));
        students.add(student);
        return student;
    }
}