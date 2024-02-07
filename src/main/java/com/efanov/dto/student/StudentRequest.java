package com.efanov.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest extends AbstractPeople {
    private Long groupNumber;

}
