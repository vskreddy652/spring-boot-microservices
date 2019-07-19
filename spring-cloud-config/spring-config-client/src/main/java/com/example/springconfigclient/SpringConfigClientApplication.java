package com.example.springconfigclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 Steps 
 run server using "mvn spring-boot:run"
 run client using "mvn spring-boot:run"
 hit on Browser, http://localhost:8080/msg
 Now make changes to development profile properties file:
 git add ., git commit -m "nothing"
 
 POST http://localhost:8080/refresh from PostMan  
 
 again visit http://localhost:8080/msg and check if modified 
 */
@SpringBootApplication
public class SpringConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringConfigClientApplication.class, args);
	}
}

@RefreshScope
@RestController
class MessageRestController {

	@Value("${msg:Hello world - Config Server is not working..pelase check}")
	private String msg;

	@RequestMapping("/msg")
	String getMsg() {
		return this.msg;
	}
}
