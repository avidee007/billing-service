package com.avi.billingservice.model.error;

import com.avi.billingservice.model.valueobjects.FieldError;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ApiInputError extends BaseError {
    List<FieldError> fieldErrors;

    public ApiInputError(String name, int value, List<FieldError> fieldErrors, LocalDateTime timestamp) {
        super(name, value, timestamp);
        this.fieldErrors = fieldErrors;
    }
}

