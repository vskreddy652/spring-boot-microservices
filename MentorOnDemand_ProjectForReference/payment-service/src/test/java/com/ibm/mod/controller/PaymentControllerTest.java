package com.ibm.mod.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before; 
import org.junit.FixMethodOrder; 
import org.junit.Test; 
import org.junit.runner.RunWith; 
import org.junit.runners.MethodSorters; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.context.SpringBootTest; 
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration; 
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner; 
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders; 
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import com.eg.Application;
import com.eg.mod.model.PaymentDtls;
import com.eg.mod.model.UserDtls;
import com.eg.mod.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(classes = Application.class) 
@SpringBootTest 
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 

public class PaymentControllerTest {

	@Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();		
	} 
	

	@Test
	public void findPaymentDtlsByDateRange() throws Exception {

		UserDtls userDtlsObj = new UserDtls();
		userDtlsObj.setUserName("dummyUser");
		userDtlsObj.setPassword("dummyUser");
		userDtlsObj.setRole("ADMIN");
		String token = createGWTToken(userDtlsObj);
		
		Long mentorId = 1L;
		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";
		String startDate = "2019-01-12", endDate = "2019-01-20";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/payments/findPaymentDtlsByDateRange/{mentorId}/{startDate}/{endDate}" + queryString, mentorId, startDate, endDate)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());
	}

	@Test
	public void findTotalPaidAmountByMentorId() throws Exception {

		Long mentorId = 5L, trainingId = 5L;

		mockMvc.perform(MockMvcRequestBuilders
				.get("/payments/findTotalPaidAmountByMentorId/" + mentorId + "/" + trainingId)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalAmountToMentor").exists());
	}

	@Test
	public void addPayment() throws Exception {

		Long mentorId = 5L, trainingId = 5L;
		PaymentDtls paymentDtlsObj = new PaymentDtls();
		Float amountToMentor = 2500.0f;
		paymentDtlsObj.setAmount(amountToMentor);
		paymentDtlsObj.setRemarks("Amount " + amountToMentor + " paid to mentor");
		paymentDtlsObj.setTxnType("CR");

		mockMvc.perform(MockMvcRequestBuilders
				.post("/payments/addPayment/" + mentorId + "/" + trainingId)
				.content(asJsonString(paymentDtlsObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Payment added successfully"));
	}

	@Test
	public void findPaymentCommission() throws Exception {

		Long paymentCommId = 1L;

		mockMvc.perform(MockMvcRequestBuilders
				.get("/payments/findPaymentCommission/{id}", paymentCommId)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.commissionPercent").exists());
		//Assert.assertNotNull(result.getModelAndView());
		//MvcResult result = .andReturn()
	}

	@Test
	public void updatePayment() throws Exception {

		Long paymentCommId = 1L;
		Float commissionPercent = 13.0f;

		mockMvc.perform(MockMvcRequestBuilders
				.put("/payments/updatePaymentCommission/" + paymentCommId + "/" + commissionPercent)
				.content("")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", containsString("Payment Commission updated successfully")));
	}

	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String createGWTToken(UserDtls userDtlsObj) {

		String token = null;
		TokenProvider jwtTokenUtil = new TokenProvider();
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + userDtlsObj.getRole()));
		final Authentication authentication = new UsernamePasswordAuthenticationToken(userDtlsObj, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		token = jwtTokenUtil.generateToken(authentication);

		return token;
	}
}