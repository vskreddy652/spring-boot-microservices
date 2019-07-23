package com.vskreddy652.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// http://localhost:8080/springbootwithsecurity/userlogin
// http://localhost:8080/springbootwithsecurity/adminlogin
	
@RestController
public class SpringBootRestController {
@RequestMapping("/userlogin")
public String userValidation() {
return "User: Successfully logged in!";
}
@RequestMapping("/adminlogin")
public String adminValidation() {
return "Admin: Successfully logged in!";
}
}