package com.avi.billingservice.service;

import com.avi.billingservice.model.domain.BillRequest;

public interface BillingService {

    double getBillAfterAppliedDiscount(BillRequest bill);
}
