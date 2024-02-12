package com.efanov.repository;

import com.efanov.exception.ModelException;
import com.efanov.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.efanov.constant.ExceptionConstan.STUDENT_NOT_FOUND_BY_ID;
import static com.efanov.constant.ExceptionConstan.STUDENT_NOT_FOUND_BY_SURNAME;

public class StudentRepository {

    private final CopyOnWriteArrayList<Student> students = new CopyOnWriteArrayList<>();

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getStudentBySurname(String surname) throws ModelException {
        return students.stream()
                .filter(el -> surname.equals(el.getSurname()))
                .findFirst()
                .orElseThrow(() -> new ModelException(STUDENT_NOT_FOUND_BY_SURNAME, surname));
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

    public Student update(Student student, Long id) {

        students.set((int) (id - 1), student);
        student.setId(id);

        return student;
    }

    public void delete(Student student) {
        students.remove(students.get(students.indexOf(student)));
    }
}