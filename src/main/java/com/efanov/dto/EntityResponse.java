package com.efanov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityResponse <T>{
    private UUID id;
    private String error;
    private int statusCode;
    private String time;
    private boolean success;
    private T entity;

}
