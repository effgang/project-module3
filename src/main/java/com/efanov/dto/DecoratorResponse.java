package com.efanov.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DecoratorResponse<T> {
    private int statusCode;
    private T entity;

    public DecoratorResponse(int statusCode, T entity) {
        this.statusCode = statusCode;
        this.entity = entity;
    }
}
