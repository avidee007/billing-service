package com.avi.billingservice.service;

import com.avi.billingservice.model.valueobjects.ExchangeRateApiResponse;
import com.avi.billingservice.exception.CurrencyRateApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @InjectMocks
    private ExchangeRateService service;

    private final String exchangeApiBaseUri = "https://api.example.com/";
    private final String apiKey = "test-api-key";
    private final String apiName = "/convert/";

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        service = new ExchangeRateService(restTemplateBuilder, exchangeApiBaseUri, apiKey, apiName);
    }

    @Test
    void testGetCurrencyGetConversionRate_Success() {
        String originalCurrency = "USD";
        String targetCurrency = "EUR";
        String url = exchangeApiBaseUri + apiKey + apiName + originalCurrency + "/" + targetCurrency;

        ExchangeRateApiResponse mockResponse = new ExchangeRateApiResponse("success", originalCurrency, targetCurrency, 0.85);
        ResponseEntity<ExchangeRateApiResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(url, ExchangeRateApiResponse.class)).thenReturn(responseEntity);

        double conversionRate = service.getConversionRate(originalCurrency, targetCurrency);

        assertEquals(0.85, conversionRate);
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateApiResponse.class);
    }

    @Test
    void testGetCurrencyGetConversionRate_should_throw_CurrencyExchangeApiException_when_status_not_200() {
        String originalCurrency = "USD";
        String targetCurrency = "EUR";
        String url = exchangeApiBaseUri + apiKey + apiName + originalCurrency + "/" + targetCurrency;

        ResponseEntity<ExchangeRateApiResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.getForEntity(url, ExchangeRateApiResponse.class)).thenReturn(responseEntity);

        CurrencyRateApiException exception = assertThrows(CurrencyRateApiException.class,
                () -> service.getConversionRate(originalCurrency, targetCurrency));

        assertTrue(exception.getMessage().contains("API failed with status code: 500"));
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateApiResponse.class);
    }

    @Test
    void testGetCurrencyGetConversionRate_should_throw_CurrencyExchangeApiException_if_RestClientException_happened() {
        String originalCurrency = "USD";
        String targetCurrency = "EUR";
        String url = exchangeApiBaseUri + apiKey + apiName + originalCurrency + "/" + targetCurrency;

        when(restTemplate.getForEntity(url, ExchangeRateApiResponse.class)).thenThrow(new RestClientException("Connection timeout"));

        CurrencyRateApiException exception = assertThrows(CurrencyRateApiException.class,
                () -> service.getConversionRate(originalCurrency, targetCurrency));

        assertTrue(exception.getMessage().contains("Exception happened with error : Connection timeout"));
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateApiResponse.class);
    }

    @Test
    void testGetCurrencyGetConversionRate_should_throw_CurrencyExchangeApiException_if_NoBodyInResponse() {
        String originalCurrency = "USD";
        String targetCurrency = "EUR";
        String url = exchangeApiBaseUri + apiKey + apiName + originalCurrency + "/" + targetCurrency;

        ResponseEntity<ExchangeRateApiResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.getForEntity(url, ExchangeRateApiResponse.class)).thenReturn(responseEntity);

        CurrencyRateApiException exception = assertThrows(CurrencyRateApiException.class,
                () -> service.getConversionRate(originalCurrency, targetCurrency));

        assertTrue(exception.getMessage().contains("API returned empty response body."));
        verify(restTemplate, times(1)).getForEntity(url, ExchangeRateApiResponse.class);
    }
}
