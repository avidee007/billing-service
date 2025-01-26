package com.avi.billingservice.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class BaseError {
    private String status;
    private int statusCode;
    private LocalDateTime timestamp;
}
