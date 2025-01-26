package com.avi.billingservice.service;

import com.avi.billingservice.discount.DiscountStrategy;
import com.avi.billingservice.discount.DiscountStrategyFactory;
import com.avi.billingservice.model.domain.BillRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingServiceImpl implements BillingService {

    private final DiscountStrategyFactory discountStrategyFactory;
    private final CurrencyConversionRateService exchangeService;


    /**
     * Does discount calculation, bill calculation and currency conversion for given bill.
     *
     * @param bill Contains bill details.
     * @return Final amount to be paid after applying application discounts and currency conversion.
     */
    @Override
    public double getBillAfterAppliedDiscount(BillRequest bill) {
        double discount = getDiscountByUserType(bill);
        double billDiscount = getBillDiscount(bill);
        double totalAmountAfterDiscount = bill.totalAmount() - (discount + billDiscount);
        log.info("Final bill after discount: {}", totalAmountAfterDiscount);
        return convertAmount(bill, totalAmountAfterDiscount);
    }

    private double convertAmount(BillRequest bill, double totalAmountAfterDiscount) {
        double conversionRate = 1.0;
        if (!bill.originalCurrency().equals(bill.targetCurrency())) {
            conversionRate = exchangeService.getConversionRate(bill.originalCurrency(), bill.targetCurrency());
        }
        log.info("Current currency rate for converting {} into {} is {}", bill.originalCurrency(),
                bill.targetCurrency(), conversionRate);
        double convertedAmount = totalAmountAfterDiscount * conversionRate;
        log.info("Final amount after currency conversion: {}", convertedAmount);
        return convertedAmount;
    }

    private double getBillDiscount(BillRequest bill) {
        double billDiscount = Math.floor(bill.totalAmount() / 100) * 5;
        log.info("Bill Discount calculated : {}", billDiscount);
        return billDiscount;
    }

    private double getDiscountByUserType(BillRequest bill) {
        DiscountStrategy discountStrategy = discountStrategyFactory.getDiscountStrategy(bill.userType());
        double discount = discountStrategy.calculateDiscount(bill);
        log.info("Discount for non grocery items calculated : {}", discount);
        return discount;

    }
}
