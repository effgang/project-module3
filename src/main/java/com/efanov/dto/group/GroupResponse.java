package com.efanov.dto.group;

import com.efanov.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    private Long id;
    private int groupNumber;
    private List<Student> studentsInGroup;
}
