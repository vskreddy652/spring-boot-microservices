package com.ibm.mod.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

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
import com.eg.mod.entity.User;
import com.eg.mod.model.UserDtls;
import com.eg.mod.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void signin() throws Exception {

		User userObj = new User();
		userObj.setUserName("arnabmca2006@gmail.com");
		userObj.setPassword("arnabray");

		mockMvc.perform(MockMvcRequestBuilders
				.post("/users/signin")
				.content(asJsonString(userObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User authenticated successfully"));

	}

	@Test
	public void signup() throws Exception {

		User userObj = new User();
		userObj.setUserName("arnabmca2006@gmail.com");
		userObj.setPassword("arnabray");
		userObj.setFirstName("Arnab");
		userObj.setLastName("Ray");
		userObj.setContactNumber(9434568906L);
		userObj.setRole("USER");

		mockMvc.perform(MockMvcRequestBuilders
				.post("/users/signup")
				.content(asJsonString(userObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Signup confirmation mail sent to your mail"));

	}

	@Test
	public void forgetPassword() throws Exception {

		String userName = "sangita_adak2006@gmail.com";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/forgetPassword/{userName}", userName)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Reset password mail sent to your mail and will valid for next 15 minutes"));

	}

	@Test
	public void blockUnblockUser() throws Exception {

		Long userId = 3L;
		User userObj = new User();
		userObj.setUserName("dummyUser");
		userObj.setPassword("dummyUser");
		userObj.setRole("ADMIN");
		String token = createGWTToken(userObj);

		mockMvc.perform(MockMvcRequestBuilders
				.put("/users/blockUnblockUser/" + userId + "/" + true)
				.header("Authorization", "Bearer " + token)
				.content("")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", containsString("successfully")));

	}

	@Test
	public void updateProfile() throws Exception {

		User userObj = new User();
		userObj.setUserName("dummyUser");
		userObj.setPassword("dummyUser");
		userObj.setRole("USER");
		String token = createGWTToken(userObj);

		Long userId = 2L;
		userObj = new User();
		userObj.setPassword("arnabray");
		userObj.setFirstName("Arnab Kumar");
		userObj.setLastName("Ray");
		userObj.setContactNumber(9434568906L);

		mockMvc.perform(MockMvcRequestBuilders
				.put("/users/updateProfile/" + userId)
				.header("Authorization", "Bearer " + token)
				.content(asJsonString(userObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Profile updated successfully"));

	}

	@Test
	public void deleteProfile() throws Exception {

		Long userId = 10L;
		User userObj = new User();
		userObj.setUserName("dummyUser");
		userObj.setPassword("dummyUser");
		userObj.setRole("ADMIN");
		String token = createGWTToken(userObj);

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/users/deleteProfile/" + userId)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Profile deleted successfully"));

	}

	@Test
	public void findAllUsers() throws Exception {

		User userObj = new User();
		userObj.setUserName("dummyUser");
		userObj.setPassword("dummyUser");
		userObj.setRole("ADMIN");
		String token = createGWTToken(userObj);
		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/findAllUsers" + queryString)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());

	}

	@Test
	public void findById() throws Exception {

		Long userId = 3L;
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/findById/{id}", userId)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}

	@Test
	public void findByName() throws Exception {

		String userName = "arnabmca2006@gmail.com";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/users/findByName/{userName}", userName)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

	}

	// below two rest service called from jsp pages through ajax

	@Test
	public void confirmUser() throws Exception {

		UserDtls userDtlsObj = new UserDtls();
		userDtlsObj.setUserName("arnabmca2006@gmail.com");
		userDtlsObj.setToken("ASD345");

		mockMvc.perform(MockMvcRequestBuilders
				.post("/users/confirmUser")
				.content(asJsonString(userDtlsObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void resetPassword() throws Exception {

		UserDtls userDtlsObj = new UserDtls();
		userDtlsObj.setUserName("arnabmca2006@gmail.com");
		userDtlsObj.setPassword("arnabray");

		mockMvc.perform(MockMvcRequestBuilders
				.post("/users/resetPassword")
				.content(asJsonString(userDtlsObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andExpect(status().isOk());

	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String createGWTToken(User userDtlsObj) {

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