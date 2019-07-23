package com.mnf.spring.security.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bank")
public class BankController {

    @GetMapping("anonymous")
    @PreAuthorize("permitAll()")
    public String anonymously() {
        return "Hello, World!";
    }

    @GetMapping("has-role")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String hasRole() {
        return "Hello, World!";
    }

}
