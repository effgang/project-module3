package com.efanov.repository;

import com.efanov.exception.ModelException;
import com.efanov.model.Teacher;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.efanov.constant.ExceptionConstan.TEACHER_NOT_FOUND_BY_ID;
import static com.efanov.constant.ExceptionConstan.TEACHER_NOT_FOUND_BY_NAME;

@Slf4j
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

    public Teacher update(Teacher teacher, Long id) {
        teacher.setId(id);
        teachers.set((int) (id - 1), teacher);

        return teacher;
    }

    public void delete(Teacher teacher) {
        teachers.remove(teacher);
    }
}
