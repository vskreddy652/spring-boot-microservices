package com.mnf.spring.security.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book")
public class BookController {

    @GetMapping("anonymous")
    @Secured("ROLE_ANONYMOUS")
    public String anonymously() {
        return "Hello, World!";
    }

    @GetMapping("has-role")
    @Secured("ROLE_ADMIN")
    public String hasRole() {
        return "Hello, World!";
    }

}
