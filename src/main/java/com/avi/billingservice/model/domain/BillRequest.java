package com.avi.billingservice.model.domain;

import com.avi.billingservice.model.valueobjects.Item;
import com.avi.billingservice.model.valueobjects.UserType;
import jakarta.validation.constraints.*;

import java.util.List;

public record BillRequest(
        @NotNull(message = "userType can not null or empty")
        UserType userType,
        @PositiveOrZero
        double tenure,
        @Positive
        double totalAmount,
        @NotBlank(message = "originalCurrency can not be null or empty")
        String originalCurrency,
        @NotBlank(message = "targetCurrency can not be null or empty")
        String targetCurrency,
        @Size(min = 1, message = "Must have minimum 1 item.")
        List<Item> items) {
}
