package com.efanov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student extends Person {
    private Long id;
    private String birthday;
    private Long groupNumber;

}
