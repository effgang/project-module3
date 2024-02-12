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
    private boolean success;

    public DeleteResponse(int statusCode, boolean success) {
        this.id = UUID.randomUUID();
        this.statusCode = statusCode;
        this.success = success;
    }
}
