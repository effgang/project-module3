package com.efanov.repository;

import com.efanov.exception.ModelException;
import com.efanov.model.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.efanov.constant.ExceptionConstan.TEACHER_NOT_FOUND_BY_ID;
import static com.efanov.constant.ExceptionConstan.TEACHER_NOT_FOUND_BY_NAME;


public class TeacherRepository {
    private final CopyOnWriteArrayList<Teacher> teachers = new CopyOnWriteArrayList<>();

    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers);
    }

    public Teacher getTeacherByName(String name) throws ModelException {
        return teachers.stream()
                .filter(el -> name.equals(el.getName()))
                .findFirst()
                .orElseThrow(() -> new ModelException(TEACHER_NOT_FOUND_BY_NAME, name));

    }

    public Teacher getTeacherBySurname(String surname) throws ModelException {
        return teachers.stream()
                .filter(el -> surname.equals(el.getSurname()))
                .findFirst()
                .orElseThrow(() -> new ModelException(TEACHER_NOT_FOUND_BY_NAME, surname));
    }

    public Teacher getTeacherById(Long id) throws ModelException {

        return teachers.stream()
                .filter(el -> el.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ModelException(TEACHER_NOT_FOUND_BY_ID, id.toString()));

    }

    public Teacher save(Teacher teacher) {
        teacher.setId((long) (teachers.size() + 1));
        teachers.add(teacher);
        return teacher;
    }

    public Teacher update(Teacher teacher, Long id) throws ModelException {
        teacher.setId(id);
        teachers.set(teachers.indexOf(getTeacherById(id)), teacher);

        return teacher;
    }

    public void delete(Long id) throws ModelException {
        teachers.remove(getTeacherById(id));
    }
}
