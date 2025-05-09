package com.efanov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    private Long id;
    private Group group;
    private Teacher teacher;
    private String timeStart;
    private String timeEnd;
    private Subject subject;
}
