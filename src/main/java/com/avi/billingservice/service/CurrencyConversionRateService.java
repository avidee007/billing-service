package com.avi.billingservice.service;

public interface CurrencyConversionRateService {
    double getConversionRate(String originalCurrency, String targetCurrency);
}
