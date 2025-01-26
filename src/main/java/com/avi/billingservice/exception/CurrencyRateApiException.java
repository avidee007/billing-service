package com.avi.billingservice.exception;

import java.io.Serial;

public class CurrencyRateApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -90745766939L;

    public CurrencyRateApiException(String message) {
        super(message);
    }
}
