package com.avi.billingservice.discount;

import com.avi.billingservice.model.valueobjects.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Discount strategy factory to encapsulate getting different discount strategies based upon user type.
 */
@Component
@RequiredArgsConstructor
public class DiscountStrategyFactory {
    private final EmployeeDiscount employeeDiscount;
    private final AffiliateDiscount affiliateDiscount;
    private final CustomerLoyaltyDiscount loyaltyDiscount;

    public DiscountStrategy getDiscountStrategy(UserType userType) {
        return switch (userType) {
            case EMPLOYEE -> employeeDiscount;
            case AFFILIATE -> affiliateDiscount;
            case CUSTOMER -> loyaltyDiscount;
        };
    }
}
