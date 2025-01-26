package com.avi.billingservice.model.error;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApiServerError extends BaseError {
    private final String errorMessage;

    public ApiServerError(String name, int value, String errorMessage, LocalDateTime timestamp) {
        super(name, value, timestamp);
        this.errorMessage = errorMessage;
    }
}
