package com.avi.billingservice.web;

import com.avi.billingservice.model.domain.BillRequest;
import com.avi.billingservice.model.domain.BillResponse;
import com.avi.billingservice.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calculate")
public class BillController {

    private final BillingService billingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BillResponse> getBillAmountInTargetCurrencyWithDiscount(@RequestBody @Valid BillRequest bill) {
        var response = new BillResponse(billingService.getBillAfterAppliedDiscount(bill), bill.targetCurrency());
        return ResponseEntity.ok(response);
    }
}
