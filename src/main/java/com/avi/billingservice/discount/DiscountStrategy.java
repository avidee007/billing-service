package com.avi.billingservice.discount;

import com.avi.billingservice.model.domain.BillRequest;

/**
 * Discount strategy for implementing various types of discount schemes.
 */
public interface DiscountStrategy {
    double calculateDiscount(BillRequest bill);
}
