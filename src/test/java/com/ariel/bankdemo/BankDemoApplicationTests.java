package com.ariel.bankdemo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class BankDemoApplicationTests {

	public static final long CUSTOMER_ID = 10L;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testHappyPathIntegrationTest() throws Exception {
		// We create an account for one existing customer
		final MvcResult resultCreatingAccount = mockMvc.perform(post("/api/customer/{customerId}/account", CUSTOMER_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
						{
							"initialCredit": 10.50
						}
						""")
				).andExpect(status().isOk()).andReturn();

		final String accountId = resultCreatingAccount.getResponse().getContentAsString();

		assertThat(accountId).isNotNull();

		mockMvc.perform(get("/api/customer/{customerId}/summary", CUSTOMER_ID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId", Matchers.not(Matchers.empty())))
				.andExpect(jsonPath("$.name", Matchers.not(Matchers.empty())))
				.andExpect(jsonPath("$.surname", Matchers.not(Matchers.empty())))
				.andExpect(jsonPath("$.accounts[0].accountId", Matchers.is(Integer.valueOf(accountId))))
				.andExpect(jsonPath("$.accounts[0].balance", Matchers.is(10.50)))
				.andExpect(jsonPath("$.accounts[0].transactions[0].transactionId", Matchers.not(Matchers.empty())))
				.andExpect(jsonPath("$.accounts[0].transactions[0].amount", Matchers.is(10.50)))
				.andExpect(jsonPath("$.accounts[0].transactions[0].when", Matchers.not(Matchers.empty())));

	}

}
