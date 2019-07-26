package com.ibm.mod.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
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

import com.eg.Application;
import com.eg.mod.model.TrainingDtls;
import com.eg.mod.model.UserDtls;
import com.eg.mod.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TrainingsControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	private String adminToken;
	private String userToken;
	private String mentorToken;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		UserDtls userDtlsObj = new UserDtls();
		userDtlsObj.setUserName("dummyUser");
		userDtlsObj.setPassword("dummyUser");
		userDtlsObj.setRole("ADMIN");
		adminToken = createGWTToken(userDtlsObj);
		userDtlsObj.setRole("USER");
		userToken = createGWTToken(userDtlsObj);
		userDtlsObj.setRole("MENTOR");
		mentorToken = createGWTToken(userDtlsObj);
	}

	@Test
	public void findAllTrainings() throws Exception {

		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/trainings/findAllTrainings" + queryString)
				.header("Authorization", "Bearer " + adminToken)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());

	}

	@Test
	public void findByMentorIdSkillId() throws Exception {

		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";
		Long mentorId = 5L, skillId = 5L;

		mockMvc.perform(MockMvcRequestBuilders
				.get("/trainings/findByMentorIdSkillId/{mentorId}/{skillId}" + queryString, mentorId, skillId)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());

	}

	@Test
	public void findProposedTrainings() throws Exception {

		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";
		Long mentorId = 1L;

		mockMvc.perform(MockMvcRequestBuilders
				.get("/trainings/findProposedTrainings/{mentorId}" + queryString, mentorId)
				.header("Authorization", "Bearer " + mentorToken)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());

	}

	@Test
	public void findTrainingByUserIdAndStatus() throws Exception {

		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";
		Long userId = 1L;
		String status = "PROPOSED";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/trainings/findByUserIdAndStatus/{userId}/{status}" + queryString, userId, status)
				.header("Authorization", "Bearer " + userToken)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());

	}

	@Test
	public void findTrainingByMentorIdAndStatus() throws Exception {

		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";
		Long mentorId = 1L;
		String status = "CONFIRMED;REJECTED";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/trainings/findByMentorIdAndStatus/{mentorId}/{status}" + queryString, mentorId, status)
				.header("Authorization", "Bearer " + mentorToken)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());

	}

	@Test
	public void findAvgRating() throws Exception {

		List<TrainingDtls> trainingDtlsList = new ArrayList<>();
		trainingDtlsList.add(new TrainingDtls(1L, 2L));
		trainingDtlsList.add(new TrainingDtls(1L, 3L));
		trainingDtlsList.add(new TrainingDtls(2L, 3L));

		mockMvc.perform(MockMvcRequestBuilders
				.post("/trainings/findAvgRating/1/2")
				.content(asJsonString(trainingDtlsList))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))));

	}

	@Test
	public void findById() throws Exception {

		Long trainingId = 3L;

		mockMvc.perform(MockMvcRequestBuilders
				.get("/trainings/findById/{id}", trainingId)
				.header("Authorization", "Bearer " + userToken)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

	}

	@Test
	public void proposeTraining() throws Exception {

		TrainingDtls trainingDtlsObj = new TrainingDtls();

		mockMvc.perform(MockMvcRequestBuilders
				.post("/trainings/proposeTraining")
				.header("Authorization", "Bearer " + userToken)
				.content(asJsonString(trainingDtlsObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Training proposal sent successfully"));

	}

	@Test
	public void approveTraining() throws Exception {

		Long trainingId = 10L;
		String trainingStatus = "CONFIRMED";

		mockMvc.perform(MockMvcRequestBuilders
				.put("/trainings/approveTraining/" + trainingId + "/" + trainingStatus)
				.header("Authorization", "Bearer " + mentorToken)
				.content("").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Training updated successfully"));

	}

	@Test
	public void updateTraining() throws Exception {

		Long trainingId = 10L;
		TrainingDtls trainingDtlsObj = new TrainingDtls();

		mockMvc.perform(MockMvcRequestBuilders
				.put("/trainings/updateTraining/" + trainingId)
				.header("Authorization", "Bearer " + userToken)
				.content(asJsonString(trainingDtlsObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Training updated successfully"));

	}

	@Test
	public void deleteTraining() throws Exception {

		Long trainingId = 10L;

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/trainings/deleteTraining/" + trainingId)
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Training deleted successfully"));

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