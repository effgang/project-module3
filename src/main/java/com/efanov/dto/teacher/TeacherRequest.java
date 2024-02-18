package com.efanov.dto.teacher;

import com.efanov.dto.AbstractPeople;
import com.efanov.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest extends AbstractPeople {
    private int experience;
    private Subject[] subjects;
}
