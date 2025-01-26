package com.avi.billingservice.discount;

import com.avi.billingservice.model.valueobjects.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DiscountStrategyFactoryTest {
    @Mock
    private EmployeeDiscount employeeDiscount;

    @Mock
    private AffiliateDiscount affiliateDiscount;

    @Mock
    private CustomerLoyaltyDiscount loyaltyDiscount;

    @InjectMocks
    private DiscountStrategyFactory discountStrategyFactory;

    @Test
    void testGetDiscountStrategyForEmployee() {
        var strategy = discountStrategyFactory.getDiscountStrategy(UserType.EMPLOYEE);
        assertEquals(employeeDiscount, strategy);
    }

    @Test
    void testGetDiscountStrategyForAffiliate() {
        var strategy = discountStrategyFactory.getDiscountStrategy(UserType.AFFILIATE);
        assertEquals(affiliateDiscount, strategy);
    }

    @Test
    void testGetDiscountStrategyForCustomer() {
        var strategy = discountStrategyFactory.getDiscountStrategy(UserType.CUSTOMER);
        assertEquals(loyaltyDiscount, strategy);
    }

}