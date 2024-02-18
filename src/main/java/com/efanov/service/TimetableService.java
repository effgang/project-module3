package com.efanov.service;

import com.efanov.dto.DecoratorResponse;
import com.efanov.dto.timetable.TimetableRequest;

public interface TimetableService {
    DecoratorResponse<String> getTimetables();

    DecoratorResponse<String> getTimetableByGroupNumber(Integer number);

    DecoratorResponse<String> getTimetableByStudentSurname(String surname);

    DecoratorResponse<String> getTimetableByTeacherSurname(String surname);

    DecoratorResponse<String> getTimetableById(Long id);

    DecoratorResponse<String> delete(Long id);

    DecoratorResponse<String> update(TimetableRequest timetableRequest, Long id);

    DecoratorResponse<String> save(TimetableRequest timetableRequest);
}
