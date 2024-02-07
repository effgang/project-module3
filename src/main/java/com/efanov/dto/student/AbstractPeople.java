package com.efanov.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractPeople {
    protected String name;
    protected String surname;
    protected String phoneNumber;
    protected String birthday;
}
