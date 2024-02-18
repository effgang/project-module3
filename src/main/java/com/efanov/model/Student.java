package com.efanov.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Student extends Person {
    private String birthday;

    public Student(Long id, String name, String surname, String phoneNumber, String birthday) {
        super(id, name, surname, phoneNumber);
        this.birthday = birthday;
    }
}
