package com.vskreddy652.securitydb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringController {
@RequestMapping("/login")
public String userValidation() {
return "User: Successfully logged in!";
}
}
// URL: http://localhost:8080/springbootauth/login