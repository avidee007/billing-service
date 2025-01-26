package com.avi.billingservice.service;

import com.avi.billingservice.discount.DiscountStrategy;
import com.avi.billingservice.discount.DiscountStrategyFactory;
import com.avi.billingservice.model.domain.BillRequest;
import com.avi.billingservice.model.valueobjects.Category;
import com.avi.billingservice.model.valueobjects.Item;
import com.avi.billingservice.model.valueobjects.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceImplTest {

    @Mock
    private DiscountStrategyFactory discountStrategyFactory;

    @Mock
    private DiscountStrategy discountStrategy;

    @Mock
    private ExchangeRateService exchangeService;

    @InjectMocks
    private BillingServiceImpl billingService;

    @Test
    void testGetBillAfterAppliedDiscount_for_employees() {
        var items = List.of(
                new Item(Category.OTHER, 1000),
                new Item(Category.GROCERIES, 500)
        );
        var bill = new BillRequest(UserType.EMPLOYEE, 3, 1500, "USD", "EUR", items);

        when(discountStrategyFactory.getDiscountStrategy(UserType.EMPLOYEE)).thenReturn(discountStrategy);
        when(discountStrategy.calculateDiscount(bill)).thenReturn(300.0);
        when(exchangeService.getConversionRate("USD", "EUR")).thenReturn(0.9);


        double finalBill = billingService.getBillAfterAppliedDiscount(bill);


        double expectedBillAfterDiscount = 1500 - (300 + 75);
        double expectedConvertedAmount = expectedBillAfterDiscount * 0.9;

        assertEquals(expectedConvertedAmount, finalBill);
        verify(discountStrategyFactory).getDiscountStrategy(UserType.EMPLOYEE);
        verify(discountStrategy).calculateDiscount(bill);
        verify(exchangeService).getConversionRate("USD", "EUR");
    }

    @Test
    void getBillAfterAppliedDiscount_for_affiliate_user() {
        var items = List.of(
                new Item(Category.OTHER, 450),
                new Item(Category.OTHER, 50.0)
        );
        var bill = new BillRequest(UserType.AFFILIATE, 2, 500, "USD", "EUR", items);

        when(discountStrategyFactory.getDiscountStrategy(UserType.AFFILIATE)).thenReturn(discountStrategy);
        when(discountStrategy.calculateDiscount(bill)).thenReturn(50.0);
        when(exchangeService.getConversionRate("USD", "EUR")).thenReturn(0.9);

        double finalBill = billingService.getBillAfterAppliedDiscount(bill);


        double expectedBillAfterDiscount = 500 - (50 + 25);
        double expectedConvertedAmount = expectedBillAfterDiscount * 0.9;
        assertEquals(expectedConvertedAmount, finalBill);

        verify(discountStrategyFactory).getDiscountStrategy(UserType.AFFILIATE);
        verify(discountStrategy).calculateDiscount(bill);
        verify(exchangeService).getConversionRate("USD", "EUR");
    }

    @Test
    void getBillAfterAppliedDiscount_for_customer_user() {

        var items = List.of(new Item(Category.OTHER, 800));
        var bill = new BillRequest(UserType.CUSTOMER, 1, 800, "USD", "INR", items);

        when(discountStrategyFactory.getDiscountStrategy(UserType.CUSTOMER)).thenReturn(discountStrategy);
        when(discountStrategy.calculateDiscount(bill)).thenReturn(0.0);
        when(exchangeService.getConversionRate("USD", "INR")).thenReturn(80.0);


        double finalBill = billingService.getBillAfterAppliedDiscount(bill);


        double expectedBillAfterDiscount = 800 - (40);
        double expectedConvertedAmount = expectedBillAfterDiscount * 80.0;
        assertEquals(expectedConvertedAmount, finalBill);

        verify(discountStrategyFactory).getDiscountStrategy(UserType.CUSTOMER);
        verify(discountStrategy).calculateDiscount(bill);
        verify(exchangeService).getConversionRate("USD", "INR");
    }

    @Test
    void getBillAfterAppliedDiscount_should_not_call_currency_conversion_api_if_both_currency_is_same() {
        var items = List.of(
                new Item(Category.OTHER, 450),
                new Item(Category.OTHER, 50.0)
        );
        var bill = new BillRequest(UserType.AFFILIATE, 2, 500, "USD", "USD", items);

        when(discountStrategyFactory.getDiscountStrategy(UserType.AFFILIATE)).thenReturn(discountStrategy);
        when(discountStrategy.calculateDiscount(bill)).thenReturn(50.0);

        double finalBill = billingService.getBillAfterAppliedDiscount(bill);


        double expectedBillAfterDiscount = 500 - (50 + 25);
        assertEquals(expectedBillAfterDiscount, finalBill);

        verify(discountStrategyFactory).getDiscountStrategy(UserType.AFFILIATE);
        verify(discountStrategy).calculateDiscount(bill);
        verifyNoInteractions(exchangeService);
    }
}
