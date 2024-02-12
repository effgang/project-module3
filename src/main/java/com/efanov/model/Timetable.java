package com.efanov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    private String date;
    private Group group;
    private Teacher teacher;
}
