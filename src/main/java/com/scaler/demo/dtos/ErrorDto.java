package com.scaler.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
public class ErrorDto {
    private String message;
    private int status;
    private String error;
    private OffsetDateTime timestamp;
    private Map<String, String> validationErrors;
}
