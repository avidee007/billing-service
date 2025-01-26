package com.avi.billingservice.exception;

import com.avi.billingservice.model.valueobjects.FieldError;
import com.avi.billingservice.model.error.ApiInputError;
import com.avi.billingservice.model.error.ApiServerError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiInputError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var inputError = new ApiInputError(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(),
                mapFieldErrors(ex), LocalDateTime.now());
        log.error("Input Error : {}", inputError.getFieldErrors());
        return new ResponseEntity<>(inputError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CurrencyRateApiException.class)
    public ResponseEntity<ApiServerError> handleCurrencyExchangeApiException(CurrencyRateApiException ex) {
        var serverError = new ApiServerError(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(), LocalDateTime.now());
        log.error("CurrencyExchangeApiException : {}", serverError.getErrorMessage());
        return new ResponseEntity<>(serverError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiServerError> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        var serverError = new ApiServerError(HttpStatus.UNAUTHORIZED.name(), HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(), LocalDateTime.now());
        log.error("AuthorizationDeniedException : {}", serverError.getErrorMessage());
        return new ResponseEntity<>(serverError, HttpStatus.UNAUTHORIZED);
    }

    private List<FieldError> mapFieldErrors(MethodArgumentNotValidException ex) {
        List<org.springframework.validation.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        return fieldErrors.stream()
                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()))
                .toList();
    }

}
