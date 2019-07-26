package com.ibm.mod;

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
import org.springframework.test.context.ContextConfiguration; 
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner; 
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders; 
import org.springframework.web.context.WebApplicationContext;

import com.eg.Application;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(classes = Application.class) 
@SpringBootTest 
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 

public class MentorCalendarControllerTest {

	@Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	} 

	@Test
	public void addCalendarEntry() throws Exception {

		Long mentorId = 1L, skillId = 5L;
		String startDate = "2019-01-12", endDate = "2019-01-20";
		String startTime = "12:00", endTime = "16:00";
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/search/addCalendarEntry/" + mentorId + "/" + skillId + "/" + startDate + "/" + endDate + "/" + startTime + "/" + endTime)
				.content(asJsonString(""))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Calendar-entry added successfully"));
	}
	
	@Test
	public void findMentorCalendarByMentorId()  throws Exception {
		
		Long mentorId = 1L;
		String startDate = "2019-01-12", endDate = "2019-01-20";
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/search/findPaymentDtlsByDateRange/{mentorId}/{startDate}/{endDate}" + mentorId, startDate, endDate)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}