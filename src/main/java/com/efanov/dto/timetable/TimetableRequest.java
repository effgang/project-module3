package com.efanov.dto.timetable;

import com.efanov.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimetableRequest {
    private Long groupID;
    private Long teacherID;
    private String timeStart;
    private String timeEnd;
    private Subject subject;

}
