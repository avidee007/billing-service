package com.avi.billingservice;

import com.avi.billingservice.service.BillingService;
import com.avi.billingservice.web.BillController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BillingServiceApplicationTest {
	@Autowired
	private BillController billController;
	@Autowired
	private BillingService billingService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(billController);
		Assertions.assertNotNull(billingService);
	}

}
