package com.mnf.spring.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @ImportResource("classpath:spring-security-config.xml")
public class Run {

    public static void main(String[] args) {
        SpringApplication.run(Run.class, args);
    }
}

/*
http://localhost:8080/bank/has-role
For above URL try with user and admin as well. Admin can open, but not User
*/