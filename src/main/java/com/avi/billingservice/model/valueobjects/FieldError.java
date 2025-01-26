package com.avi.billingservice.model.valueobjects;

public record FieldError(String fieldName, Object rejectedValue, String message) {
}