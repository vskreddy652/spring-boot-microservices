package com.vskreddy652.hystrix.hystrix.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/*
 Hystrix dashboard can be  opened form below URL
 http://localhost:8829/hystrix
 
 In text box enter
 http://localhost:8829/actuator/hystrix.stream
 */
@EnableHystrixDashboard
@EnableCircuitBreaker
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
