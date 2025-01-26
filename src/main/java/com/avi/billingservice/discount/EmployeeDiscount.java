package com.avi.billingservice.discount;

import com.avi.billingservice.model.domain.BillRequest;
import com.avi.billingservice.model.valueobjects.Category;
import com.avi.billingservice.model.valueobjects.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeDiscount implements DiscountStrategy {
    /**
     * Implements Employee discount strategy for EMPLOYEE user type.
     *
     * @param bill accepts bill.
     * @return Discount amount to be applied.
     */
    @Override
    public double calculateDiscount(BillRequest bill) {
        log.info("Applying applicable EMPLOYEE discounts.");
        double nonGroceryTotal = bill.items().stream()
                .filter(item -> !item.category().equals(Category.GROCERIES))
                .mapToDouble(Item::price)
                .sum();
        return nonGroceryTotal * 0.30;
    }
}
