package com.efanov.repository;


import com.efanov.exception.ModelException;
import com.efanov.model.Student;
import com.efanov.model.Timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.efanov.constant.ExceptionConstan.TIMETABLE_NOT_FOUND_BY_ID;


public class TimetableRepository {
    private final CopyOnWriteArrayList<Timetable> timetables = new CopyOnWriteArrayList<>();

    public List<Timetable> getTimetables() {
        return new ArrayList<>(timetables);
    }


    public Timetable getTimetableById(Long id) throws ModelException {
        return timetables.stream()
                .filter(el -> el.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ModelException(TIMETABLE_NOT_FOUND_BY_ID, id.toString()));
    }

    public Timetable getTimetableByGroupNumber(Integer number) {
        for (Timetable timetable : timetables) {
            if (timetable.getGroup().getGroupNumber() == number) {
                return timetable;
            }
        }
        return null;

    }

    public Timetable getTimetableByStudentSurname(String surname) {
        for (Timetable timetable : timetables) {
            for (Student student : timetable.getGroup().getStudentsInGroup()) {
                if (student.getSurname().equals(surname)) {
                    return timetable;
                }
            }
        }
        return null;
    }

    public Timetable getTimetableByTeacherSurname(String surname) {
        for (Timetable timetable : timetables) {
            if (timetable.getTeacher().getSurname().equals(surname)) {
                return timetable;
            }
        }
        return null;
    }


    public Timetable update(Timetable timetable, Long id) throws ModelException {
        timetable.setId(id);
        timetables.set(timetables.indexOf(getTimetableById(id)), timetable);

        return timetable;
    }

    public Timetable save(Timetable timetable) {
        timetable.setId((long) (timetables.size() + 1));
        timetables.add(timetable);
        return timetable;
    }

    public void delete(Long id) throws ModelException {
        timetables.remove(getTimetableById(id));
    }
}
