package com.avi.billingservice.service;

import com.avi.billingservice.exception.CurrencyRateApiException;
import com.avi.billingservice.model.valueobjects.ExchangeRateApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * Provides realtime currency exchange rate implementation using <a href="https://www.exchangerate-api.com/">ExchangeRate-API</a>
 */
@Service
@Slf4j
public class ExchangeRateApiService implements CurrencyConversionRateService {
    private final RestTemplate restTemplate;
    private final String exchangeApiBaseUri;
    private final String apiKey;
    private final String apiName;

    public ExchangeRateApiService(RestTemplateBuilder restTemplateBuilder,
                                  @Value("${app.currency.exchange.api.baseUrl}")
                                  String exchangeApiBaseUri,
                                  @Value("${app.currency.exchange.api.key}")
                                  String apiKey,
                                  @Value("${app.currency.exchange.api.name}")
                                  String apiName) {
        this.restTemplate = restTemplateBuilder.build();
        this.exchangeApiBaseUri = exchangeApiBaseUri;
        this.apiKey = apiKey;
        this.apiName = apiName;
    }

    /**
     * Fetches realtime currency rate between currencies.
     *
     * @param originalCurrency Currency name of current currency.(which will be converted).
     * @param targetCurrency   Currency name of target currency.(Into which being converted).
     * @return Conversion rate.
     * @throws CurrencyRateApiException If error happened while fetching rate from external API.
     */
    @Override
    public double getConversionRate(String originalCurrency, String targetCurrency) {
        String url = exchangeApiBaseUri + apiKey + apiName + originalCurrency + "/" + targetCurrency;
        try {
            var response = restTemplate.getForEntity(url, ExchangeRateApiResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("External exchange Api to get currency rate is successful.");
                if (response.hasBody()) {
                    return Objects.requireNonNull(response.getBody()).conversionRate();
                }
                String message = "API returned empty response body.";
                log.error(message);
                throw new CurrencyRateApiException(message);
            }
            String message = "API failed with status code: %d".formatted(response.getStatusCode().value());
            log.error(message);
            throw new CurrencyRateApiException(message);
        } catch (RestClientException e) {
            String message = "Exception happened with error : %s".formatted(e.getMessage());
            log.error(message);
            throw new CurrencyRateApiException(message);
        }

    }
}
