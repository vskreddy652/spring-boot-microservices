package org.exampledriven.sleuth;

import org.exampledriven.sleuth.rest.SleuthCommonsRequestLoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;


@Configuration
public class Config {

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private Tracer tracer;

    @Bean
    public Filter logFilter() {

        SleuthCommonsRequestLoggingFilter filter = new SleuthCommonsRequestLoggingFilter();

        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(5120);
        return filter;
    }
}
