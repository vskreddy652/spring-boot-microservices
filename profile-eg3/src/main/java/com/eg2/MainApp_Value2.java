package com.eg2;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
 
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
 
/*
 mvn spring-boot:run -Dspring.profiles.active=qa
 NOTE: Give above command from command line
 replace qa with dev
 */
@Configuration
@ConfigurationProperties("cmdb")
class CmdbProperties {
 
    private String resourceUrl;
    private List<Integer> resourcePort;
 
    @Override
    public String toString() {
 
        return "resourceUrl: "+ this.resourceUrl+"\n"
                + "resourcePort: "+this.resourcePort+"\n";
    }
 
    public String getResourceUrl() {
        return resourceUrl;
    }
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
    public List<Integer> getResourcePort() {
        return resourcePort;
    }
    public void setResourcePort(List<Integer> resourcePort) {
        this.resourcePort = resourcePort;
    }
}

@SpringBootApplication
public class MainApp_Value2
{
	  @PostConstruct
	  public void print() {
	    System.out.println("xxxxxxxxxxxxx:"+cmdbProperties.toString());
	  }
	  
	    @Autowired
	    private CmdbProperties cmdbProperties;
	  
    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(MainApp_Value2.class, args);
        
    }
}