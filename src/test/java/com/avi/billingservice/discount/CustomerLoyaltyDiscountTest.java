package com.avi.billingservice.discount;

import com.avi.billingservice.model.domain.BillRequest;
import com.avi.billingservice.model.valueobjects.Category;
import com.avi.billingservice.model.valueobjects.Item;
import com.avi.billingservice.model.valueobjects.UserType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerLoyaltyDiscountTest {

    private final CustomerLoyaltyDiscount loyaltyDiscount = new CustomerLoyaltyDiscount();

    @Test
    void testCalculateDiscount_should_return_5_percent_discount_for_non_groceries_item() {
        var items = List.of(
                new Item(Category.OTHER, 1000)
        );
        var bill = new BillRequest(UserType.CUSTOMER, 3, 1100.0, "USD", "INR", items);

        double discount = loyaltyDiscount.calculateDiscount(bill);
        assertEquals(50.0, discount);
    }

    @Test
    void testCalculateDiscount_should_return_5_percent_discount_for_only_non_groceries_item() {
        var items = List.of(
                new Item(Category.OTHER, 1000),
                new Item(Category.GROCERIES, 500)
        );
        var bill = new BillRequest(UserType.CUSTOMER, 2.5, 1100.0, "USD", "INR", items);

        double discount = loyaltyDiscount.calculateDiscount(bill);
        assertEquals(50.0, discount);
    }

    @Test
    void testCalculateDiscount_should_return_0_discount_for_customer_newer_than_2_years() {
        var items = List.of(
                new Item(Category.GROCERIES, 500)
        );
        var bill = new BillRequest(UserType.CUSTOMER, 1, 1100.0, "USD", "INR", items);

        double discount = loyaltyDiscount.calculateDiscount(bill);
        assertEquals(0.0, discount);
    }


    @Test
    void testCalculateDiscount_should_return_0_discount_for_groceries_item() {

        var items = List.of(
                new Item(Category.GROCERIES, 500)
        );
        var bill = new BillRequest(UserType.CUSTOMER, 4, 1100.0, "USD", "INR", items);

        double discount = loyaltyDiscount.calculateDiscount(bill);
        assertEquals(0.0, discount);
    }

}