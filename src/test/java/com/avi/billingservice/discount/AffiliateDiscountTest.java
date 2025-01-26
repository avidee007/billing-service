package com.avi.billingservice.discount;

import com.avi.billingservice.model.domain.BillRequest;
import com.avi.billingservice.model.valueobjects.Category;
import com.avi.billingservice.model.valueobjects.Item;
import com.avi.billingservice.model.valueobjects.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AffiliateDiscountTest {

    private final AffiliateDiscount affiliateDiscount = new AffiliateDiscount();

    @Test
    void testCalculateDiscount_should_return_10_percent_discount_for_non_groceries_item() {
        var items = List.of(
                new Item(Category.OTHER, 1000)
        );
        var bill = new BillRequest(UserType.AFFILIATE, 2, 1100.0, "USD", "INR", items);

        double discount = affiliateDiscount.calculateDiscount(bill);
        assertEquals(100.0, discount);
    }

    @Test
    void testCalculateDiscount_should_return_10_percent_discount_for_only_non_groceries_item() {
        var items = List.of(
                new Item(Category.OTHER, 1000),
                new Item(Category.GROCERIES, 500)
        );
        var bill = new BillRequest(UserType.AFFILIATE, 2, 1100.0, "USD", "INR", items);

        double discount = affiliateDiscount.calculateDiscount(bill);
        assertEquals(100.0, discount);
    }


    @Test
    void testCalculateDiscount_should_return_0_discount_for_groceries_item() {
        var items = List.of(
                new Item(Category.GROCERIES, 500)
        );
        var bill = new BillRequest(UserType.AFFILIATE, 1, 1100.0, "USD", "INR", items);

        double discount = affiliateDiscount.calculateDiscount(bill);
        assertEquals(0.0, discount);
    }

}