package com.project.raiserbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {

    private int code;
    private String message;
    private boolean status;

    public APIResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }
}
