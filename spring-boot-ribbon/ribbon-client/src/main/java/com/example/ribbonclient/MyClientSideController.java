package com.example.ribbonclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MyClientSideController {
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/client/frontend")
	//public String hi(@PathVariable String id) {
	public String hi() {
		System.out.println("in ribbon-client id is ");
		String randomString = this.restTemplate.getForObject("http://server/backend", String.class);
		System.out.println("in ribbon-client, after invoking server "+randomString);
		return "Server Response :: " + randomString;
	}
}
