package com.avi.billingservice.web;

import com.avi.billingservice.service.BillingService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class BillControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private BillingService billingService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_200_with_valid_payload() throws Exception {
        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getValidPayload()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("INR"));
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void getBillAmount_should_return_403_with_guest_user() throws Exception {
        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getValidPayload()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_if_api_url_is_incorrect() throws Exception {
        mockMvc.perform(
                        post("/api/v1/incorrect")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getValidPayload()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_with_null_user_type() throws Exception {

        var withNullEmployType = """
                {
                	"tenure": 1,
                	"totalAmount": 500,
                	"originalCurrency": "USD",
                	"targetCurrency": "USD",
                	"items": [
                		{
                			"category": "OTHER",
                			"price": 500
                		}
                	]
                }
                """;
        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withNullEmployType))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("userType"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", Matchers.nullValue()))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("userType can not null or empty"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_when_totalAmount_is_negative() throws Exception {

        var withNegativeTotalAmount = """
                {
                    "userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": -200,
                	"originalCurrency": "USD",
                	"targetCurrency": "INR",
                	"items": [
                		{
                			"category": "OTHER",
                			"price": 500
                		}
                	]
                }
                """;
        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withNegativeTotalAmount))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("totalAmount"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(-200))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("must be greater than 0"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_when_totalAmount_is_zero() throws Exception {

        var withZeroTotalAmount = """
                {
                    "userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": 0,
                	"originalCurrency": "USD",
                	"targetCurrency": "INR",
                	"items": [
                		{
                			"category": "OTHER",
                			"price": 500
                		}
                	]
                }
                """;

        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withZeroTotalAmount))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("totalAmount"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(0))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("must be greater than 0"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_when_originalCurrency_is_null() throws Exception {

        var withNullOriginalCurrency = """
                {
                    "userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": 100,
                	"targetCurrency": "INR",
                	"items": [
                		{
                			"category": "OTHER",
                			"price": 500
                		}
                	]
                }
                """;

        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withNullOriginalCurrency))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("originalCurrency"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", Matchers.nullValue()))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("originalCurrency can not be null or empty"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_when_originalCurrency_is_empty() throws Exception {

        var withNullOriginalCurrency = """
                {
                    "userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": 100,
                	"originalCurrency": "",
                	"targetCurrency": "INR",
                	"items": [
                		{
                			"category": "OTHER",
                			"price": 500
                		}
                	]
                }
                """;

        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withNullOriginalCurrency))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("originalCurrency"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(""))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("originalCurrency can not be null or empty"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_when_targetCurrency_is_empty() throws Exception {

        var withTargetCurrency = """
                {
                    "userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": 100,
                	"originalCurrency": "INR",
                	"items": [
                		{
                			"category": "OTHER",
                			"price": 500
                		}
                	]
                }
                """;

        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withTargetCurrency))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("targetCurrency"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", Matchers.nullValue()))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("targetCurrency can not be null or empty"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_400_when_item_list_empty() throws Exception {

        var withTargetCurrency = """
                {
                    "userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": 100,
                	"originalCurrency": "INR",
                	"targetCurrency": "INR",
                	"items": []
                }
                """;

        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withTargetCurrency))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].fieldName").value("items"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", Matchers.empty()))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("Must have minimum 1 item."));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBillAmount_should_return_500_when_currency_rate_api_throws_exception() throws Exception {

        var withInvalidCurrencyType = """
                {
                    "userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": 100,
                	"originalCurrency": "INVALID",
                	"targetCurrency": "WRONG",
                	"items": [
                	{
                			"category": "OTHER",
                			"price": 1000
                		}
                	]
                }
                """;

        mockMvc.perform(
                        post("/api/v1/calculate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(withInvalidCurrencyType))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(Matchers.containsString("Exception happened with error :")));
    }


    private static String getValidPayload() {
        return """
                {
                	"userType": "EMPLOYEE",
                	"tenure": 1,
                	"totalAmount": 1000,
                	"originalCurrency": "USD",
                	"targetCurrency": "INR",
                	"items": [
                		{
                			"category": "OTHER",
                			"price": 1000
                		}
                	]
                }
                """;
    }


}