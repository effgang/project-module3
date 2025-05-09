package com.efanov.dto.teacher;

import com.efanov.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long id;
    private String name;
    private String surname;
    private int experience;
    private Subject[] subjects;
    private String phoneNumber;
}
