/**
 * 
 */
package com.mentorondemand.SkillsTechnologies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/skillstech")
public class SkillsTechnologiesController {
		/*
		 To Access from Zuul API Gateway
	 http://localhost:8989/mentorportal/skillTechService/skillstech/hello
*/
	@GetMapping("/hello")
	public String sayHello() {
		return "Hello Hello";
	}
	/*
	@GetMapping("/Skill")
	public List<SkillsTechnologiesRepositorydao>getSkill(){
		
		return null;//TODO
	}
	
	@GetMapping("/SearchSkill")
	public List<SkillsTechnologiesRepositorydao>searchSkills(){
		
		return null;//TODO
	}
	*/
	


	//Input
	/*
	 * id searchString
	 */
//Output
	/*
	 * Skill Matching skills array
	 */
//Note: If search string is empty retrieve all Skills

}
