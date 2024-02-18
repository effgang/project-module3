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
public class DeleteResponse {
    private UUID id;
    private int statusCode;


    public DeleteResponse(int statusCode) {
        this.id = UUID.randomUUID();
        this.statusCode = statusCode;
    }
}
