package com.ibm.mod.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashSet;
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
import com.eg.mod.model.SkillDtls;
import com.eg.mod.model.UserDtls;
import com.eg.mod.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SkillsControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	private String token;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		UserDtls userDtlsObj = new UserDtls();
		userDtlsObj.setUserName("dummyUser");
		userDtlsObj.setPassword("dummyUser");
		userDtlsObj.setRole("ADMIN");
		token = createGWTToken(userDtlsObj);
	}

	@Test
	public void findById() throws Exception {

		Long skillId = 3L;
		
		mockMvc.perform(
				MockMvcRequestBuilders
				.get("/skills/findById/{skillId}", skillId)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

	}

	@Test
	public void findByName() throws Exception {

		String skillName = "Spring 4";

		mockMvc.perform(MockMvcRequestBuilders
				.get("/skills/findByName/{skillName}", skillName)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}

	@Test
	public void findByLikeName() throws Exception {

		String skillName = "%Spring%";
		mockMvc.perform(MockMvcRequestBuilders
				.get("/skills/findByLikeName/{skillName}", skillName)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))));

	}

	@Test
	public void findAllSkills() throws Exception {

		String queryString = "?orderBy=id&direction=ASC&page=0&size=10";

		mockMvc.perform(
				MockMvcRequestBuilders
				.get("/skills/findAllSkills" + queryString)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());

	}

	@Test
	public void addSkill() throws Exception {

		SkillDtls skillDtlsObj = new SkillDtls();
		skillDtlsObj.setName("testSkill");
		skillDtlsObj.setPrerequisites("test");
		skillDtlsObj.setToc("table of content");

		mockMvc.perform(MockMvcRequestBuilders
				.post("/skills/addSkill")
				.header("Authorization", "Bearer " + token)
				.content(asJsonString(skillDtlsObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Skill added successfully"));

	}

	@Test
	public void updateSkill() throws Exception {

		Long skillId = 8L;
		SkillDtls skillDtlsObj = new SkillDtls();
		skillDtlsObj.setPrerequisites("test test");
		skillDtlsObj.setToc("table of content");
		
		mockMvc.perform(MockMvcRequestBuilders
				.put("/users/updateSkill/" + skillId)
				.header("Authorization", "Bearer " + token)
				.content(asJsonString(skillDtlsObj))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Skill updated successfully"));

	}

	@Test
	public void deleteSkills() throws Exception {

		Long skillId = 8L;

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/skills/deleteSkills/" + skillId)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andDo(print())
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Skill deleted successfully"));
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