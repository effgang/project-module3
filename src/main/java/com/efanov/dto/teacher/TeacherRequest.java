package com.efanov.dto.teacher;

import com.efanov.dto.AbstractPeople;
import com.efanov.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest extends AbstractPeople {
    private int experience;
    private Subject[] subjects;
}
