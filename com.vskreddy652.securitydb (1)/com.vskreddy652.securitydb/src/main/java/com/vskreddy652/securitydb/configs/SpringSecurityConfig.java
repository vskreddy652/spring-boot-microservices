package com.vskreddy652.securitydb.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private DataSource ds;
 
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource ds() {
        return DataSourceBuilder.create().build();
    }
 
    /* Spring Security Configurations Start */
    @Autowired
    public void configureAMBuilder(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(ds)
            .authoritiesByUsernameQuery("select email, role FROM USERS where email=?")
            .usersByUsernameQuery("select email,userPassword, 1 FROM USERS where email=?");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
            .authorizeRequests()
            .anyRequest().authenticated();
        http.csrf().disable();
    }
    /* Spring Security Configurations End */
}
 
/*
 
  	CREATE TABLE Users (
     id int,
     userName varchar(255),
     email varchar(255),
     userPassword varchar(255),
     role varchar(10),
     created timestamp
 );
 
 insert into users (id, userName, email, userPassword, role, created) values(1,'username1', 'abc@xyz.com', 'pwd1','ADMIN', CURRENT_TIMESTAMP);
 insert into users (id, userName, email, userPassword, role, created) values(1,'username2', 'qrs@xyz.com', 'pwd2','USER', CURRENT_TIMESTAMP);
 
 */