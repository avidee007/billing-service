package com.avi.billingservice.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExchangeRateApiResponse(String result,
                                      @JsonProperty("base_code")
                                      String baseCode,
                                      @JsonProperty("target_code")
                                      String targetCode,
                                      @JsonProperty("conversion_rate")
                                      double conversionRate) {
}
