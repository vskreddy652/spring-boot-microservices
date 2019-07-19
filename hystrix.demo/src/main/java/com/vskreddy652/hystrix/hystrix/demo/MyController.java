package com.vskreddy652.hystrix.hystrix.demo;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

//http://localhost:8921/rest/hello

@RestController
@RequestMapping("/rest")
public class MyController {
	@HystrixCommand(fallbackMethod="myMet", commandKey="hello", groupKey="hello")
	//@HystrixCommand
	@GetMapping("hello")
	public String hello() 
	{
		//search mentors by skill id, list of mentors(23,28,59, mentor's calendar
		
		if(RandomUtils.nextBoolean()) //invoke User service pass 23
		{
			throw new RuntimeException("Failedd");
		}
		
		//invoke Technology service
		
		//invoke Training Service for avg rating, etc...
		
		return "This is Hello World";
	}
	
	@HystrixCommand(fallbackMethod="myMet", commandKey="hello1", groupKey="hello1")
	//@HystrixCommand
	@GetMapping("hello1")
	public String hello1()
	{
		if(RandomUtils.nextBoolean())
		{
			throw new RuntimeException("Failedd1");
		}
		
		return "This is also Hello World";
	}
	
	public String myMet()
	{
		return "From Failedd Method";
	}
}
