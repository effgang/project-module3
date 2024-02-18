package com.efanov.dto.student;

import com.efanov.dto.AbstractPeople;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest extends AbstractPeople {
    private String birthday;
}
