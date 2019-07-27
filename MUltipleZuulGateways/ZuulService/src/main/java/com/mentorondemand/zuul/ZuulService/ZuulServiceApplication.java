package com.mentorondemand.zuul.ZuulService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

/*
 http://localhost:8989/mentorportal/skillTechService/skillstech/hello
 */
@EnableDiscoveryClient
@EnableZuulProxy //enable Zuul gateway
@SpringBootApplication
public class ZuulServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServiceApplication.class, args);
	}

	  @Bean
	  public SimpleFilter simpleFilter() {
	     return new SimpleFilter();
	  }
}


class SimpleFilter extends ZuulFilter {

   //private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

   @Override
   public String filterType() {
       return "pre";
   }

   @Override
   public int filterOrder() {
       return 1;
   }

   @Override
   public boolean shouldFilter() {
       return true;
   }

   @Override
   public Object run() {
       RequestContext ctx = RequestContext.getCurrentContext();
       HttpServletRequest request = ctx.getRequest();
       
       

       //log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

       System.out.println("Thissssssssssssssssss isssssss to testtttttttttttttttt "+request.getMethod());
       return null;
   }
}